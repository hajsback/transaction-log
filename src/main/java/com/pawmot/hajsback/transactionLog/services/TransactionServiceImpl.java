package com.pawmot.hajsback.transactionLog.services;

import com.pawmot.hajsback.internal.api.transactions.AddDebtRequest;
import com.pawmot.hajsback.transactionLog.model.transactions.Transaction;
import com.pawmot.hajsback.transactionLog.model.transactions.TransactionFactory;
import com.pawmot.hajsback.transactionLog.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionFactory transactionFactory;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionFactory transactionFactory) {
        this.transactionRepository = transactionRepository;
        this.transactionFactory = transactionFactory;
    }

    @Override
    public void process(AddDebtRequest request) {
        // TODO: validation!
        Transaction transaction = transactionFactory.create(request.getCreditorEmail(), request.getDebtorEmail(), request.getAmount());

        transactionRepository.save(transaction);
    }
}
