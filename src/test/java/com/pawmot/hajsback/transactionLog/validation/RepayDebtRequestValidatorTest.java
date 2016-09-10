package com.pawmot.hajsback.transactionLog.validation;

import com.pawmot.hajsback.transactionLog.dto.transactions.RepayDebtRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RepayDebtRequestValidatorTest {
    private RepayDebtRequestValidator validator = new RepayDebtRequestValidatorImpl();

    @Test
    public void shouldAcceptPositiveAmountMatchingCreditorAddress() {
        RepayDebtRequest request = getRepayDebtRequestBuilderWithDefaultData()
                .build();

        boolean result = validator.validate(request, "test1@t.pl");

        assertEquals(true, result);
    }

    @Test
    public void positiveAmountMatchingDebtorAddress() {
        RepayDebtRequest request = getRepayDebtRequestBuilderWithDefaultData()
                .build();

        boolean result = validator.validate(request, "test2@t.pl");

        assertEquals(true, result);
    }

    @Test
    public void negativeAmount() {
        RepayDebtRequest request = getRepayDebtRequestBuilderWithDefaultData()
                .amount(-10)
                .build();

        boolean result = validator.validate(request, "test1@t.pl");

        assertEquals(false, result);
    }

    @Test
    public void addressNotMatching() {
        RepayDebtRequest request = getRepayDebtRequestBuilderWithDefaultData()
                .build();

        boolean result = validator.validate(request, "test4@t.pl");

        assertEquals(false, result);
    }

    private RepayDebtRequest.RepayDebtRequestBuilder getRepayDebtRequestBuilderWithDefaultData() {
        return RepayDebtRequest.builder()
                .amount(10)
                .payerEmail("test1@t.pl")
                .payeeEmail("test2@t.pl");
    }
}
