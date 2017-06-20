package com.agh.inzynierka.service;

import com.agh.inzynierka.model.User;

import java.util.List;
import java.util.Set;

public interface EmailService {

    public List<User> getUserList(String string);

    public Set<String> getEmails(String string);
}
