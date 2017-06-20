package com.agh.inzynierka.service;

import com.agh.inzynierka.model.Debt;
import com.agh.inzynierka.model.User;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.util.List;

public interface DebtService {
    public List<Debt> debtsInDeal(Money dealSum, List<BigDecimal> paids, List<User> participants);

    List<String> createDebts(Money money, List<BigDecimal> paidList, List<User> userList);

    public Debt updateDebtValue(Debt debt);

    Money countDebt(Debt debt);
}
