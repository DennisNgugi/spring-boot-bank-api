package com.example.bank.configurations;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@NoArgsConstructor
public class ConfigProperties {
    @Value("${daily_deposit_limit}")
    private double daily_deposit_limit;
    @Value("${transaction_limit}")
    private double transaction_limit;
    @Value("${deposit_frequency}")
    private int deposit_frequency = 4;
    @Value("${withdrawal_transaction_limit}")
    private double withdrawal_transaction_limit;
    @Value("${daily_withdrawal_limit}")
    private double daily_withdrawal_limit;
    @Value("${withdrawal_frequency}")
    private double withdrawal_frequency;


}
