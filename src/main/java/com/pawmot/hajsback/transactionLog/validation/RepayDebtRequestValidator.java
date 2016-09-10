package com.pawmot.hajsback.transactionLog.validation;

import com.pawmot.hajsback.transactionLog.dto.transactions.RepayDebtRequest;

public interface RepayDebtRequestValidator {
    boolean validate(RepayDebtRequest transaction, String userEmail);
}
