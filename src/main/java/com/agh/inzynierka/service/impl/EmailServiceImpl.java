package com.agh.inzynierka.service.impl;

import com.agh.inzynierka.dao.UserDao;
import com.agh.inzynierka.model.User;
import com.agh.inzynierka.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailServiceImpl implements EmailService {

    private Pattern pattern;
    private Matcher matcher;

    @Autowired
    private UserDao userDao;

    private static final String EMAILS_PATTERN =
            "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";

    @Override
    public List<User> getUserList(String string) {

        matcher = Pattern.compile(EMAILS_PATTERN).matcher(string);

        Set<User> userSet = new HashSet<>();

        while (matcher.find()) {
            userSet.add(userDao.findByEmail(matcher.group()));
        }

        return new ArrayList<User>(userSet);
    }

    @Override
    public Set<String> getEmails(String string) {

        matcher = Pattern.compile(EMAILS_PATTERN).matcher(string);

        Set<String> userSet = new HashSet<>();
        while (matcher.find()) {
            userSet.add(matcher.group());
        }

        return userSet;
    }
}
