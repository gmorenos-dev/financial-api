package com.gmoreno.financialapi.dto;

import com.gmoreno.financialapi.model.Transaction;
import com.gmoreno.financialapi.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionResponse {

    private Long id;
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDate date;

    public TransactionResponse(
            Long id,
            String description,
            BigDecimal amount,
            TransactionType type,
            LocalDate date) {

        this.id = id;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDate()
        );
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }
}