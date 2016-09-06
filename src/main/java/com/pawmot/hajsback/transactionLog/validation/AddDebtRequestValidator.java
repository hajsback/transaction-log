package com.pawmot.hajsback.transactionLog.validation;

import com.pawmot.hajsback.internal.api.transactions.AddDebtRequest;

public interface AddDebtRequestValidator {
    boolean validate(AddDebtRequest transaction, String userEmail);
}
