package com.pawmot.hajsback.transactionLog.model.transactions;

import lombok.Getter;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Transaction {
    Transaction(String sourceEmail, String targetEmail, int amount) {
        this.sourceEmail = sourceEmail;
        this.targetEmail = targetEmail;
        this.amount = amount;
    }

    @Id
    private ObjectId id;

    @Getter
    @Email
    private String sourceEmail;

    @Getter
    @Email
    private String targetEmail;

    @Getter
    private int amount;
}
