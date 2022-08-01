package com.example.bank.services;

import com.example.bank.configurations.ConfigProperties;
import com.example.bank.dto.DepositRequest;
import com.example.bank.dto.WithdrawRequest;
import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.model.TransactionType;
import com.example.bank.pojos.ResourceResponse;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.repositories.TransactionRepository;
import com.example.bank.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.bank.utils.LogsManager;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ConfigProperties configProperties;


    public Account registerAccount(Account account) {
        return accountRepository.save(account);
    }

    public ResponseEntity<ResourceResponse> getBalance(int accountNumber) {
        Instant start = Instant.now();
        LocalDateTime atStartOfDayResult = LocalDate.of(2022, 7, 29).atStartOfDay();
        System.out.println(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Account> accountBalance = accountRepository.findByAccountNumber(accountNumber);
        if(accountBalance.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new ResourceResponse("Account number is not in our system", HttpStatus.NOT_FOUND.value(),null));
        }
        return ResponseEntity.status(HttpStatus.OK).
                body(new ResourceResponse("Account balance", HttpStatus.OK.value(), accountBalance));

    }

    public ResponseEntity<ResourceResponse> deposit(DepositRequest request) {

        Instant start = Instant.now();
        System.out.println(start);
        // check if account number exists

        Optional<Account> account = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (account.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new ResourceResponse("Account number does not exist",HttpStatus.NOT_FOUND.value(),null));
        }
        // get transactions in a day
        List<Transaction> todayDeposits = transactionRepository
                .findAllByAccountAndCreatedAtBetweenAndTransactionType(account.get(), Utils.startAndEndOfDay().get("startDay"),
                        Utils.startAndEndOfDay().get("endDay"),TransactionType.DEPOSIT);

        if (todayDeposits.stream().count() >= configProperties.getDeposit_frequency()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new ResourceResponse("You have reached your daily transaction limit",HttpStatus.FORBIDDEN.value(),null));
        }
        // get maximum limit deposited in a day
        if (todayDeposits.stream().mapToDouble(Transaction::getTransactionAmount).sum() > configProperties.getDaily_deposit_limit()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new ResourceResponse("You have reached your daily maximum deposit limit",HttpStatus.FORBIDDEN.value(),null));
        }


        // save to transaction db

        Transaction transaction = new Transaction("3dfef3", TransactionType.DEPOSIT,account.get(), request.getAmount());

        transactionRepository.save(transaction);

        double new_balance = account.get().getAccountBalance() + request.getAmount();
        account.get().setAccountBalance(new_balance);
        accountRepository.save(account.get());

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start,finish).toMillis();

        Map<String ,String> logParams = Utils.getLoggingParameters("deposit","deposit()",
                Long.toString(timeElapsed),String.valueOf(HttpStatus.OK.value()),"Success","");

        LogsManager.info("Deposit request",logParams.get("transactionType"),logParams.get("process")+" ms",logParams.get("processDuration"),
                null,"Deposit API",null,logParams.get("httpStatus"),logParams.get("responseMsg"),logParams.get("errorDescription"));

        return ResponseEntity.status(HttpStatus.OK).
                body(new ResourceResponse("Transaction has processed successfully", HttpStatus.OK.value(), transaction));

        // check if account has deposited more than 4 times

        // check if maximum deposit is less than 150k


    }

    public ResponseEntity<ResourceResponse> withdraw(WithdrawRequest request) {
        Optional<Account> account = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (account.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new ResourceResponse("Account number does not exist",HttpStatus.NOT_FOUND.value(),null));
        }

        List<Transaction> todayDeposits = transactionRepository
                .findAllByAccountAndCreatedAtBetweenAndTransactionType(account.get(), Utils.startAndEndOfDay().get("startDay"),
                        Utils.startAndEndOfDay().get("endDay"),TransactionType.WITHDRAW);

        if (todayDeposits.stream().count() >= configProperties.getWithdrawal_frequency()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new ResourceResponse("You have reached your daily transaction limit",HttpStatus.FORBIDDEN.value(),null));
        }
        // get maximum limit deposited in a day
        if (todayDeposits.stream().mapToDouble(Transaction::getTransactionAmount).sum() > configProperties.getWithdrawal_transaction_limit()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new ResourceResponse("You have reached your daily maximum Withdrawal limit",HttpStatus.FORBIDDEN.value(),null));
        }


        // save to transaction db

        Transaction transaction = new Transaction("3dfef3", TransactionType.WITHDRAW,account.get(), request.getAmount());

        transactionRepository.save(transaction);

        double new_balance = account.get().getAccountBalance() - request.getAmount();
        account.get().setAccountBalance(new_balance);
        accountRepository.save(account.get());
        System.out.println(account.get());

        return ResponseEntity.status(HttpStatus.OK).
                body(new ResourceResponse("Transaction has processed successfully", HttpStatus.OK.value(), transaction));

    }


}
