package com.gmoreno.financialapi;

import com.gmoreno.financialapi.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FinancialApiApplicationTests {

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
}
