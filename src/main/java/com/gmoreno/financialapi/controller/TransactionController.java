package com.gmoreno.financialapi.controller;

import com.gmoreno.financialapi.dto.FinancialSummaryResponse;
import com.gmoreno.financialapi.dto.TransactionRequest;
import com.gmoreno.financialapi.dto.TransactionResponse;
import com.gmoreno.financialapi.service.TransactionService;
import com.gmoreno.financialapi.model.Transaction;
import com.gmoreno.financialapi.dto.ErrorResponse;
import com.gmoreno.financialapi.dto.TransactionPageResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import com.gmoreno.financialapi.model.TransactionType;
import org.springdoc.core.annotations.ParameterObject;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Map;
import java.math.BigDecimal;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Transação criada com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = TransactionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados da transação inválidos",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })

    @PostMapping("/api/transactions")
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest transactionRequest) {

        Transaction transaction = new Transaction(
                transactionRequest.getDescription(),
                transactionRequest.getAmount(),
                transactionRequest.getType(),
                transactionRequest.getDate()
        );

        Transaction savedTransaction = transactionService.save(transaction);

        TransactionResponse response =
                TransactionResponse.from(savedTransaction);

        return ResponseEntity.status(201).body(response);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de transações retornada com sucesso",
                    content = @Content(
                            schema = @Schema(
                                    implementation = TransactionPageResponse.class
                            )
                    )
            )
    })

    @GetMapping("/api/transactions")
    public TransactionPageResponse getAllTransactions(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @ParameterObject
            @PageableDefault(sort = "id") Pageable pageable) {

        Page<TransactionResponse> transactionPage =
                transactionService.findAll(
                                type,
                                startDate,
                                endDate,
                                minAmount,
                                maxAmount,
                                pageable
                        )
                        .map(TransactionResponse::from);

        return new TransactionPageResponse(
                transactionPage.getContent(),
                transactionPage.getNumber(),
                transactionPage.getSize(),
                transactionPage.getTotalElements(),
                transactionPage.getTotalPages()
        );
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resumo financeiro retornado com sucesso",
                    content = @Content(
                            schema = @Schema(
                                    implementation = FinancialSummaryResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Período incompleto: informe startDate e endDate juntos",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })

    @GetMapping("/api/transactions/summary")
    public ResponseEntity<?> getFinancialSummary(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            HttpServletRequest request) {

        FinancialSummaryResponse summary;

        if (startDate == null && endDate == null) {
            summary = transactionService.getFinancialSummary();
        } else if (startDate == null || endDate == null) {

            ErrorResponse errorResponse = new ErrorResponse(
                    400,
                    "Período incompleto",
                    Map.of(
                            "period",
                            "startDate e endDate devem ser informados juntos"
                    ),
                    request.getRequestURI()
            );

            return ResponseEntity.badRequest().body(errorResponse);
        }

            else {
            summary = transactionService.getFinancialSummary(startDate, endDate);
        }

        return ResponseEntity.ok(summary);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Transação encontrada",
                    content = @Content(
                            schema = @Schema(
                                    implementation = TransactionResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transação não encontrada",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })

    @GetMapping("/api/transactions/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable Long id) {

        Transaction transaction = transactionService.findById(id);

        TransactionResponse response =
                TransactionResponse.from(transaction);

        return ResponseEntity.ok(response);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Transação atualizada com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = TransactionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados da transação inválidos",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transação não encontrada",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })

    @PutMapping("/api/transactions/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest transactionRequest) {

        Transaction transactionData = new Transaction(
                transactionRequest.getDescription(),
                transactionRequest.getAmount(),
                transactionRequest.getType(),
                transactionRequest.getDate()
        );

        Transaction updatedTransaction =
                transactionService.update(id, transactionData);

        TransactionResponse response =
                TransactionResponse.from(updatedTransaction);

        return ResponseEntity.ok(response);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Transação excluída com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Transação não encontrada",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })

    @DeleteMapping("/api/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {

        transactionService.delete(id);

        return ResponseEntity.noContent().build();
    }

}