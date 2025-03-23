package com.taxcalculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TaxCalculatorApplication.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class TaxControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void calculateTax_validIncome_returnsCorrectTax() throws Exception {
        mockMvc.perform(get("/api/tax")
                        .param("income", "2500000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tax").value(583250.00));
    }

    @Test
    void calculateTax_validIncomeWithCommas_returnsCorrectTax() throws Exception {
        mockMvc.perform(get("/api/tax")
                .param("income", "20,00,000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tax").value(408250.00));
    }

    @Test
    void calculateTax_negativeIncome_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/tax")
                        .param("income", "-500000"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Income cannot be negative"));
    }

    @Test
    void calculateTax_notNumberIncome_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/tax")
                        .param("income", "income"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid income format. Please provide a valid number"));
    }
}