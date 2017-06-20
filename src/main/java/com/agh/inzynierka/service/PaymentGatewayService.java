package com.agh.inzynierka.service;

import java.math.BigDecimal;
import java.net.URI;
import javax.servlet.http.HttpServletRequest;

import com.agh.inzynierka.utils.PaymentGatewayException;
import com.google.common.base.Preconditions;

public interface PaymentGatewayService {

    /**
     * Generic method for one-time payment initiation
     *
     * @param serialId               serial ID for the payment - needs to be set so we're able to recognize the payment after
     *                               the user approves it in the payment gateway
     * @param userVisibleDescription payment description that will be shown to the user.
     *                               The " for CURRENCY_CODE NET_AMOUNT + VAT" will be appended to the value given here
     * @param netAmount100           net amount for the payment given as integer in 0.01 of the currency
     *                               (e.g. for a payment of GBP 5.25, 525 needs to be given)
     * @param taxRate                tax rate in %
     * @param currencyCode           3-letter ISO code of the payment currency
     * @param uriReturn              URI to which the user will be redirected after they approve the payment in the payment gateway
     * @param uriCancel              URI to which the user will be redirected after they cancel the payment in the payment gateway
     * @return URI to which the customer should be redirected to process the payment agreement
     * @throws PaymentGatewayException
     */
    URI initiatePayment(
            final String serialId,
            final String userVisibleDescription,
            final int netAmount100,
            final BigDecimal taxRate,
            final String currencyCode,
            final URI uriReturn,
            final URI uriCancel
    ) throws PaymentGatewayException, PaymentGatewayException;

    /**
     * Activates a payment. Must be called before loading the uriReturn (see:
     * {@link #initiatePayment(String, String, int, BigDecimal, String, URI, URI)} )
     *
     * @param paymentId ID of the payment. Value is passed to the uriReturn as a "paymentId" query param
     * @param payerId   ID of the payer. Value is passed to the uriReturn as a "PayerId" query param (case sensitive)
     * @param expectedDescriptionStart expected payment description start, to be matched with the actual description received from PayPal
     * @param expectedNetAmount100 expected payment amount (excl. VAT) to be matched with the actual payment amount received from PayPal
     * @param expectedCurrencyCode expected payment currency code to be matched with the actual payment currency code received from PayPal
     * @throws PaymentGatewayException
     */
    void activatePayment(
            final String paymentId,
            final String payerId,
            final String expectedDescriptionStart,
            final int expectedNetAmount100,
            final String expectedCurrencyCode
    ) throws PaymentGatewayException;

    /**
     * Retrieves payment status basing on payment ID sent by the Payment Gateway
     *
     * @param paymentId ID of the payment for which the status should be retrieved.
     * @return payment status. See {@link PaymentGatewayState} for possible options
     */
    PaymentGatewayState getPaymentState(final String paymentId) throws PaymentGatewayException;

    /**
     * Retrieves payment serialId basing on payment ID sent by the Payment Gateway
     *
     * @param paymentId ID of the payment for which the status should be retrieved
     * @return payment serial ID
     */
    String getPaymentSerialId(final String paymentId) throws PaymentGatewayException;

    /**
     * Validates integrity and origin of the incoming notification
     *
     * @param request request containing the notification
     * @return {@code true} if the Payment Gateway confirmed the origin and integrity of the message, otherwise {@code false}
     */
    boolean validateNotification(final HttpServletRequest request);


    enum PaymentGatewayState {
        FAILED,
        PENDING,
        APPROVED;

        public static PaymentGatewayState getValue(final String paymentStatus) throws PaymentGatewayException {
            Preconditions.checkNotNull(paymentStatus, "paymentStatus");

            switch (paymentStatus) {
                case "failed":
                case "canceled":
                case "expired":
                    return FAILED;
                case "created":
                case "pending":
                    return PENDING;
                case "approved":
                    return APPROVED;
                default:
                    throw new PaymentGatewayException(
                            String.format("Unknown payment status received from the payment gateway: %s", paymentStatus));
            }
        }
    }
}
