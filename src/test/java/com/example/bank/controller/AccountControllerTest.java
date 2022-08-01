package com.example.bank.controller;

import com.example.bank.model.Account;
import com.example.bank.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = AccountController.class)
class AccountControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void registerAccount() {
        Account mockAccount = new Account(12345,100);
        Mockito.when(accountService.registerAccount(Mockito.any(Account.class))).thenReturn(mockAccount);


    }

    @Test
    void getBalance() {
    }

    @Test
    void deposit() {
    }

    @Test
    void withdraw() {
    }
}