package com.gmoreno.financialapi;

import com.gmoreno.financialapi.model.Transaction;
import com.gmoreno.financialapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FinancialApiApplicationTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldCreateTransaction() {
        LocalDate date = LocalDate.of(2026, 9, 17);
        BigDecimal amount = new BigDecimal("150.50");

        Transaction transaction = new Transaction(
                "Supermercado",
                amount,
                "EXPENSE",
                date
        );

        assertEquals("Supermercado", transaction.getDescription());
        assertEquals(amount, transaction.getAmount());
        assertEquals("EXPENSE", transaction.getType());
        assertEquals(date, transaction.getDate());
    }

    @Test
    void shouldSaveTransaction() {
        LocalDate date = LocalDate.of(2026, 9, 21);
        BigDecimal amount = new BigDecimal("200.00");

        Transaction transaction = new Transaction(
                "Teste Repository",
                amount,
                "EXPENSE",
                date
        );

        Transaction savedTransaction = transactionRepository.save(transaction);

        assertNotNull(savedTransaction.getId());

        Optional<Transaction> foundTransaction =
                transactionRepository.findById(savedTransaction.getId());

        assertTrue(foundTransaction.isPresent());

        Transaction retrievedTransaction = foundTransaction.get();

        assertEquals("Teste Repository", retrievedTransaction.getDescription());
        assertEquals(amount, retrievedTransaction.getAmount());
        assertEquals("EXPENSE", retrievedTransaction.getType());
        assertEquals(date, retrievedTransaction.getDate());
    }
}
