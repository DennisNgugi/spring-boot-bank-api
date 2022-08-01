package com.example.bank.services;

import com.example.bank.dto.DepositRequest;
import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.model.TransactionType;
import com.example.bank.pojos.ResourceResponse;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.repositories.TransactionRepository;
import com.example.bank.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;


    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepository);
    }

    @Test
    void canRegisterAccount() {
        Account account = new Account(12345,500);

        accountService.registerAccount(account);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);

        verify(accountRepository).save(accountArgumentCaptor.capture());

        assertEquals(accountArgumentCaptor.getValue(),account);
    }

    @Test
    void checkIfAccountExists() {
        Account account = new Account(12345,500);

        given(accountRepository.findByAccountNumber(account.getAccountNumber())).willReturn(Optional.of(account));

        ResponseEntity<ResourceResponse> expectedRes = ResponseEntity.status(HttpStatus.OK).body(
                new ResourceResponse("Account balance request processed successfully", HttpStatus.OK.value(), account
                ));
        assertEquals(accountService.getBalance(account.getAccountNumber()).getStatusCode(),expectedRes.getStatusCode());

    }
    @Test
    void checkIfAccountDoesNotExists() {
        Account account = new Account(1234,500);

        accountService.getBalance(account.getAccountNumber());

        ArgumentCaptor<Integer> accountNoCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(accountRepository).findByAccountNumber(accountNoCaptor.capture());

        assertEquals(account.getAccountNumber(),accountNoCaptor.getValue());

        //given(accountRepository.findByAccountNumber(account.getAccountNumber())).willReturn(Optional.of(account));

        ResponseEntity<ResourceResponse> expectedRes = ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResourceResponse("Account number is not in our system", HttpStatus.NOT_FOUND.value(), account
                ));
        assertEquals(accountService.getBalance(account.getAccountNumber()).getStatusCode(),expectedRes.getStatusCode());

    }

    @Test
    void depositToAccount() {

        int accountNumber = 125;

        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAccountNumber(accountNumber);
        depositRequest.setAmount(200);

        accountService.deposit(depositRequest);

        ArgumentCaptor<Integer> depositRequestArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(accountRepository).findByAccountNumber(depositRequestArgumentCaptor.capture());

        ResponseEntity<ResourceResponse> expectedRes = ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResourceResponse("Account number does not exist", HttpStatus.NOT_FOUND.value(), depositRequest
                ));
        assertEquals(accountService.getBalance(depositRequest.getAccountNumber()).getStatusCode(),expectedRes.getStatusCode());



    }
    @Test
    void canDepositAndGetAllDailyTransactions(){
        int accountNo = 12345;
        int amount = 500;
        int id = 123;
        LocalDateTime date = LocalDate.of(2022,07,29).atStartOfDay();

        Account account = new Account();
        account.setId(123L);
        account.setAccountNumber(accountNo);
        account.setAccountBalance(amount);
        account.setCreated_at(Date.from(date.atZone(ZoneId.of("UTC")).toInstant()));

        Transaction transaction = new Transaction();
        transaction.setId(897L);
        transaction.setAccount(account);
        transaction.setTransactionAmount(500);
        transaction.setTransactionCode("TXN");
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setCreatedAt(Date.from(date.atZone(ZoneId.of("UTC")).toInstant()));
        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
        when(transactionRepository
                .findAllByAccountAndCreatedAtBetweenAndTransactionType(
                        (Account) any(),(java.util.Date) any()
                        ,(java.util.Date) any(),(TransactionType) any()))
                .thenReturn(new ArrayList<>());
       // List<Transaction> deposits = transactionRepository.findAllByAccountAndCreatedAtBetweenAndTransactionType(any(Account.class),any(Date.class),any(Date.class),any(TransactionType.class));
        when(accountRepository.save((Account) any())).thenReturn(account);
        when(accountRepository.findByAccountNumber(anyInt())).thenReturn(Optional.of(account));
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAccountNumber(accountNo);
        depositRequest.setAmount(amount);

        ResponseEntity<ResourceResponse> responseEntity = accountService.deposit(depositRequest);


        assertTrue(responseEntity.hasBody());
        assertTrue(responseEntity.getHeaders().isEmpty());
        //assertEquals(responseEntity.getStatusCode(),200);
        ResourceResponse response = responseEntity.getBody();
        assertEquals("Transaction has processed successfully",response.getMessage());
        assertEquals(response.getStatus(),HttpStatus.OK.value());
        assertEquals(response.getBody(),transaction);

        verify(transactionRepository).save((Transaction) any());
        verify(transactionRepository).findAllByAccountAndCreatedAtBetweenAndTransactionType((Account) any(),(java.util.Date) any()
                ,(java.util.Date) any(),(TransactionType) any());
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findByAccountNumber(anyInt());




    }

    @Test
    void withdraw() {
    }
}