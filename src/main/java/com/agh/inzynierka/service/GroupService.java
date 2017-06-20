package com.agh.inzynierka.service;


import com.agh.inzynierka.model.Group;
import com.agh.inzynierka.model.User;

public interface GroupService {
    void savegroup(Group group, User orCreateUser);
}
