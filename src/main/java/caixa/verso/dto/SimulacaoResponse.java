package caixa.verso.dto;

import java.math.BigDecimal;
import java.util.List;

public record SimulacaoResponse(
        Long id,
        BigDecimal valorInicial,
        BigDecimal taxaJurosMensal,
        Integer prazoMeses,
        BigDecimal valorTotalFinal,
        BigDecimal valorTotalJuros,
        List<ParcelaMensalResponse> parcelas
) {}


