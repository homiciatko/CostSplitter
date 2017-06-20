package com.agh.inzynierka.controller;

import com.agh.inzynierka.dao.DebtDao;
import com.agh.inzynierka.model.Deal;
import com.agh.inzynierka.model.Debt;
import com.agh.inzynierka.service.DealService;
import com.agh.inzynierka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pawel on 2017-04-23.
 */

@Controller
public class DebtController {

    @Autowired
    DebtDao debtDao;

    @Autowired
    UserService userService;

    @Autowired
    DealService dealService;



    @RequestMapping(value = "/debts", method = RequestMethod.GET)
    public String getDebtList(Model model, Principal principal) {

//        List<Debt> debts = new ArrayList<>(debtDao.findAllByUser(userService.getOrCreateUser(principal)));
//        model.addAttribute("debtList", debts);

        model.addAttribute("dealList", dealService.getUserDealsList(userService.getOrCreateUser(principal)));

        return "debts";
    }
}
