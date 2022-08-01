package com.example.bank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WithdrawRequest {
    private int accountNumber;
    private double amount;
}
