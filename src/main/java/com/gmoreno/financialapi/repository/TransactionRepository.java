package com.gmoreno.financialapi.repository;

import com.gmoreno.financialapi.model.Transaction;
import com.gmoreno.financialapi.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByType(TransactionType type, Pageable pageable);

    Page<Transaction> findByDateBetween(
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    Page<Transaction> findByTypeAndDateBetween(
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = :type")
    BigDecimal sumAmountByType(TransactionType type);
    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.type = :type
        AND t.date BETWEEN :startDate AND :endDate
        """)
    BigDecimal sumAmountByTypeAndDateBetween(
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate
    );
}

