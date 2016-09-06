package com.pawmot.hajsback.transactionLog.services;

import com.pawmot.hajsback.internal.api.results.Result;
import com.pawmot.hajsback.internal.api.transactions.AddDebtRequest;

public interface TransactionService {
    Result process(AddDebtRequest request, String userEmail);
}
