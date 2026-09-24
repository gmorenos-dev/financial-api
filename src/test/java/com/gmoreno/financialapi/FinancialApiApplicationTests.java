package com.gmoreno.financialapi;

import com.gmoreno.financialapi.model.Transaction;
import com.gmoreno.financialapi.model.TransactionType;
import com.gmoreno.financialapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Optional;
import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class FinancialApiApplicationTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MockMvc mockMvc;

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

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
                TransactionType.DESPESA,
                date
        );

        assertEquals("Supermercado", transaction.getDescription());
        assertEquals(amount, transaction.getAmount());
        assertEquals(TransactionType.DESPESA, transaction.getType());
        assertEquals(date, transaction.getDate());
    }

    @Test
    void shouldSaveTransaction() {
        LocalDate date = LocalDate.of(2026, 9, 21);
        BigDecimal amount = new BigDecimal("200.00");

        Transaction transaction = new Transaction(
                "Teste Repository",
                amount,
                TransactionType.DESPESA,
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
              TransactionType.RECEITA,
              date
       );

       Transaction savedTransaction = transactionRepository.save(transaction);

       Optional<Transaction> foundTransaction =
               transactionRepository.findById(savedTransaction.getId());

       assertTrue(foundTransaction.isPresent());

       Transaction retrievedTransaction = foundTransaction.get();

      assertEquals("Teste FindById", retrievedTransaction.getDescription());
      assertEquals(amount, retrievedTransaction.getAmount());
      assertEquals(TransactionType.RECEITA, retrievedTransaction.getType());
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
                        TransactionType.RECEITA,
                        LocalDate.now()
                )
        );

        mockMvc.perform(get("/api/transactions/{id}", savedTransaction.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTransaction.getId()))
                .andExpect(jsonPath("$.description").value("Teste HTTP GET"))
                .andExpect(jsonPath("$.amount").value(150.00))
                .andExpect(jsonPath("$.type").value("RECEITA"));;
    }

    @Test
    void shouldReturnNotFoundWhenTransactionDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/transactions/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value("/api/transactions/999999"));
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
                        TransactionType.RECEITA,
                        LocalDate.now()
                )
        );

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.description == 'Teste HTTP LIST')]").exists());
    }
    @Test
    void shouldRejectTransactionWithoutDescription() throws Exception {
        String json = """
        {
            "description": "",
            "amount": 100.00,
            "type": "DESPESA",
            "date": "2026-09-23"
        }
        """;

        mockMvc.perform(post("/api/transactions")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectTransactionWithNegativeAmount() throws Exception {
        String json = """
        {
            "description": "Teste valor negativo",
            "amount": -100.00,
            "type": "DESPESA",
            "date": "2026-09-23"
        }
        """;

        mockMvc.perform(post("/api/transactions")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectTransactionWithoutType() throws Exception {
        String json = """
        {
            "description": "Teste sem tipo",
            "amount": 100.00,
            "type": "",
            "date": "2026-09-23"
        }
        """;

        mockMvc.perform(post("/api/transactions")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectTransactionWithoutDate() throws Exception {
        String json = """
        {
            "description": "Teste sem data",
            "amount": 100.00,
            "type": "DESPESA",
            "date": null
        }
        """;

        mockMvc.perform(post("/api/transactions")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateTransactionThroughHttp() throws Exception {
        Transaction transaction = new Transaction(
                "Conta antiga",
                new BigDecimal("100.00"),
                TransactionType.DESPESA,
                LocalDate.of(2026, 9, 20)
        );

        Transaction savedTransaction = transactionRepository.save(transaction);

        String json = """
            {
                "description": "Conta atualizada",
                "amount": 150.00,
                "type": "DESPESA",
                "date": "2026-09-25"
            }
            """;

        mockMvc.perform(put("/api/transactions/" + savedTransaction.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Conta atualizada"))
                .andExpect(jsonPath("$.amount").value(150.00))
                .andExpect(jsonPath("$.type").value("DESPESA"))
                .andExpect(jsonPath("$.date").value("2026-09-25"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingTransactionDoesNotExist() throws Exception {
        String json = """
            {
                "description": "Conta atualizada",
                "amount": 150.00,
                "type": "DESPESA",
                "date": "2026-09-25"
            }
            """;

        mockMvc.perform(put("/api/transactions/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteTransactionThroughHttp() throws Exception {
        Transaction transaction = new Transaction(
                "Conta para excluir",
                new BigDecimal("100.00"),
                TransactionType.DESPESA,
                LocalDate.of(2026, 9, 20)
        );

        Transaction savedTransaction = transactionRepository.save(transaction);

        mockMvc.perform(delete("/api/transactions/" + savedTransaction.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/transactions/" + savedTransaction.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingTransactionDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/transactions/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnValidationErrorsWhenCreatingInvalidTransaction() throws Exception {

        String requestBody = """
            {
                "description": "",
                "amount": -100,
                "type": null,
                "date": null
            }
            """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.description").value("não pode estar em branco"))
                .andExpect(jsonPath("$.errors.amount").value("deve ser maior que 0"))
                .andExpect(jsonPath("$.errors.type").value("não pode ser nulo"))
                .andExpect(jsonPath("$.errors.date").value("não pode ser nulo"));
    }


}
