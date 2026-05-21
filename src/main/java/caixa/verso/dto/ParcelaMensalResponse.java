package caixa.verso.dto;

import java.math.BigDecimal;

public record ParcelaMensalResponse(
        Integer mes,
        BigDecimal saldoInicial,
        BigDecimal juros,
        BigDecimal saldoFinal
) {}
