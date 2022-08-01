package com.example.bank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String transactionCode;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    private Account account;
    private double transactionAmount;
    @CreationTimestamp
    private Date createdAt;

    public Transaction(String transactionCode, TransactionType transactionType, Account account, double transactionAmount) {
        this.transactionCode = transactionCode;
        this.transactionType = transactionType;
        this.account = account;
        this.transactionAmount = transactionAmount;
    }
}
