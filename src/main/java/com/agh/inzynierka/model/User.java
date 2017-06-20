package com.agh.inzynierka.model;


import lombok.*;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    @Singular
    private List<PaymentMethod> acceptedPaymentMethods;

    @Singular
    private Map<String, String> externalIds = new HashMap<>(3, 1);

    private Role role = Role.USER;

    @Singular("tagsRef")
    private List<String> tagsRef;

    @Singular
    @Transient
    private List<Tag> tags;

    @Singular // uzytkownicy powiazani w grupy
    private Map<String, List<String>> groups;

    @Singular //uzytkownicy niezgrupowani ale jednak powiazani
    List<String> associations;

    public enum Role {
        USER, ADMIN
    }

    //    te metody na pewno powinny być w modelu ?
    public boolean lockBalanceIfCan(final Money moneyToLock) {
        //TODO w przyszlosci można lokować tylko konkretna liczbę teraz cały balance
        return false;
    }

    public boolean unlockBalance() {
        return false;
    }

    public String display() {
        return firstName + " " + lastName + ": " + email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User otherUser = (User) obj;

        if (otherUser.getId() == this.id)
            return true;
        else
            return false;
    }

}
