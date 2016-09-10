package com.pawmot.hajsback.transactionLog.model.transactions;

public interface TransactionFactory {
    Transaction create(String sourceEmail, String targetEmail, int amount);
}
