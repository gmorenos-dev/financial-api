package com.gmoreno.financialapi.controller;

import com.gmoreno.financialapi.service.TransactionService;
import com.gmoreno.financialapi.model.Transaction;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/api/transactions")
    public String createTransaction(@RequestBody Transaction transaction) {
        transactionService.save(transaction);
        return "OK";
    }
}