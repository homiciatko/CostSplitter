package com.agh.inzynierka.utils;

import com.agh.inzynierka.model.Deal;
import com.agh.inzynierka.model.Debt;
import lombok.NonNull;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import static java.math.RoundingMode.HALF_DOWN;

public class DealsCalculator {

    public Money calculateTotalLoanValue(@NonNull Deal deal) {
        final CurrencyUnit currencyUnit = deal.getDebts().stream().findFirst().get().getMoney().getCurrencyUnit();
        Money totalLoanValue = deal.getDebts().stream()
                .map(Debt::getMoney)
                .reduce(Money.zero(currencyUnit), Money::plus);
        return totalLoanValue;
    }

    public Money calculateLoanValuePerParticipant(@NonNull Deal deal) {
        Money totalLoanValue = calculateTotalLoanValue(deal);
        int size = deal.getParticipants().size();
        return totalLoanValue.dividedBy(size, HALF_DOWN);
    }
}
