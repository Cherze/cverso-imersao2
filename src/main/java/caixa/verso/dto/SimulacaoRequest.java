package caixa.verso.dto;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record SimulacaoRequest(
        @NotNull @DecimalMin("0.01") BigDecimal valorInicial,
        @NotNull @DecimalMin("0.0") BigDecimal taxaJurosMensal,
        @NotNull @Min(1) Integer prazoMeses
) {}