package com.pawmot.hajsback.transactionLog.dto.transactions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepayDebtRequest {
    private String payerEmail;

    private String payeeEmail;

    private int amount;
}
