package com.agh.inzynierka.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.agh.inzynierka.utils.PaymentGatewayException;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.ipn.IPNMessage;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;

public interface PaymentGatewayDelegate {
    String retrieveAccessToken(final OAuthTokenCredential tokenCredential) throws PaymentGatewayException;

    Payment doCreatePayment(final APIContext apiContext, final Payment payment) throws PaymentGatewayException;

    Payment executePayment(
            final APIContext apiContext,
            final Payment payment,
            final PaymentExecution paymentExecution
    ) throws PaymentGatewayException;

    boolean validateIPNMessage(final IPNMessage message);

    OAuthTokenCredential createOAuthTokenCredential(
            final String clientId,
            final String clientSecret,
            final Map<String, String> configMap
    );

    APIContext createApiContext(final Map<String, String> configMap, final String accessToken);

    Payer createPayer(final String paymentMethod);

    Details createAmountDetails();

    Amount createAmount(final String currencyCode, final String amountTotal);

    Transaction createTransaction();

    RedirectUrls createRedirectUrls();

    Payment createPayment(final String intent, final Payer payer);

    PaymentExecution createPaymentExecution(final String payerId);

    IPNMessage createIPNMessage(final HttpServletRequest request, final Map<String, String> configMap);

    Payment getPayment(final APIContext apiContext, final String paymentId) throws PaymentGatewayException;

    List<Links> getPaymentLinks(final Payment payment);

    String getLinkRel(final Links links);

    String getLinkHref(final Links links);
}