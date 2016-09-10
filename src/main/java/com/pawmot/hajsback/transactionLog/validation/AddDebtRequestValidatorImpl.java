package com.pawmot.hajsback.transactionLog.validation;

import com.pawmot.hajsback.transactionLog.dto.transactions.AddDebtRequest;
import org.springframework.stereotype.Component;

@Component
class AddDebtRequestValidatorImpl implements AddDebtRequestValidator {
    @Override
    public boolean validate(AddDebtRequest request, String userEmail) {
        return request.getAmount() > 0 &&
                (request.getDebtorEmail().equals(userEmail) || request.getCreditorEmail().equals(userEmail));
    }
}
