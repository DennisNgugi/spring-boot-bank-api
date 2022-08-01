package com.example.bank.repositories;

import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findAllByAccountAndCreatedAtBetweenAndTransactionType(Account account, Date startDate,
                                                                    Date endDate, TransactionType transactionType);
}
