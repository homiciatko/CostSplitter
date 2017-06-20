package com.agh.inzynierka.service.impl;

import com.agh.inzynierka.dao.DealDao;
import com.agh.inzynierka.dao.DebtDao;
import com.agh.inzynierka.dao.UserDao;
import com.agh.inzynierka.model.Deal;
import com.agh.inzynierka.model.Debt;
import com.agh.inzynierka.model.User;
import com.agh.inzynierka.service.DebtService;
import com.agh.inzynierka.service.EmailService;
import com.agh.inzynierka.service.PaidService;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DebtServiceImpl implements DebtService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PaidService paidService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DebtDao debtDao;

    @Autowired
    private DealDao dealDao;

    private CurrencyUnit pln = CurrencyUnit.ofCountry("PL");

    private CurrencyUnit currentCurrency = pln;

    @Override
    public List<Debt> debtsInDeal(Money dealSum, List<BigDecimal> paids, List<User> participants) {

        if (paids.size() != participants.size()) {
            List<Debt> lista = new ArrayList<Debt>();
            lista.add(Debt.builder()
                    .user(userDao.findByEmail("dupa@dupa.pl"))
                    .money(Money.of(CurrencyUnit.EUR, BigDecimal.ONE))
                    .build()
            );
            return lista;
        }

        Money debtPerParticipant = dealSum.dividedBy(participants.size(), RoundingMode.HALF_UP);
        List<Debt> debtsInDealList = new ArrayList<>();
        Debt debt;
        User user;

        for (int i = 0; i < paids.size(); i++) {
            user = participants.get(i);
            debt = Debt.builder()
                    .user(userDao.findOne(user.getId()))
//                    .money(debtPerParticipant.minus(paids.get(i)))
                    .money(Money.of(currentCurrency, paids.get(i)))
                    .build();
            debtsInDealList.add(debtDao.save(debt));
        }

        return debtsInDealList;
    }

    @Override
    public List<String> createDebts(Money money, List<BigDecimal> paidList, List<User> userList) {

        List<String> debtsInDealListRef = new ArrayList<>();

        if (paidList.size() != userList.size()) {

            return debtsInDealListRef;
        }

        Money debtPerParticipant = money.dividedBy(userList.size(), RoundingMode.HALF_UP);
        Debt debt;
        User user;

        for (int i = 0; i < userList.size(); i++) {

            user = userList.get(i);
            debt = saveDebt(user, debtPerParticipant.minus(paidList.get(i)));
            debtsInDealListRef.add(debtDao.save(debt).getId());
        }

        return debtsInDealListRef;
    }

    @Override
    public Money countDebt(Debt debt) {

        Deal deal = dealDao.findByDebtsId(debt.getId());
        Money currentDebtInDeal = deal.getDealSum().dividedBy(deal.getDebts().size(), RoundingMode.HALF_UP).minus(debt.getMoney());

        return currentDebtInDeal;
    }

    @Override
    public Debt updateDebtValue(Debt debt) {

        debt.setMoney(countDebt(debt));
        debt.setPaidDebtTime(LocalDateTime.now());

        return debtDao.save(debt);
    }

    public Debt saveDebt(User user, Money money) {

        return Debt.builder()
                .userRef(user.getId())
                .money(money)
                .build();
    }
}
