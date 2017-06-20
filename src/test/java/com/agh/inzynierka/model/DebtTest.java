package com.agh.inzynierka.model;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class DebtTest {

    @Test(expected = IllegalArgumentException.class)
    public void displayWithoutUser() {
        //given
        Debt debt = Debt.builder().id("id").money(Money.zero(CurrencyUnit.EUR)).build();

        //when
        debt.display();
    }

    @Test(expected = IllegalArgumentException.class)
    public void displayWithUserWithoutMail() {
        //given
        User user = User.builder().id("idU").firstName("f").lastName("l").build();
        Debt debtU = Debt.builder().id("id").user(user).money(Money.zero(CurrencyUnit.EUR)).build();

        //when
        debtU.display();
    }

    @Test
    public void displayWithUserWithMail() {
        //given
        User userE = User.builder().id("idU").firstName("f").lastName("l").email("email").build();
        Debt debtUwE = Debt.builder().id("id").user(userE).money(Money.zero(CurrencyUnit.EUR)).build();

        //when
        assertThat(debtUwE.display()).isNotBlank();
    }
}