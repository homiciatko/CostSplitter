package com.agh.inzynierka.model;

import lombok.*;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deal {

    @Id
    private String id;

    @DBRef
    @Singular
    private List<User> participants;


    @DBRef
    @Singular
    private List<Debt> debts;
    private List<String> debtsRef;

    private LocalDateTime createDateTime;
    @Singular
    private List<String> tags;
    private String description;
    private Money dealSum;

}
