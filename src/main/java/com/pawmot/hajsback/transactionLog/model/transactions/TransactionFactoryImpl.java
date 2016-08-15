package com.pawmot.hajsback.transactionLog.model.transactions;

import org.springframework.stereotype.Component;

@Component
class TransactionFactoryImpl implements TransactionFactory {
    @Override
    public Transaction create(String creditorEmail, String debtorEmail, int amount) {
        return new Transaction(creditorEmail, debtorEmail, amount);
    }
}
