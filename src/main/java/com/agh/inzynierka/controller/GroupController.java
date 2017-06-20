package com.agh.inzynierka.controller;


import com.agh.inzynierka.dao.GroupDao;
import com.agh.inzynierka.model.Group;
import com.agh.inzynierka.service.GroupService;
import com.agh.inzynierka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/group")
@Controller
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupDao groupDao;


    @GetMapping()
    public String getUserGroupsPage(Principal principal){

        return "groups";
    }

    @GetMapping("/create")
    public String getGroupForm() {

        return "group-create";
    }

    @PostMapping("/create")
    public String savegroup(@ModelAttribute Group group, Principal principal) {


        groupService.savegroup(group, userService.getOrCreateUser(principal));

        return "redirect:/group";
    }

    @GetMapping("/edit/{id}")
    public String getgroupEditPage(@PathVariable String id, Model model) {

        model.addAttribute("group", groupDao.findOne(id));

        return "group-create";
    }

}