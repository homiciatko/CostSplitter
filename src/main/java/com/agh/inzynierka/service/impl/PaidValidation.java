package com.agh.inzynierka.service.impl;

import com.agh.inzynierka.service.PaidService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PaidValidation implements PaidService {

    private Pattern pattern;
    private Matcher matcher;

    private static final String PAIDS_PATTERNS =
            "\\d+";

    @Override
    public List<BigDecimal> getPaidList(String string) {

        matcher = Pattern.compile(PAIDS_PATTERNS).matcher(string);

        List<BigDecimal> paidList = new ArrayList<>();

        while (matcher.find()) {
            paidList.add(BigDecimal.valueOf(Long.parseLong(matcher.group())));
        }

        return paidList;
    }

    @Override
    public boolean isPaidSumValid(BigDecimal dealSum, List<BigDecimal> paids) {
        BigDecimal paidSum = BigDecimal.ZERO;
        for (BigDecimal paid : paids)
            paidSum.add(paid);

        if (paidSum.equals(dealSum))
            return true;

        return false;
    }
}
