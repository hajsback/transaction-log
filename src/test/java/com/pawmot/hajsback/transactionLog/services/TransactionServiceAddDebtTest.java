package com.pawmot.hajsback.transactionLog.services;

import com.pawmot.hajsback.internal.api.results.Result;
import com.pawmot.hajsback.internal.api.results.ResultKind;
import com.pawmot.hajsback.transactionLog.dto.transactions.AddDebtRequest;
import com.pawmot.hajsback.transactionLog.model.transactions.Transaction;
import com.pawmot.hajsback.transactionLog.model.transactions.TransactionFactory;
import com.pawmot.hajsback.transactionLog.repositories.TransactionRepository;
import com.pawmot.hajsback.transactionLog.validation.AddDebtRequestValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceAddDebtTest {
    @Mock
    private AddDebtRequestValidator validator;

    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionFactory factory;

    private TransactionService service;

    @Before
    public void setup() {
        service = new TransactionServiceImpl(repository, factory, validator, null);
    }

    @Test
    public void shouldReturnOKIfValidatorAcceptsInput() {
        // given
        given(validator.validate(any(), any())).willReturn(true);

        // when
        Result result = service.addDebt(AddDebtRequest.builder().build(), "any@mail.uk");

        // then
        assertEquals(ResultKind.OK, result.getResultKind());
    }

    @Test
    public void shouldReturnValidationErrorIfValidatorRejectsInput() {
        // given
        given(validator.validate(any(), any())).willReturn(false);

        // when
        Result result = service.addDebt(AddDebtRequest.builder().build(), "any@mail.uk");

        // then
        assertEquals(ResultKind.ValidationError, result.getResultKind());
    }

    @Test
    public void dontSaveWhenNotValid() {
        // given
        given(validator.validate(any(), any())).willReturn(false);

        // when
        Result result = service.addDebt(AddDebtRequest.builder().build(), "any@mail.uk");

        // then
        verify(repository, never()).save(any(Transaction.class));
    }

    @Test
    public void saveWhenValid() {
        // given
        given(validator.validate(any(), any())).willReturn(true);

        // when
        Result result = service.addDebt(AddDebtRequest.builder().build(), "any@mail.uk");

        // then
        verify(repository).save(any(Transaction.class));
    }
}
