package com.agh.inzynierka.mongo;

import com.agh.inzynierka.InzynierkaApp;
import com.agh.inzynierka.dao.UserDao;
import com.agh.inzynierka.model.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {InzynierkaApp.class})
public class InzynierkaAppMongoTest {

    @Autowired
    private UserDao userDao;

    @Before
    public void setUp() {
        userDao.deleteAll();
    }

            @Ignore
    @Test
    public void saveSomeUser() {
        String email = "a@a.a";
        User dummyUser = createDummyUser(email);
        userDao.save(dummyUser);
        User user = userDao.findByEmail(email);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(userDao.findAll().size()).isEqualTo(1);
    }

            @Ignore
    @Test
    public void editSomeUser() {
        User dummyUser = createDummyUser("b@b.b");
        userDao.save(dummyUser);
        String firstName = "Sommer";
        String lastName = "Himpson";

        User user = userDao.findAll().stream().findAny().orElseThrow(() -> new IllegalStateException("DB should contain user"));
        user.setFirstName(firstName);
        user.setLastName(lastName);

        userDao.save(user);

        User editedUser = userDao.findAll().stream().findAny().orElseThrow(() -> new IllegalStateException("DB should contain user"));
        assertThat(editedUser.getFirstName()).isEqualTo(firstName);
        assertThat(editedUser.getLastName()).isEqualTo(lastName);
    }

            @Ignore
    @Test
    public void saveSomeUserWithFriend() {
        String emailC = "c@c.c";
        User dummyUser = createDummyUser(emailC);
        userDao.save(dummyUser);

        String emailD = "d@d.d";
        User dummyUserWithFriends = createDummyUserWithFriends(emailD);
        userDao.save(dummyUserWithFriends);

        User user = userDao.findByEmail(emailD);
        assertThat(user.getAssociations().size()).isGreaterThanOrEqualTo(1);
        assertThat(user.getAssociations()).contains(userDao.findByEmail(emailC).getId());
    }

    @Ignore
    @Test
    public void deleteAllUsers() {
        userDao.deleteAll();
        assertThat(userDao.findAll()).isEmpty();
    }

    private User createDummyUser(String email) {
        String firstName = "A";
        String lastName = "B";
        String idContext = "github";
        String id = "bitowiec";
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .externalId(idContext, id)
                .build();
    }

    private User createDummyUserWithFriends(String email) {
        List<String> friendsIds = userDao.findAll().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        String firstName = "A";
        String lastName = "B";
        String idContext = "github";
        String id = "bitowiec";
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .externalId(idContext, id)
                .associations(friendsIds)
                .build();
    }
}
