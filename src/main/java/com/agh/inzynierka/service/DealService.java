package com.agh.inzynierka.service;

import com.agh.inzynierka.model.Deal;
import com.agh.inzynierka.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface DealService {

    public Deal saveDeal(String participants, String description, BigDecimal dealSum, String paids);

    List<Deal> getUserDealsList(User user);
}
