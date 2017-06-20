package com.agh.inzynierka.model.paymentMethods;

import com.agh.inzynierka.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankTransfer implements PaymentMethod {

    private
    String accountNumber;

    @Override
    public String display() {
        return accountNumber;
    }
}
