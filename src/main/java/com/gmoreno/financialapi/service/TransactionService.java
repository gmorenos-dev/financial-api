package com.gmoreno.financialapi.service;

import com.gmoreno.financialapi.exception.TransactionNotFoundException;
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

    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() ->
                        new TransactionNotFoundException("Transação não encontrada: " + id));
    }

    public Transaction update(Long id, Transaction transactionData) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new TransactionNotFoundException("Transação não encontrada: " + id));

        transaction.setDescription(transactionData.getDescription());
        transaction.setAmount(transactionData.getAmount());
        transaction.setType(transactionData.getType());
        transaction.setDate(transactionData.getDate());

        return transactionRepository.save(transaction);
    }

    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new TransactionNotFoundException(
                    "Transação não encontrada: " + id
            );
        }

        transactionRepository.deleteById(id);
    }

}