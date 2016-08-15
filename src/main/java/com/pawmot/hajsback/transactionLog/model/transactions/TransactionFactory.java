package com.pawmot.hajsback.transactionLog.model.transactions;

public interface TransactionFactory {
    Transaction create(String creditorEmail, String debtorEmail, int amount);
}
