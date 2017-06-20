package com.agh.inzynierka.controller;

import com.agh.inzynierka.dao.UserDao;
import com.agh.inzynierka.model.User;
import com.agh.inzynierka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.security.Principal;

import static java.math.BigInteger.ONE;

@Controller
public class MainController {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;
    BigInteger i = ONE;

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String getMainPage() {

        return "main";
    }

    @ResponseBody
    @RequestMapping(value = "mm", method = RequestMethod.GET)
    public String getUsersPage(Principal principal) {

        userDao.save(User.builder()
                .firstName("pawel " + i)
                .lastName("rz")
                .email("q" + i + "@q" + i + "q.q" + i + "q")
                .externalId("MI6", "007")
                .build()
        );

        i = i.add(ONE);
        userDao.save(User.builder()
                .firstName("facebook " + i)
                .lastName("rz")
                .email(i + "@" + i + "." + i)
                .externalId("MI6", "007")
                .build()
        );
        return userDao.findAll().toString() + "\n" + Long.valueOf(principal.getName()) + "<p>"
                + "test querys: look for firstName *facebook* <p>"
                + userDao.findByFirstNameLike("facebook");
    }
}
