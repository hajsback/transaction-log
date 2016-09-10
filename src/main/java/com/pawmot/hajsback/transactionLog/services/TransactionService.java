package com.pawmot.hajsback.transactionLog.services;

import com.pawmot.hajsback.internal.api.results.Result;
import com.pawmot.hajsback.transactionLog.dto.transactions.AddDebtRequest;
import com.pawmot.hajsback.transactionLog.dto.transactions.RepayDebtRequest;

public interface TransactionService {
    Result addDebt(AddDebtRequest request, String userEmail);

    Result repayDebt(RepayDebtRequest request, String userEmail);
}
