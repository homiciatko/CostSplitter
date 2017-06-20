package com.agh.inzynierka.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.agh.inzynierka.dao.DealDao;
import com.agh.inzynierka.dao.DebtDao;
import com.agh.inzynierka.model.Deal;
import com.agh.inzynierka.model.Debt;
import com.agh.inzynierka.service.DebtService;
import com.agh.inzynierka.service.PaymentGatewayService;
import com.agh.inzynierka.service.UserService;
import com.agh.inzynierka.utils.PaymentGatewayException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class PaymentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentGatewayService paymentGatewayService;

    @Autowired
    private DebtDao debtDao;

    @Autowired
    private DealDao dealDao;

    @Autowired
    private DebtService debtService;

    @Autowired
    private UserService userService;

    //TODO draft
    @RequestMapping({"/pay"})
    public String doPayment() {


        final Random random = new Random();

        final String paymentId;

        try {
            paymentId = String.valueOf(random.nextInt());

            paymentGatewayService.activatePayment(
                    paymentId,
                    String.valueOf(random.nextInt()),
                    "some description",
                    50,
                    "USD"
            );
        } catch (PaymentGatewayException e) {
            LOGGER.error("Failure", e);
        }


        URI uri = null;

        try {
            uri = paymentGatewayService.initiatePayment(
                    String.valueOf(random.nextInt()),
                    "some description",
                    50,
                    BigDecimal.ZERO,
                    "PLN",
                    new URI("www.google.com"),
                    new URI("www.google.com")
            );
        } catch (PaymentGatewayException e) {
        } catch (URISyntaxException e) {
            LOGGER.error("Failure", e);
        }
        return "redirect:" + uri.toString();
    }

    @RequestMapping(value = "/pay/debt/{id}", method = RequestMethod.GET)
    public String getDealDetailPage(@PathVariable String id, Model model, Principal principal) {

        List<Debt> debtList = new ArrayList<>();
        Debt debt = debtDao.findOne(id);

        if (!userService.getOrCreateUser(principal).getId().equals(debt.getUser().getId()))
            return "errorek";

        debtService.updateDebtValue(debt);
        debtList.add(debt);

        List<Deal> dealList = new ArrayList<>();
        dealList.add( dealDao.findByDebtsId(id));

        model.addAttribute("debtList", debtList);
        model.addAttribute("dealList", dealList);

        return "debt";
    }


}
