package com.gmoreno.financialapi.dto;

import java.math.BigDecimal;

public class FinancialSummaryResponse {

    private BigDecimal totalReceitas;
    private BigDecimal totalDespesas;
    private BigDecimal saldo;

    public FinancialSummaryResponse(
            BigDecimal totalReceitas,
            BigDecimal totalDespesas,
            BigDecimal saldo) {

        this.totalReceitas = totalReceitas;
        this.totalDespesas = totalDespesas;
        this.saldo = saldo;
    }

    public BigDecimal getTotalReceitas() {
        return totalReceitas;
    }

    public BigDecimal getTotalDespesas() {
        return totalDespesas;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}