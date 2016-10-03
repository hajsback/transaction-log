package com.pawmot.hajsback.transactionLog.services;

import com.pawmot.hajsback.internal.api.results.Result;
import com.pawmot.hajsback.internal.api.results.ResultKind;
import com.pawmot.hajsback.transactionLog.dto.transactions.AddDebtRequest;
import com.pawmot.hajsback.transactionLog.dto.transactions.RepayDebtRequest;
import com.pawmot.hajsback.transactionLog.model.transactions.Transaction;
import com.pawmot.hajsback.transactionLog.model.transactions.TransactionFactory;
import com.pawmot.hajsback.transactionLog.repositories.TransactionRepository;
import com.pawmot.hajsback.transactionLog.validation.AddDebtRequestValidator;
import com.pawmot.hajsback.transactionLog.validation.RepayDebtRequestValidator;
import org.apache.camel.ExchangeProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionFactory transactionFactory;
    private final AddDebtRequestValidator addDebtRequestValidator;
    private final RepayDebtRequestValidator repayDebtRequestValidator;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            TransactionFactory transactionFactory,
            AddDebtRequestValidator addDebtRequestValidator,
            RepayDebtRequestValidator repayDebtRequestValidator) {
        this.transactionRepository = transactionRepository;
        this.transactionFactory = transactionFactory;
        this.addDebtRequestValidator = addDebtRequestValidator;
        this.repayDebtRequestValidator = repayDebtRequestValidator;
    }

    @Override
    public Result<Transaction> addDebt(AddDebtRequest request, @ExchangeProperty("userEmail") String userEmail) {
        if (!addDebtRequestValidator.validate(request, userEmail)) {
            return Result.<Transaction>builder().resultKind(ResultKind.ValidationError).build();
        }

        Transaction transaction = transactionFactory.create(request.getCreditorEmail(), request.getDebtorEmail(), request.getAmount());

        transactionRepository.save(transaction);
        return Result.<Transaction>builder().resultKind(ResultKind.OK).data(transaction).build();
    }

    @Override
    public Result<Transaction> repayDebt(RepayDebtRequest request, @ExchangeProperty("userEmail") String userEmail) {
        if (!repayDebtRequestValidator.validate(request, userEmail)) {
            return Result.<Transaction>builder().resultKind(ResultKind.ValidationError).build();
        }

        Transaction transaction = transactionFactory.create(request.getPayerEmail(), request.getPayeeEmail(), request.getAmount());

        transactionRepository.save(transaction);
        return Result.<Transaction>builder().resultKind(ResultKind.OK).data(transaction).build();
    }
}
