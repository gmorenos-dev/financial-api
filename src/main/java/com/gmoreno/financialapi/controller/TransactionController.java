package com.gmoreno.financialapi.controller;

import com.gmoreno.financialapi.dto.FinancialSummaryResponse;
import com.gmoreno.financialapi.dto.TransactionRequest;
import com.gmoreno.financialapi.service.TransactionService;
import com.gmoreno.financialapi.model.Transaction;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/api/transactions")
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {

        Transaction transaction = new Transaction(
                transactionRequest.getDescription(),
                transactionRequest.getAmount(),
                transactionRequest.getType(),
                transactionRequest.getDate()
        );

        Transaction savedTransaction = transactionService.save(transaction);

        return ResponseEntity.status(201).body(savedTransaction);
    }

    @GetMapping("/api/transactions")
    public List<Transaction> getAllTransactions() {

        return transactionService.findAll();
    }
    @GetMapping("/api/transactions/summary")
    public ResponseEntity<FinancialSummaryResponse> getFinancialSummary() {
        FinancialSummaryResponse summary = transactionService.getFinancialSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/api/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.findById(id);

        return ResponseEntity.ok(transaction);

    }

    @PutMapping("/api/transactions/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest transactionRequest) {

        Transaction transactionData = new Transaction(
                transactionRequest.getDescription(),
                transactionRequest.getAmount(),
                transactionRequest.getType(),
                transactionRequest.getDate()
        );

        Transaction updatedTransaction =
                transactionService.update(id, transactionData);

        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/api/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {

        transactionService.delete(id);

        return ResponseEntity.noContent().build();
    }

}