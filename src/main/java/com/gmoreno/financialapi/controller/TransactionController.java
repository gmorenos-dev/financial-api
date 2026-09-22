package com.gmoreno.financialapi.controller;

import com.gmoreno.financialapi.service.TransactionService;
import com.gmoreno.financialapi.model.Transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/api/transactions")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.save(transaction);
        return ResponseEntity.status(201).body(savedTransaction);
    }

    @GetMapping("/api/transactions")
    public List<Transaction> getAllTransactions() {

        return transactionService.findAll();
    }
    @GetMapping("/api/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.findById(id);

        if (transaction.isPresent()) {
            return ResponseEntity.ok(transaction.get());
        }

        return ResponseEntity.notFound().build();
    }

}