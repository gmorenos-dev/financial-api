package com.gmoreno.financialapi;

import com.gmoreno.financialapi.model.Transaction;
import com.gmoreno.financialapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Optional;
import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDate;



@SpringBootTest
@AutoConfigureMockMvc
class FinancialApiApplicationTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    void shouldGetTransactionByIdThroughHttp() throws Exception {
        Transaction savedTransaction = transactionRepository.save(
                new Transaction(
                        "Teste HTTP GET",
                        new BigDecimal("150.00"),
                        "RECEITA",
                        LocalDate.now()
                )
        );

        mockMvc.perform(get("/api/transactions/{id}", savedTransaction.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTransaction.getId()))
                .andExpect(jsonPath("$.description").value("Teste HTTP GET"))
                .andExpect(jsonPath("$.amount").value(150.00))
                .andExpect(jsonPath("$.type").value("RECEITA"));
    }

    @Test
    void shouldReturnNotFoundWhenTransactionDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/transactions/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateTransactionThroughHttp() throws Exception {
        String json = """
        {
            "description": "Teste HTTP POST",
            "amount": 250.00,
            "type": "DESPESA",
            "date": "2026-09-22"
        }
        """;

        mockMvc.perform(post("/api/transactions")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.description").value("Teste HTTP POST"))
                .andExpect(jsonPath("$.amount").value(250.00))
                .andExpect(jsonPath("$.type").value("DESPESA"))
                .andExpect(jsonPath("$.date").value("2026-09-22"));
    }

    @Test
    void shouldGetAllTransactionsThroughHttp() throws Exception {
        transactionRepository.save(
                new Transaction(
                        "Teste HTTP LIST",
                        new BigDecimal("300.00"),
                        "RECEITA",
                        LocalDate.now()
                )
        );

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.description == 'Teste HTTP LIST')]").exists());
    }
}
