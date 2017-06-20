package com.agh.inzynierka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Debt {

    @Id
    private String id;

    @DBRef
    private User user;

    private String userRef;

    private Money money;

    private LocalDateTime paidDebtTime;

    private String tagId;

    @Transient
    private Tag tag;

    public boolean isPaid(){
        return money.isZero();
    }

    public String display() {
        return Optional.ofNullable(user).map(User::getEmail)
                .orElseThrow(() -> new IllegalArgumentException("User should be well defined do Debt: " + this))
                + ": " + money + ": " + isPaid();
    }
}
