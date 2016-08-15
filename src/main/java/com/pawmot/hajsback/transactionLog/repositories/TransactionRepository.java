package com.pawmot.hajsback.transactionLog.repositories;

import com.pawmot.hajsback.transactionLog.model.transactions.Transaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, ObjectId> {
}
