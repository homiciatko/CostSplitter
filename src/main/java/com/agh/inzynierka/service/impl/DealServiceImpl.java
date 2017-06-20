package com.agh.inzynierka.service.impl;

import com.agh.inzynierka.dao.DealDao;
import com.agh.inzynierka.dao.DebtDao;
import com.agh.inzynierka.dao.UserDao;
import com.agh.inzynierka.model.Deal;
import com.agh.inzynierka.model.Debt;
import com.agh.inzynierka.model.User;
import com.agh.inzynierka.service.*;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DealServiceImpl implements DealService {

    @Autowired
    private PaidService paidValidation;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DebtService debtService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DebtDao debtDao;

    @Autowired
    private DealDao dealDao;

    @Autowired
    private UserService userService;

    private List<String> tags;

    private CurrencyUnit pln = CurrencyUnit.ofCountry("PL");

    private CurrencyUnit currentCurrency = pln;

    @Override
    public Deal saveDeal(String participants, String description, BigDecimal dealSum, String paids) {

        Money money = Money.of(currentCurrency, dealSum);

        tags = new ArrayList<>();
        tags.add("sport");
        tags.add("zdrowie");

        List<User> userList = emailService.getUserList(participants);
        List<BigDecimal> paidList = paidValidation.getPaidList(paids);
        List<Debt> debtList = debtService.debtsInDeal(money, paidList, userList);

//        List<String> debtListRef = debtService.createDebts(money, paidList, userList);

        return dealDao.save(
                Deal.builder()
                .debts(debtList)
                .createDateTime(LocalDateTime.now())
                .tags(tags)
                .description(description)
                .dealSum(money)
                .build()
        );
    }

    @Override
    public List<Deal> getUserDealsList(User user){
        List<Debt> debts = new ArrayList<>(debtDao.findByUserId(user.getId()));
        List<Deal> deals = new ArrayList<>();

        debts.forEach(debt -> {
            Deal deal = dealDao.findByDebtsId(debt.getId());
            deal.setDebts(Arrays.asList(debt));
            deals.add(deal);
        });
        return deals;
    }
}
