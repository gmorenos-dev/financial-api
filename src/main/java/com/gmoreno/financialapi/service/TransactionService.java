package com.gmoreno.financialapi.service;

import com.gmoreno.financialapi.exception.TransactionNotFoundException;
import com.gmoreno.financialapi.model.Transaction;
import com.gmoreno.financialapi.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.gmoreno.financialapi.dto.FinancialSummaryResponse;
import com.gmoreno.financialapi.model.TransactionType;

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

    public FinancialSummaryResponse getFinancialSummary() {

        BigDecimal totalReceitas =
                transactionRepository.sumAmountByType(TransactionType.RECEITA);

        BigDecimal totalDespesas =
                transactionRepository.sumAmountByType(TransactionType.DESPESA);

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new FinancialSummaryResponse(
                totalReceitas,
                totalDespesas,
                saldo
        );
    }
    public FinancialSummaryResponse getFinancialSummary(
            LocalDate startDate,
            LocalDate endDate) {

        BigDecimal totalReceitas =
                transactionRepository.sumAmountByTypeAndDateBetween(
                        TransactionType.RECEITA,
                        startDate,
                        endDate
                );

        BigDecimal totalDespesas =
                transactionRepository.sumAmountByTypeAndDateBetween(
                        TransactionType.DESPESA,
                        startDate,
                        endDate
                );

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new FinancialSummaryResponse(
                totalReceitas,
                totalDespesas,
                saldo
        );
    }

}