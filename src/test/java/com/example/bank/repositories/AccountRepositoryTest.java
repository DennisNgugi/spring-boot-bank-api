package com.example.bank.repositories;

import com.example.bank.model.Account;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setup(){
        Account account = new Account();
        account.setAccountNumber(2345);
        account.setAccountBalance(2000);
        entityManager.persist(account);
    }

    @Test
    void testIfAccountBalanceIsRecieved() {
        Account account = accountRepository.findByAccountNumber(2345).get();
        assertEquals(account.getAccountNumber(),2345);
    }


}