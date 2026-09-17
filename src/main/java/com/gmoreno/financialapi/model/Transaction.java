package com.gmoreno.financialapi.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    private Long id;
    private String description;
    private BigDecimal amount;
    private String type;
    private LocalDate date;

public Transaction(String description, BigDecimal amount, String type, LocalDate date) {
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

    public String getType() {
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

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

