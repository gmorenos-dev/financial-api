package com.gmoreno.financialapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.gmoreno.financialapi.model.TransactionType;

public class TransactionRequest {

    @NotBlank(message = "não pode estar em branco")
    private String description;

    @Positive(message = "deve ser maior que 0")
    private BigDecimal amount;

    @NotNull(message = "não pode ser nulo")
    private TransactionType type;

    @NotNull(message = "não pode ser nulo")
    private LocalDate date;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}