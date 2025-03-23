package com.taxcalculator.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TaxService {

    private static final BigDecimal SLAB1_LIMIT = new BigDecimal("350000");
    private static final BigDecimal SLAB2_LIMIT = new BigDecimal("625000");
    private static final BigDecimal SLAB3_LIMIT = new BigDecimal("1200000");
    private static final BigDecimal SLAB2_TAX_RATE = new BigDecimal("0.09");
    private static final BigDecimal SLAB3_TAX_RATE = new BigDecimal("0.18");
    private static final BigDecimal SLAB4_TAX_RATE = new BigDecimal("0.35");
    private static final BigDecimal SLAB2_RANGE = SLAB2_LIMIT.subtract(SLAB1_LIMIT);
    private static final BigDecimal SLAB3_RANGE = SLAB3_LIMIT.subtract(SLAB2_LIMIT);


    //@Override
    public BigDecimal calculateTax(BigDecimal income) {
        return calculateSlab2Tax(income)
                .add(calculateSlab3Tax(income))
                .add(calculateSlab4Tax(income));
    }

    private BigDecimal calculateSlab2Tax(BigDecimal income) {
        return income.compareTo(SLAB1_LIMIT) > 0 ?
                income.subtract(SLAB1_LIMIT)
                        .min(SLAB2_RANGE)
                        .multiply(SLAB2_TAX_RATE) :
                BigDecimal.ZERO;
    }

    private BigDecimal calculateSlab3Tax(BigDecimal income) {
        return income.compareTo(SLAB2_LIMIT) > 0 ?
                income.subtract(SLAB2_LIMIT)
                        .min(SLAB3_RANGE)
                        .multiply(SLAB3_TAX_RATE) :
                BigDecimal.ZERO;
    }

    private BigDecimal calculateSlab4Tax(BigDecimal income) {
        return income.compareTo(SLAB3_LIMIT) > 0 ?
                income.subtract(SLAB3_LIMIT)
                        .multiply(SLAB4_TAX_RATE) :
                BigDecimal.ZERO;
    }

}
