package com.pawmot.hajsback.transactionLog.services;

import com.pawmot.hajsback.internal.api.results.Result;
import com.pawmot.hajsback.internal.api.results.ResultKind;
import com.pawmot.hajsback.internal.api.transactions.AddDebtRequest;
import com.pawmot.hajsback.transactionLog.model.transactions.Transaction;
import com.pawmot.hajsback.transactionLog.model.transactions.TransactionFactory;
import com.pawmot.hajsback.transactionLog.repositories.TransactionRepository;
import com.pawmot.hajsback.transactionLog.validation.AddDebtRequestValidator;
import org.apache.camel.ExchangeProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionFactory transactionFactory;
    private final AddDebtRequestValidator addDebtRequestValidator;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionFactory transactionFactory, AddDebtRequestValidator addDebtRequestValidator) {
        this.transactionRepository = transactionRepository;
        this.transactionFactory = transactionFactory;
        this.addDebtRequestValidator = addDebtRequestValidator;
    }

    @Override
    public Result process(AddDebtRequest request, @ExchangeProperty("userEmail") String userEmail) {
        if (!addDebtRequestValidator.validate(request, userEmail)) {
            return Result.builder().resultKind(ResultKind.ValidationError).build();
        }
        Transaction transaction = transactionFactory.create(request.getCreditorEmail(), request.getDebtorEmail(), request.getAmount());

        transactionRepository.save(transaction);
        return Result.builder().resultKind(ResultKind.OK).build();
    }
}
