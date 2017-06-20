package com.agh.inzynierka.service;

import java.math.BigDecimal;
import java.util.List;

public interface PaidService {

    public List<BigDecimal> getPaidList(String string);

    public boolean isPaidSumValid(BigDecimal dealSum, List<BigDecimal> paid);

}
