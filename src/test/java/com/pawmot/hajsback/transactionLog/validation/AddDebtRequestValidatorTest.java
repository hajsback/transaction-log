package com.pawmot.hajsback.transactionLog.validation;

import com.pawmot.hajsback.internal.api.transactions.AddDebtRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("AddDebtRequestValidator test suite")
public class AddDebtRequestValidatorTest {
    private AddDebtRequestValidator validator = new AddDebtRequestValidatorImpl();

    @Test
    @DisplayName("Validator should accept positive amount and matching email address (with creditor)")
    public void positiveAmountMatchingCreditorAddress() {
        AddDebtRequest request = getAddDebtRequestBuilderWithDefaultData()
                .build();

        boolean result = validator.validate(request, "test1@t.pl");

        assertEquals(true, result);
    }

    @Test
    @DisplayName("Validator should accept positive amount and matching email address (with debtor)")
    public void positiveAmountMatchingDebtorAddress() {
        AddDebtRequest request = getAddDebtRequestBuilderWithDefaultData()
                .build();

        boolean result = validator.validate(request, "test2@t.pl");

        assertEquals(true, result);
    }

    @Test
    @DisplayName("Validator should reject negative amount")
    public void negativeAmount() {
        AddDebtRequest request = getAddDebtRequestBuilderWithDefaultData()
                .amount(-10)
                .build();

        boolean result = validator.validate(request, "test1@t.pl");

        assertEquals(false, result);
    }

    @Test
    @DisplayName("Validator should reject address that does not match")
    public void addressNotMatching() {
        AddDebtRequest request = getAddDebtRequestBuilderWithDefaultData()
                .build();

        boolean result = validator.validate(request, "test4@t.pl");

        assertEquals(false, result);
    }

    private AddDebtRequest.AddDebtRequestBuilder getAddDebtRequestBuilderWithDefaultData() {
        return AddDebtRequest.builder()
                .amount(10)
                .creditorEmail("test1@t.pl")
                .debtorEmail("test2@t.pl");
    }
}
