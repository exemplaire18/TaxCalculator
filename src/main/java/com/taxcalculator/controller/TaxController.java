package com.taxcalculator.controller;

import com.taxcalculator.dto.ErrorResponse;
import com.taxcalculator.dto.TaxResponse;
import com.taxcalculator.service.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/tax")
public class TaxController {

    private final TaxService taxService;

    @Autowired
    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> calculateTax(@RequestParam(name = "income") String incomeParam) {
        try {
            BigDecimal income = new BigDecimal(incomeParam.replace(",", ""));

            if (income.compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Income cannot be negative"));
            }

            BigDecimal tax = taxService.calculateTax(income);
            return ResponseEntity.ok(new TaxResponse(tax));

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Invalid income format. Please provide a valid number"));
        }
    }
}
