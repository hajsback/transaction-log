package com.pawmot.hajsback.transactionLog.services;

import com.pawmot.hajsback.internal.api.transactions.AddDebtRequest;

public interface TransactionService {
    void process(AddDebtRequest request);
}
