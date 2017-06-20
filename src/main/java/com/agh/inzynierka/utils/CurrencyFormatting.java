package com.agh.inzynierka.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.google.common.base.Preconditions;

public class CurrencyFormatting {

    public static String format(final BigDecimal amount) {
        Preconditions.checkNotNull(amount, "amount");
        Preconditions.checkArgument(amount.compareTo(BigDecimal.ZERO) >= 0, "amount must not be negative");

        final DecimalFormat format = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ROOT));
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        format.setMinimumIntegerDigits(1);
        format.setGroupingUsed(false);
        format.setRoundingMode(RoundingMode.HALF_EVEN);

        return format.format(amount);
    }
}
