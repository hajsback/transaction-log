package com.pawmot.hajsback.transactionLog.model.transactions;

import lombok.Getter;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Transaction {
    Transaction(String creditorEmail, String debtorEmail, int amount) {
        this.creditorEmail = creditorEmail;
        this.debtorEmail = debtorEmail;
        this.amount = amount;
    }

    @Id
    private ObjectId id;

    @Getter
    @Email
    private String creditorEmail;

    @Getter
    @Email
    private String debtorEmail;

    @Getter
    private int amount;
}
