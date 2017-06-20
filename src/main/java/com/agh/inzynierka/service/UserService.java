package com.agh.inzynierka.service;

import com.agh.inzynierka.dao.UserDao;
import com.agh.inzynierka.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User getOrCreateUser(Principal principal) {
        Map<String, String> accountDetails = extractAccountDetails(principal);

        User user = null;
        if (accountDetails.containsKey("link") || accountDetails.containsKey("url") || accountDetails.containsKey("email")) {

            Map<String, String> externalIds = new HashMap<>();

            extractExternalIds(accountDetails, externalIds, "email", "gmail", "email");
            extractExternalIds(accountDetails, externalIds, "url", "github", "id");
            extractExternalIds(accountDetails, externalIds, "link", "facebook", "id");

            System.out.println("ExternalIds: " + externalIds);

            List<List<User>> usersLists = externalIds.entrySet().stream()
                    .map(e -> userDao.findBySelectInExternalId(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            validate(usersLists);

            user = usersLists.stream().flatMap(Collection::stream).findAny()
                    .orElseGet(() -> {
                        User newUser = userDao.save(User.builder()
                                .firstName("A")
                                .lastName("B")
                                .email(accountDetails.get("email"))
                                .externalIds(externalIds)
                                .build()
                        );
                        System.out.println("Create new user: " + newUser);
                        return newUser;
                    });
        }
        return user;
    }

    private void validate(List<List<User>> usersLists) {
        boolean listMatchOneIdPerContext = usersLists.stream().allMatch(users -> users.size() == 1 || users.isEmpty());
        Assert.isTrue(listMatchOneIdPerContext, "More than 1 id was found for single context. " + usersLists);

        Set<User> userSet = usersLists.stream().flatMap(Collection::stream).collect(Collectors.toSet());
        List<User> users = usersLists.stream().flatMap(Collection::stream).distinct().collect(Collectors.toList());

        Assert.isTrue(userSet.size() == users.size(), "District works different than expected " + userSet);
        Assert.isTrue(userSet.size() == 1 || users.isEmpty(), "More than 1 user match to logged accounts. " + userSet);
    }

    private void extractExternalIds(Map<String, String> accountDetails, Map<String, String> externalIds, String contextValidatorField, String externalContext, String externalIdIdentifier) {
        Optional.ofNullable(accountDetails.get(contextValidatorField))
                .filter(l -> l.contains(externalContext))
                .ifPresent((String d) -> {
                    String id = accountDetails.get(externalIdIdentifier);
                    externalIds.put(externalContext, id);
                });
    }

    public User saveUser(User user) {
        User editedUser;
        if (user.getId() == null) {
            editedUser = user;

        } else {
            User oldUser = userDao.findOne(user.getId());
            editedUser = User
                    .builder()
                    .id(oldUser.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .externalIds(oldUser.getExternalIds())
                    .build();
        }

        return editedUser;
    }

    public Map<String, String> extractAccountDetails(Principal principal) {
        Map<String, Object> map = Optional.ofNullable(principal)
                .filter(OAuth2Authentication.class::isInstance)
                .map(OAuth2Authentication.class::cast)
                .map(OAuth2Authentication::getUserAuthentication)
                .filter(UsernamePasswordAuthenticationToken.class::isInstance)
                .map(UsernamePasswordAuthenticationToken.class::cast)
                .map(AbstractAuthenticationToken::getDetails)
                .filter(HashMap.class::isInstance)
                .map(HashMap.class::cast)
                .orElseGet(HashMap::new);

        return map.entrySet().stream()
                .filter(es -> Optional.ofNullable(es.getValue()).isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, es -> es.getValue().toString(),
                        (v1, v2) -> {
                            throw new RuntimeException(String.format("Duplicate key for values %s and %s", v1, v2));
                        },
                        HashMap::new));
    }
}