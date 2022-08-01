package com.example.bank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

@NoArgsConstructor
@Data
public class DepositRequest {

    private int accountNumber;
    @Max(value = 40000,message = "Maximum transaction is 40000")
    private double amount;
}
