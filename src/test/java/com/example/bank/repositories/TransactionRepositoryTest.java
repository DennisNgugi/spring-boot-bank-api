package com.example.bank.repositories;

import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.model.TransactionType;
import com.example.bank.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    void findAllByAccountAndCreatedAtBetweenAndTransactionType() {
        Account account = new Account();
        account.setAccountNumber(2345);
        account.setAccountBalance(2000);
        entityManager.persist(account);


        Transaction transaction = new Transaction();
        transaction.setTransactionCode("txn001");
        transaction.setTransactionAmount(2000);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAccount(account);
        entityManager.persist(transaction);



        List<Transaction> transactions = transactionRepository.findAllByAccountAndCreatedAtBetweenAndTransactionType(
          account,Utils.startAndEndOfDay().get("startDay"),
                Utils.startAndEndOfDay().get("endDay"),TransactionType.DEPOSIT
        );
        assertEquals(transactions.size(),1);
    }
}