package com.taxcalculator;

import com.taxcalculator.service.TaxService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class TaxServiceTest {

    @InjectMocks
    private TaxService taxService;

    @Test
    @DisplayName("Test zero income")
    void testZeroIncome() {
        BigDecimal tax = taxService.calculateTax(BigDecimal.ZERO);
        assertThat(tax).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Test negative income")
    void testNegativeIncome() {
        BigDecimal income = new BigDecimal("-50000");
        BigDecimal tax = taxService.calculateTax(income);
        assertThat(tax).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @ParameterizedTest(name = "Income: {0}, Expected Tax: {1}")
    @DisplayName("Test tax calculation for various income levels")
    @MethodSource("incomeAndTaxProvider")
    void testTaxCalculation(BigDecimal income, BigDecimal expectedTax) {
        BigDecimal tax = taxService.calculateTax(income);
        assertThat(tax).isEqualByComparingTo(expectedTax);
    }

    private static Stream<Arguments> incomeAndTaxProvider() {
        return Stream.of(
                // Income below first slab
                Arguments.of(new BigDecimal("300000"), new BigDecimal("0.00")),
                // Income at first slab limit
                Arguments.of(new BigDecimal("350000"), new BigDecimal("0.00")),
                // Income just above first slab
                Arguments.of(new BigDecimal("350001"), new BigDecimal("0.09")),
                // Income in second slab middle
                Arguments.of(new BigDecimal("500000"), new BigDecimal("13500.00")),
                // Income at second slab limit
                Arguments.of(new BigDecimal("625000"), new BigDecimal("24750.00")),
                // Income just above second slab
                Arguments.of(new BigDecimal("625001"), new BigDecimal("24750.18")),
                // Income in third slab middle
                Arguments.of(new BigDecimal("900000"), new BigDecimal("74250.00")),
                // Income at third slab limit
                Arguments.of(new BigDecimal("1200000"), new BigDecimal("128250.00")),
                // Income just above third slab
                Arguments.of(new BigDecimal("1200001"), new BigDecimal("128250.35")),
                // Income far above third slab
                Arguments.of(new BigDecimal("2500000"), new BigDecimal("583250.00"))
        );
    }
}
