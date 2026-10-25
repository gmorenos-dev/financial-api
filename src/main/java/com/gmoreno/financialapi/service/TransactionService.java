package com.gmoreno.financialapi.service;

import com.gmoreno.financialapi.exception.InvalidDateRangeException;
import com.gmoreno.financialapi.exception.TransactionNotFoundException;
import com.gmoreno.financialapi.exception.InvalidAmountRangeException;
import com.gmoreno.financialapi.model.Transaction;
import com.gmoreno.financialapi.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
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

    public Page<Transaction> findAll(
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable
    ) {

        if (minAmount != null && minAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountRangeException(
                    "O valor mínimo não pode ser negativo"
            );
        }

        if (maxAmount != null && maxAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountRangeException(
                    "maxAmount",
                    "O valor máximo não pode ser negativo"
            );
        }


        if (minAmount != null
                && maxAmount != null
                && minAmount.compareTo(maxAmount) > 0) {
            throw new InvalidAmountRangeException(
                    "O valor mínimo não pode ser maior que o valor máximo"
            );
        }

        if ((startDate == null) != (endDate == null)) {
            throw new InvalidDateRangeException(
                    "Informe startDate e endDate para filtrar por período"
            );
        }

        if (startDate != null && startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException(
                    "A data inicial não pode ser posterior à data final"
            );
        }

        Specification<Transaction> specification =
                (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    if (type != null) {
                        predicates.add(
                                criteriaBuilder.equal(root.get("type"), type)
                        );
                    }

                    if (startDate != null) {
                        predicates.add(
                                criteriaBuilder.greaterThanOrEqualTo(
                                        root.<LocalDate>get("date"),
                                        startDate
                                )
                        );
                    }

                    if (endDate != null) {
                        predicates.add(
                                criteriaBuilder.lessThanOrEqualTo(
                                        root.<LocalDate>get("date"),
                                        endDate
                                )
                        );
                    }

                    if (minAmount != null) {
                        predicates.add(
                                criteriaBuilder.greaterThanOrEqualTo(
                                        root.<BigDecimal>get("amount"),
                                        minAmount
                                )
                        );
                    }

                    if (maxAmount != null) {
                        predicates.add(
                                criteriaBuilder.lessThanOrEqualTo(
                                        root.<BigDecimal>get("amount"),
                                        maxAmount
                                )
                        );
                    }

                    return criteriaBuilder.and(
                            predicates.toArray(new Predicate[0])
                    );
                };

        return transactionRepository.findAll(specification, pageable);
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

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException(
                    "A data inicial não pode ser posterior à data final"
            );
        }

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