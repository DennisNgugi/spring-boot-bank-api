package com.example.bank.controller;

import com.example.bank.dto.DepositRequest;
import com.example.bank.dto.WithdrawRequest;
import com.example.bank.model.Account;
import com.example.bank.pojos.ResourceResponse;
import com.example.bank.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/new")
    public Account registerAccount(@RequestBody Account account) {

        return accountService.registerAccount(account);

    }
    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<ResourceResponse> getBalance(@PathVariable int accountNumber){
        return accountService.getBalance(accountNumber);
    }


    @PostMapping("/deposit")
    public ResponseEntity<ResourceResponse> deposit(@RequestBody DepositRequest request){
        return accountService.deposit(request);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ResourceResponse> withdraw(@RequestBody WithdrawRequest request){
        return accountService.withdraw(request);
    }

}
