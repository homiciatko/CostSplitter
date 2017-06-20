package com.agh.inzynierka.controller;

import com.agh.inzynierka.model.User;
import com.agh.inzynierka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
//@SessionScope
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping({"/login", "/me"})
    public Map<String, String> user(Principal principal) {
        Map<String, String> map = new LinkedHashMap<>();

        if (Optional.ofNullable(principal).isPresent()) {
            User user = userService.getOrCreateUser(principal);

            if (user != null) {
                map.put("name", user.getFirstName());
                map.put("surname", user.getLastName());
                map.put("email", user.getEmail());
            }

            map.put("name", Optional.of(userService.extractAccountDetails(principal))
                    .map(p -> p.get("email"))
                    //email nie jest najlepszym zrozwiazaniem zwlaszcza dla gmaila gdzie
                    // kropke mozna wpiac wszedzie
                    .orElse(principal.getName()));
        }
        return map;
    }
}
