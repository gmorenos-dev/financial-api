package com.gmoreno.financialapi.service;

import com.gmoreno.financialapi.model.Transaction;
import com.gmoreno.financialapi.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public Optional<Transaction> update(Long id, Transaction transactionData) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    transaction.setDescription(transactionData.getDescription());
                    transaction.setAmount(transactionData.getAmount());
                    transaction.setType(transactionData.getType());
                    transaction.setDate(transactionData.getDate());

                    return transactionRepository.save(transaction);
                });
    }

}