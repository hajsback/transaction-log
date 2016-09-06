package com.pawmot.hajsback.transactionLog.services;

import com.pawmot.hajsback.internal.api.results.Result;
import com.pawmot.hajsback.internal.api.results.ResultKind;
import com.pawmot.hajsback.internal.api.transactions.AddDebtRequest;
import com.pawmot.hajsback.testUtils.MockitoExtension;
import com.pawmot.hajsback.transactionLog.model.transactions.Transaction;
import com.pawmot.hajsback.transactionLog.model.transactions.TransactionFactory;
import com.pawmot.hajsback.transactionLog.repositories.TransactionRepository;
import com.pawmot.hajsback.transactionLog.validation.AddDebtRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@DisplayName("TransactionService suite")
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private static AddDebtRequestValidator validator;

    @Mock
    private static TransactionRepository repository;

    @Mock
    private static TransactionFactory factory;

    private static TransactionService service;

    @BeforeEach
    public void setup() {
        service = new TransactionServiceImpl(repository, factory, validator);
    }

    @Test
    @DisplayName("Should return ValidationError if validator returns false")
    public void validationError() {
        // given
        given(validator.validate(any(), any())).willReturn(false);

        // when
        Result result = service.process(AddDebtRequest.builder().build(), "any@mail.uk");

        // then
        assertEquals(ResultKind.ValidationError, result.getResultKind());
    }

    @Test
    @DisplayName("Should return OK if validator returns true")
    public void happyPath() {
        // given
        given(validator.validate(any(), any())).willReturn(true);

        // when
        Result result = service.process(AddDebtRequest.builder().build(), "any@mail.uk");

        // then
        assertEquals(ResultKind.OK, result.getResultKind());
    }

    @Test
    @DisplayName("Should not save anything if the validator returns false")
    public void dontSaveWhenNotValid() {
        // given
        given(validator.validate(any(), any())).willReturn(false);

        // when
        Result result = service.process(AddDebtRequest.builder().build(), "any@mail.uk");

        // then
        verify(repository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should save a transaction if the validator returns true")
    public void saveWhenValid() {
        // given
        given(validator.validate(any(), any())).willReturn(true);

        // when
        Result result = service.process(AddDebtRequest.builder().build(), "any@mail.uk");

        // then
        verify(repository).save(any(Transaction.class));
    }
}
