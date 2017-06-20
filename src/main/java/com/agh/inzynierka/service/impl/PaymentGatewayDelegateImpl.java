package com.agh.inzynierka.service.impl;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.agh.inzynierka.service.PaymentGatewayDelegate;
import com.agh.inzynierka.utils.PaymentGatewayException;
import com.google.common.base.Preconditions;
import com.paypal.api.payments.Agreement;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.ChargeModels;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.MerchantPreferences;
import com.paypal.api.payments.Patch;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentDefinition;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.Plan;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.ipn.IPNMessage;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayDelegateImpl implements PaymentGatewayDelegate {

    @Override
    public String retrieveAccessToken(final OAuthTokenCredential tokenCredential) throws PaymentGatewayException {
        Preconditions.checkNotNull(tokenCredential, "tokenCredential");

        final String accessToken;
        try {
            accessToken = tokenCredential.getAccessToken();
        } catch (PayPalRESTException e) {
            throw new PaymentGatewayException("Cannot retrieve accessToken from Paypal", e);
        }
        return accessToken;
    }


    @Override
    public Payment doCreatePayment(final APIContext apiContext, final Payment payment)
            throws PaymentGatewayException {
        Preconditions.checkNotNull(apiContext, "apiContext");
        Preconditions.checkNotNull(payment, "payment");

        try {
            return payment.create(apiContext);
        } catch (PayPalRESTException e) {
            throw new PaymentGatewayException("Failed to create payment", e);
        }
    }

    @Override
    public Payment executePayment(
            final APIContext apiContext,
            final Payment payment,
            final PaymentExecution paymentExecution) throws PaymentGatewayException {
        Preconditions.checkNotNull(apiContext, "apiContext");
        Preconditions.checkNotNull(payment, "payment");
        Preconditions.checkNotNull(paymentExecution, "paymentExecution");

        try {
            return payment.execute(apiContext, paymentExecution);
        } catch (PayPalRESTException e) {
            throw new PaymentGatewayException("Failed to execute payment", e);
        }
    }

    @Override
    public boolean validateIPNMessage(final IPNMessage message) {
        Preconditions.checkNotNull(message, "message");

        return message.validate();
    }

    @Override
    public OAuthTokenCredential createOAuthTokenCredential(
            final String clientId,
            final String clientSecret,
            final Map<String, String> configMap) {
        Preconditions.checkNotNull(clientId, "clientId");
        Preconditions.checkNotNull(clientSecret, "clientSecret");
        Preconditions.checkNotNull(configMap, "configMap");

        return new OAuthTokenCredential(clientId, clientSecret, configMap);
    }

    @Override
    public APIContext createApiContext(final Map<String, String> configMap, final String accessToken) {
        Preconditions.checkNotNull(configMap, "configMap");
        Preconditions.checkNotNull(accessToken, "accessToken");

        final APIContext apiContext = new APIContext(accessToken);
        apiContext.setConfigurationMap(configMap);
        return apiContext;
    }

    @Override
    public Payer createPayer(final String paymentMethod) {
        Preconditions.checkNotNull(paymentMethod, "paymentMethod");

        return new Payer(paymentMethod);
    }

    @Override
    public Details createAmountDetails() {
        return new Details();
    }

    @Override


    public Amount createAmount(final String currencyCode, final String amountTotal) {
        Preconditions.checkNotNull(currencyCode, "currencyCode");
        Preconditions.checkNotNull(amountTotal, "amountTotal");

        return new Amount(currencyCode, amountTotal);
    }

    @Override
    public Transaction createTransaction() {
        return new Transaction();
    }

    @Override
    public RedirectUrls createRedirectUrls() {
        return new RedirectUrls();
    }

    @Override
    public Payment createPayment(final String intent, final Payer payer) {
        Preconditions.checkNotNull(intent, "intent");
        Preconditions.checkNotNull(payer, "payer");

        return new Payment(intent, payer);
    }


    @Override
    public PaymentExecution createPaymentExecution(final String payerId) {
        Preconditions.checkNotNull(payerId, "payerId");

        return new PaymentExecution(payerId);
    }

    @Override
    public IPNMessage createIPNMessage(final HttpServletRequest request, final Map<String, String> configMap) {
        Preconditions.checkNotNull(request, "request");
        Preconditions.checkNotNull(configMap, "configMap");

        return new IPNMessage(request, configMap);
    }

    @Override
    public Payment getPayment(final APIContext apiContext, final String paymentId) throws PaymentGatewayException {
        Preconditions.checkNotNull(apiContext, "apiContext");
        Preconditions.checkNotNull(paymentId, "paymentId");

        try {
            return Payment.get(apiContext, paymentId);
        } catch (PayPalRESTException e) {
            throw new PaymentGatewayException("Cannot retrieve payment by payment ID and payer ID", e);
        }
    }

    @Override
    public List<Links> getPaymentLinks(final Payment payment) {
        Preconditions.checkNotNull(payment, "payment");

        return payment.getLinks();
    }

    @Override
    public String getLinkRel(final Links links) {
        Preconditions.checkNotNull(links, "links");

        return links.getRel();
    }

    @Override
    public String getLinkHref(final Links links) {
        Preconditions.checkNotNull(links, "links");

        return links.getHref();
    }

}