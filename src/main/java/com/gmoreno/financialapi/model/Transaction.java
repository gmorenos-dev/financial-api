package com.gmoreno.financialapi.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDate date;

protected Transaction() {
}

public Transaction(String description, BigDecimal amount, TransactionType type, LocalDate date) {
    this.description = description;
    this.amount = amount;
    this.type = type;
    this.date = date;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {

    this.description = description;
    }

    public void setAmount(BigDecimal amount) {

    this.amount = amount;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setDate(LocalDate date) {

    this.date = date;
    }
}

