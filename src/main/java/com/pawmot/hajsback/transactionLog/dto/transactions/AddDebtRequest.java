package com.pawmot.hajsback.transactionLog.dto.transactions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddDebtRequest {
    private String debtorEmail;

    private String creditorEmail;

    private int amount;
}
