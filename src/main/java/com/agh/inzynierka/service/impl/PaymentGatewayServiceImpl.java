package com.agh.inzynierka.service.impl;


import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriBuilder;

import com.agh.inzynierka.config.PaymentGatewayConfiguration;
import com.agh.inzynierka.service.PaymentGatewayDelegate;
import com.agh.inzynierka.service.PaymentGatewayService;
import com.agh.inzynierka.utils.CurrencyFormatting;
import com.agh.inzynierka.utils.PaymentGatewayException;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.Constants;
import com.paypal.base.ipn.IPNMessage;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PaymentGatewayServiceImpl implements PaymentGatewayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentGatewayServiceImpl.class);

    @Autowired
    private PaymentGatewayDelegate delegate;

    private PaymentGatewayConfiguration paymentGatewayConfig = new PaymentGatewayConfiguration();


    public URI initiatePayment(
            final String serialId,
            final String userVisibleDescription,
            final int netAmount100,
            final BigDecimal taxRate,
            final String currencyCode,
            final URI uriReturn,
            final URI uriCancel
    ) throws PaymentGatewayException {
        Preconditions.checkNotNull(serialId, "serialId");
        Preconditions.checkNotNull(userVisibleDescription, "userVisibleDescription");
        Preconditions.checkArgument(netAmount100 > 0, "netAmount100 must be a positive value");
        Preconditions.checkArgument(taxRate.compareTo(BigDecimal.ZERO) >= 0, "taxRate must be a non-negative integer");
        Preconditions.checkNotNull(currencyCode, "currencyCode");
        Preconditions.checkNotNull(uriReturn, "uriReturn");
        Preconditions.checkNotNull(uriCancel, "uriCancel");

        LOGGER.info("Contacting PayPal to initiate a Payment with" +
                        " serialID: [{}], description: [{}], netAmount*100: {}, currency: {}, and taxRate: {}",
                serialId, userVisibleDescription, netAmount100, currencyCode, taxRate);

        final APIContext apiContext = createApiContext();

        final Payment createdPayment = createPayment(
                apiContext, serialId, userVisibleDescription, netAmount100, taxRate, currencyCode, uriReturn, uriCancel);

        LOGGER.info("Successfully initiated a Payment with serialID: [{}]. Payment data: {}", serialId, createdPayment);

        return getRedirectToPaymentApproval(createdPayment);
    }

    @Override
    public void activatePayment(
            final String paymentId,
            final String payerId,
            final String expectedDescriptionStart,
            final int expectedNetAmount100,
            final String expectedCurrencyCode
    ) throws PaymentGatewayException {
        Preconditions.checkNotNull(paymentId, "paymentId");
        Preconditions.checkNotNull(payerId, "payerId");
        Preconditions.checkNotNull(expectedDescriptionStart, "expectedDescriptionStart");
        Preconditions.checkArgument(expectedNetAmount100 > 0, "netAmount100 must be a positive value");
        Preconditions.checkNotNull(expectedCurrencyCode, "expectedCurrencyCode");

        LOGGER.info("Contacting PayPal to execute a Payment with paymentID: [{}], and payerID: [{}]", paymentId, payerId);

        final APIContext apiContext = createApiContext();

        final Payment payment = delegate.getPayment(apiContext, paymentId);

        validatePayment(payment, expectedDescriptionStart, expectedNetAmount100, expectedCurrencyCode);

        LOGGER.info("Successfully validated a Payment with paymentID: [{}], and payerID: [{}]. Payment data: {}",
                paymentId, payerId, payment);

        final PaymentExecution paymentExecution = delegate.createPaymentExecution(payerId);

        final Payment executedPayment = delegate.executePayment(apiContext, payment, paymentExecution);

        final String executedPaymentState = executedPayment.getState();
        if (!Const.PAYMENT_STATE_APPROVED.equals(executedPaymentState)) {
            final String message = String.format("Cannot confirm payment activation. Payment state is: %s. Payment data: %s",
                    executedPaymentState, executedPayment);
            throw new PaymentGatewayException(message);
        }

        LOGGER.info("Successfully executed a Payment with paymentID: [{}], and payerID: [{}]. Payment data: {}",
                paymentId, payerId, executedPayment);

    }

    @Override
    public PaymentGatewayState getPaymentState(final String paymentId) throws PaymentGatewayException {
        Preconditions.checkNotNull(paymentId, "paymentId");

        final APIContext apiContext = createApiContext();

        final Payment payment = delegate.getPayment(apiContext, paymentId);

        return PaymentGatewayState.getValue(payment.getState());
    }


    @Override
    public String getPaymentSerialId(final String paymentId) throws PaymentGatewayException {
        Preconditions.checkNotNull(paymentId, "paymentId");

        final APIContext apiContext = createApiContext();

        final Payment payment = delegate.getPayment(apiContext, paymentId);

        return extractPaymentSerialId(payment);
    }

    @Override
    public boolean validateNotification(final HttpServletRequest request) {
        Preconditions.checkNotNull(request, "request");

        final Map<String, String> configMap = createConfigMap();

        final IPNMessage message = delegate.createIPNMessage(request, configMap);

        return delegate.validateIPNMessage(message);
    }

    private APIContext createApiContext() throws PaymentGatewayException {

        final Map<String, String> configMap = createConfigMap();

        final OAuthTokenCredential tokenCredential =
                delegate.createOAuthTokenCredential(paymentGatewayConfig.getClientId(), paymentGatewayConfig.getClientSecret(), configMap);

        final String accessToken = delegate.retrieveAccessToken(tokenCredential);

        return delegate.createApiContext(configMap, accessToken);
    }

    private Map<String, String> createConfigMap() {
        return ImmutableMap.of(Constants.MODE, paymentGatewayConfig.getMode());
    }


    private URI getRedirectToPaymentApproval(final Payment payment) throws PaymentGatewayException {
        return getRedirectToApproval(delegate.getPaymentLinks(payment));
    }

    private URI getRedirectToApproval(final List<Links> links) throws PaymentGatewayException {
        final Optional<Links> redirectLinkApproval =
                links.stream().filter(
                        link -> Const.LINK_APPROVAL_URL.equals(delegate.getLinkRel(link))
                ).findFirst();

        if (!redirectLinkApproval.isPresent()) {
            throw new PaymentGatewayException("Missing approval redirect link");
        }

        return UriBuilder.fromUri(delegate.getLinkHref(redirectLinkApproval.get())).build();
    }


    private Payment createPayment(
            final APIContext apiContext,
            final String serialId,
            final String userVisibleDescription,
            final int netAmount100,
            final BigDecimal taxRate,
            final String currencyCode,
            final URI uriReturn,
            final URI uriCancel
    ) throws PaymentGatewayException {

        final BigDecimal netAmount = BigDecimal.valueOf(netAmount100, 2);

        final BigDecimal taxAmount = BigDecimal.valueOf(netAmount100, 2).multiply(taxRate);

        final Details details = delegate.createAmountDetails();
        details.setSubtotal(CurrencyFormatting.format(netAmount));
        details.setTax(CurrencyFormatting.format(taxAmount));

        final Amount amount = delegate.createAmount(currencyCode, CurrencyFormatting.format(netAmount.add(taxAmount)));
        amount.setDetails(details);

        final Transaction transaction = delegate.createTransaction();
        transaction.setDescription(createPaymentDescription(userVisibleDescription, currencyCode, netAmount));
        transaction.setAmount(amount);
        transaction.setCustom(serialId);

        final Payer payer = delegate.createPayer(Const.PAYER_PAYMENT_METHOD_PAYPAL);
        payer.setStatus(Const.PAYER_STATUS_UNVERIFIED);

        final RedirectUrls redirectUrls = delegate.createRedirectUrls();
        redirectUrls.setCancelUrl(uriCancel.toString());
        redirectUrls.setReturnUrl(uriReturn.toString());

        final Payment payment = delegate.createPayment(Const.PAYMENT_INTENT_SALE, payer);
        payment.setTransactions(ImmutableList.of(transaction));
        payment.setRedirectUrls(redirectUrls);

        return delegate.doCreatePayment(apiContext, payment);
    }

    private static void validatePayment(
            final Payment payment,
            final String expectedDescriptionStart,
            final int expectedNetAmount100,
            final String expectedCurrencyCode
    ) throws PaymentGatewayException {

        final BigDecimal expectedNetAmount = BigDecimal.valueOf(expectedNetAmount100, 2);

        final String expectedDescription =
                createPaymentDescription(expectedDescriptionStart, expectedCurrencyCode, expectedNetAmount);

        final Transaction transaction = getSingleTransaction(payment);

        final String actualDescription = transaction.getDescription();
        if (!expectedDescription.equals(actualDescription)) {
            throw new PaymentGatewayException(
                    String.format("Different payment description. Expected: [%s]. Actual: [%s].", expectedDescription, actualDescription));
        }

        final Amount transactionAmount = transaction.getAmount();

        final String expectedNetAmountAsString = CurrencyFormatting.format(expectedNetAmount);
        final String actualNetAmountAsString = transactionAmount.getDetails().getSubtotal();
        if (!expectedNetAmountAsString.equals(actualNetAmountAsString)) {
            throw new PaymentGatewayException(
                    String.format(
                            "Different payment amount. Expected: [%s]. Actual: [%s].", expectedNetAmountAsString, actualNetAmountAsString
                    )
            );
        }

        final String actualCurrencyCode = transactionAmount.getCurrency();
        if (!expectedCurrencyCode.equals(actualCurrencyCode)) {
            throw new PaymentGatewayException(
                    String.format(
                            "Different payment currency. Expected: [%s]. Actual: [%s].", expectedCurrencyCode, actualCurrencyCode
                    )
            );
        }
    }


    private static String extractPaymentSerialId(final Payment payment) throws PaymentGatewayException {
        final Transaction transaction = getSingleTransaction(payment);
        return transaction.getCustom();
    }


    private static Transaction getSingleTransaction(final Payment payment) throws PaymentGatewayException {
        final List<Transaction> transactions = payment.getTransactions();

        if (transactions == null || transactions.isEmpty()) {
            throw new PaymentGatewayException("No transactions found for the activated payment");
        }

        if (transactions.size() > 1) {
            throw new PaymentGatewayException("More than one transaction found for the activated payment");
        }

        final Transaction transaction = transactions.get(0);
        if (transaction == null) {
            throw new PaymentGatewayException("Retrieved transaction cannot be null");
        }

        return transaction;
    }


    private static String createPaymentDescription(final String userVisibleDescription, final String currencyCode, final BigDecimal netAmount) {
        return String.format("%s for %s %s+VAT", userVisibleDescription, currencyCode, CurrencyFormatting.format(netAmount));
    }


    private static class PlanState {


        private final String state;

        public PlanState(final String state) {
            this.state = state;
        }


        @SuppressWarnings("unused") // used by Paypal SDK
        public String getState() {
            return state;
        }
    }

    /**
     * Contains PayPal constants defined in PayPal online documentation available <a href="https://developer.paypal.com/docs/api/">here</a>
     */
    private static class Const {

        /**
         * Indicates that the billing agreement state is active
         * For details see <a href="https://developer.paypal.com/docs/api/#agreement-object">relevant PayPal documentation</a>
         */
        public static final String AGREEMENT_STATE_ACTIVE = "Active";

        /**
         * Indicates that the charge model amount attached to the billing plan denotes the Tax part of the gross price
         * For details see <a href="https://developer.paypal.com/docs/api/#chargemodels-object">relevant PayPal documentation</a>
         */
        public static final String CHARGE_MODEL_TYPE_TAX = "TAX";

        /**
         * Indicates a regular (i.e. not trial) payment for the billing plan
         * For details see <a href="https://developer.paypal.com/docs/api/#paymentdefinition-object">relevant PayPal documentation</a>
         */
        public static final String PAYMENT_DEFINITION_TYPE_REGULAR = "REGULAR";
        /**
         * Monthly frequency of charges connected with the billing plan
         * For details see <a href="https://developer.paypal.com/docs/api/#paymentdefinition-object">relevant PayPal documentation</a>
         */
        public static final String PAYMENT_DEFINITION_FREQUENCY_MONTHLY = "MONTH";

        /**
         * The plan will not have a predefined number of periodic charges.
         * For details see <a href="https://developer.paypal.com/docs/api/#plan-object">relevant PayPal documentation</a>
         */
        public static final String BILLING_PLAN_TYPE_INFINITE = "INFINITE";
        /**
         * Indicates that the billing plan state is active.
         * For details see <a href="https://developer.paypal.com/docs/api/#plan-object">relevant PayPal documentation</a>
         */
        public static final String BILLING_PLAN_STATE_ACTIVE = "ACTIVE";

        /**
         * Patch command that updates the given object
         * For details see <a href="https://developer.paypal.com/docs/api/#patchrequest-object">relevant PayPal documentation</a>
         */
        public static final String PATCH_COMMAND_REPLACE = "replace";

        /**
         * Defines that the payment will be done in the PayPal portal and the user will not be redirected to a credit card payment company.
         * For details see <a href="https://developer.paypal.com/docs/api/#payer-object">relevant PayPal documentation</a>
         */
        public static final String PAYER_PAYMENT_METHOD_PAYPAL = "paypal";
        /**
         * Denotes a user's PayPal account status.
         * Since we have no information on our user's status, to be on the safe side UNVERIFIED has been chosen.
         * For details see <a href="https://developer.paypal.com/docs/api/#payer-object">relevant PayPal documentation</a>
         */
        public static final String PAYER_STATUS_UNVERIFIED = "UNVERIFIED";

        /**
         * Indicates a link to which the user needs to be redirected in order to accept the billing agreement
         * For details see <a href="https://developer.paypal.com/docs/api/#create-an-agreement">relevant PayPal documentation</a>
         * Also see <a href="https://developer.paypal.com/docs/api/#hateoas-links">instructions on handling PayPal links</a>
         */
        public static final String LINK_APPROVAL_URL = "approval_url";

        /**
         * Indicates that this payment is an immediate payment
         * For details see <a href="https://developer.paypal.com/docs/api/#payment-object">relevant PayPal documentation</a>
         */
        public static final String PAYMENT_INTENT_SALE = "sale";
        /**
         * Indicates that this payment has been approved by the user and confirmed by Rizikon
         * For details see <a href="https://developer.paypal.com/docs/api/#payment-object">relevant PayPal documentation</a>
         */
        public static final String PAYMENT_STATE_APPROVED = "approved";
    }
}
