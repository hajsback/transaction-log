package com.pawmot.hajsback.transactionLog.validation;

import com.pawmot.hajsback.transactionLog.dto.transactions.AddDebtRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AddDebtRequestValidatorTest {
    private AddDebtRequestValidator validator = new AddDebtRequestValidator();

    @Test
    public void shouldAcceptPositiveAmountMatchingCreditorAddress() {
        AddDebtRequest request = getAddDebtRequestBuilderWithDefaultData()
                .build();

        boolean result = validator.validate(request, "test1@t.pl");

        assertEquals(true, result);
    }

    @Test
    public void shouldAcceptPositiveAmountMatchingDebtorAddress() {
        AddDebtRequest request = getAddDebtRequestBuilderWithDefaultData()
                .build();

        boolean result = validator.validate(request, "test2@t.pl");

        assertEquals(true, result);
    }

    @Test
    public void shouldRejectNegativeAmount() {
        AddDebtRequest request = getAddDebtRequestBuilderWithDefaultData()
                .amount(-10)
                .build();

        boolean result = validator.validate(request, "test1@t.pl");

        assertEquals(false, result);
    }

    @Test
    public void shouldRejectNotMatchingAddress() {
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
