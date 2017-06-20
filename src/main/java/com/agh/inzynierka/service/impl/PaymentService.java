package com.agh.inzynierka.service.impl;

import com.agh.inzynierka.dao.DealDao;
import com.agh.inzynierka.dao.UserDao;
import com.agh.inzynierka.model.Deal;
import com.agh.inzynierka.model.Debt;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private DealDao dealDao;

    public boolean purchase(String userId, String dealId) {
        Deal oldDeal = dealDao.findById(dealId);
        List<Debt> debtList = oldDeal.getDebts().stream()
                .filter(debt -> debt.getUser().getId().equals(userId))
                .filter(debt -> !debt.isPaid())
                .collect(Collectors.toList());

        Deal newDeal;
        try {
            Debt oldDebt = debtList.stream().findFirst().orElseThrow(IllegalStateException::new);
            CurrencyUnit currency = oldDebt.getMoney().getCurrencyUnit();

            Debt newDebt = Debt.builder().user(oldDebt.getUser()).money(Money.zero(currency)).build();


            newDeal = Optional.of(oldDeal)
                    .map(old -> Deal.builder()
                            .participants(old.getParticipants())
                            .debts(filterOldDebt(old.getDebts(), oldDebt))
                            .debt(newDebt)
                            .createDateTime(old.getCreateDateTime())
                            .tags(old.getTags())
                            .description(old.getDescription())
                            .dealSum(old.getDealSum())
                            .build())
                    .orElseThrow(UnsupportedOperationException::new);
        } catch (IllegalStateException e) {
            return false;
        }

        dealDao.save(newDeal);
        dealDao.delete(oldDeal);

        return true;
    }

    private List<Debt> filterOldDebt(List<Debt> debts, Debt oldDebt) {
        return debts.stream().filter(d -> !d.getId().equals(oldDebt)).collect(Collectors.toList());
    }
}
