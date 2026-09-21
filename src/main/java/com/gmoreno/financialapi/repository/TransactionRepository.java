package com.gmoreno.financialapi.repository;

import com.gmoreno.financialapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

