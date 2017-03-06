package com.pawmot.hajsback.transactionLog.validation;

import com.pawmot.hajsback.transactionLog.dto.transactions.AddDebtRequest;
import org.springframework.stereotype.Component;

@Component
public class AddDebtRequestValidator {

    public boolean validate(AddDebtRequest request, String userEmail) {
        return request.getAmount() > 0 &&
                (request.getDebtorEmail().equals(userEmail) || request.getCreditorEmail().equals(userEmail));
    }
}
