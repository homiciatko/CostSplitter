package com.agh.inzynierka.controller;

import com.agh.inzynierka.dao.UserDao;
import com.agh.inzynierka.model.User;
import com.agh.inzynierka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @GetMapping()
    public String getUserList(Model model) {

        model.addAttribute("userList", userDao.findAll());

        return "users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {

        userDao.delete(id);

        return "redirect:/user";
    }

    @GetMapping("/create")
    public String getUserForm() {

        return "user-create";
    }

    @PostMapping("/create")
    public String saveUser(@ModelAttribute User user) {


        userDao.save(userService.saveUser(user));

        return "redirect:/user";
    }

    @GetMapping("/edit/{id}")
    public String getUserEditPage(@PathVariable String id, Model model) {

        User user = userDao.findOne(id);

        model.addAttribute("user", user);

        return "user-create";
    }

//    @PostMapping("/groups/add")
//    public String addGroup(Principal principal, ){
    //TODO
//        return "user-groups";
//    }

    @PostMapping("/associations/add")
    public String addAssociations(Principal principal, @ModelAttribute List<String> associations) {
        User user = userService.getOrCreateUser(principal);
        user.setAssociations(associations);
        userDao.save(user);
        return "user-associations";
    }

    @GetMapping("/associations")
    public String getAssociations(Principal principal, Model model) {
        List<String> associations = userService.getOrCreateUser(principal).getAssociations();
        model.addAttribute("associations", associations); //todo zrobic wrzucenie maili

        return "user-associations";
    }
}
