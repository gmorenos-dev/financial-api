package com.gmoreno.financialapi;

import com.gmoreno.financialapi.model.Transaction;
import com.gmoreno.financialapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

    }

    @Test
    void shouldFindTransactionById() {
       LocalDate date = LocalDate.of(2026, 9, 21);
       BigDecimal amount = new BigDecimal("300.00");

       Transaction transaction = new Transaction(
              "Teste FindById",
              amount,
              "INCOME",
              date
       );

       Transaction savedTransaction = transactionRepository.save(transaction);

       Optional<Transaction> foundTransaction =
               transactionRepository.findById(savedTransaction.getId());

       assertTrue(foundTransaction.isPresent());

       Transaction retrievedTransaction = foundTransaction.get();

      assertEquals("Teste FindById", retrievedTransaction.getDescription());
      assertEquals(amount, retrievedTransaction.getAmount());
      assertEquals("INCOME", retrievedTransaction.getType());
      assertEquals(date, retrievedTransaction.getDate());

   }


    @Test
    void shouldFindAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();

    assertFalse(transactions.isEmpty());


    }

}
