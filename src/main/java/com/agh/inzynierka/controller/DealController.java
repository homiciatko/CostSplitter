package com.agh.inzynierka.controller;

import com.agh.inzynierka.dao.DealDao;
import com.agh.inzynierka.dao.DebtDao;
import com.agh.inzynierka.dao.UserDao;
import com.agh.inzynierka.model.Deal;
import com.agh.inzynierka.service.DealService;
import com.agh.inzynierka.service.EmailService;
import com.agh.inzynierka.service.PaidService;
import com.agh.inzynierka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;

@Controller
public class DealController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DebtDao debtDao;

    @Autowired
    private DealDao dealDao;

    @Autowired
    private DealService dealService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PaidService paidService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/allDeals", method = RequestMethod.GET)
    public String getAllDealsList(Model model) {

        model.addAttribute("dealList", dealDao.findAll());

        return "deals";
    }

    @RequestMapping(value = "/myDeals", method = RequestMethod.GET)
    public String getUserDealsListPage(Model model, Principal principal) {
//        model.addAttribute("dealList", dealDao.findByParticipantsId(userService.getOrCreateUser(principal).getId()));
        model.addAttribute("dealList", dealService.getUserDealsList(userService.getOrCreateUser(principal)));

        return "deals";
    }

    @RequestMapping(value = "/deals/detail/{id}", method = RequestMethod.GET)
    public String getDealDetailPage(@PathVariable String id, Model model) {

        Deal deal = dealDao.findOne(id);

        model.addAttribute("dealList", Arrays.asList(deal));

        model.addAttribute("debtList", deal.getDebts());

        return "debts";
    }

    @RequestMapping(value = "/deal-create", method = RequestMethod.GET)
    public String getDealForm(@ModelAttribute Deal deal) {

        deal = new Deal();

        return "deal-create";
    }

    @RequestMapping(value = "/deal-create", method = RequestMethod.POST)
    public String saveDeal(@RequestParam(required = false) String id,
                           @RequestParam String participants,
                           @RequestParam String description,
                           @RequestParam BigDecimal dealSum,
                           @RequestParam String paids
    ) {


        dealService.saveDeal(participants, description, dealSum, paids);

        return "redirect:/myDeals";
    }

    @RequestMapping(value = "/deals/delete/{id}")
    public String deleteDeal(@PathVariable String id) {

        dealDao.delete(id);

        return "redirect:/allDeals";
    }
}
