package com.agh.inzynierka.utils;

public class PaymentGatewayException extends Exception {

    private static final long serialVersionUID = 3397985619175148747L;

    public PaymentGatewayException(final String technicalMessage) {
        super(technicalMessage);
    }

    public PaymentGatewayException(final String technicalMessage, final Throwable t) {
        super(technicalMessage, t);
    }
}

