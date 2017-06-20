package com.agh.inzynierka.controller;


import com.agh.inzynierka.dao.TagDao;
import com.agh.inzynierka.dao.UserDao;
import com.agh.inzynierka.model.Tag;
import com.agh.inzynierka.service.TagService;
import com.agh.inzynierka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/tag")
@Controller
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private UserDao userDao;

    @GetMapping()
    public String getUserTagsPage(Model model, Principal principal){

        model.addAttribute("tagList", tagService.getTags(userService.getOrCreateUser(principal)));

        return "tags";
    }

    @GetMapping("/create")
    public String getTagForm(@ModelAttribute Tag tag) {

        tag = new Tag();

        return "tag-create";
    }

    @PostMapping("/create")
    public String saveTag(@ModelAttribute Tag tag, Principal principal) {

//        if (!userService.getOrCreateUser(principal).getId().equals(userDao.findByTags(tag)))
//            return "errorek";


        if(!tagService.saveTag(tag, userService.getOrCreateUser(principal)))
            return "errorek";

        return "redirect:/tag";
    }

    @GetMapping("/edit/{id}")
    public String getTagEditPage(@PathVariable String id, Model model) {

        model.addAttribute("tag", tagDao.findOne(id));

        return "tag-create";
    }

}
