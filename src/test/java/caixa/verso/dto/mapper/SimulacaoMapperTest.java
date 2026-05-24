package caixa.verso.dto.mapper;

import static org.junit.jupiter.api.Assertions.*;

import caixa.verso.dto.ParcelaMensalResponse;
import caixa.verso.dto.SimulacaoRequest;
import caixa.verso.dto.SimulacaoResponse;
import caixa.verso.model.ParcelaMensal;
import caixa.verso.model.Simulacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class SimulacaoMapperTest {

    @Test
    void deveMapearSimulacaoCompleta() {
        Simulacao simulacao = new Simulacao();
        simulacao.id = 1L;
        simulacao.valorInicial = new BigDecimal("1000.00");
        simulacao.taxaJurosMensal = new BigDecimal("1.5");
        simulacao.prazoMeses = 12;
        simulacao.valorTotalFinal = new BigDecimal("1195.62");
        simulacao.valorTotalJuros = new BigDecimal("195.62");
        simulacao.dataCriacao = LocalDateTime.now();

        ParcelaMensal p1 = new ParcelaMensal();
        p1.mes = 1;
        p1.saldoInicial = new BigDecimal("1000.00");
        p1.juros = new BigDecimal("15.00");
        p1.saldoFinal = new BigDecimal("1015.00");
        simulacao.parcelas = new ArrayList<>();
        simulacao.parcelas.add(p1);

        SimulacaoResponse response = SimulacaoMapper.toResponse(simulacao);

        assertEquals(1L, response.id());
        assertEquals(new BigDecimal("1000.00"), response.valorInicial());
        assertEquals(1, response.parcelas().size());

        ParcelaMensalResponse primeiraParcela = response.parcelas().get(0);
        assertEquals(1, primeiraParcela.mes());
        assertEquals(new BigDecimal("15.00"), primeiraParcela.juros());
    }

    @Test
    void deveMapearSimulacaoSemParcelas() {
        Simulacao simulacao = new Simulacao();
        simulacao.id = 2L;
        simulacao.valorInicial = BigDecimal.TEN;
        simulacao.taxaJurosMensal = BigDecimal.ONE;
        simulacao.prazoMeses = 1;
        simulacao.valorTotalFinal = BigDecimal.TEN;
        simulacao.valorTotalJuros = BigDecimal.ZERO;
        simulacao.parcelas = Collections.emptyList();

        SimulacaoResponse response = SimulacaoMapper.toResponse(simulacao);
        assertNotNull(response);
        assertTrue(response.parcelas().isEmpty());
    }

    @Test
    void deveTerEqualsEHashCodeConsistentes() {
        var req1 = new SimulacaoRequest(new BigDecimal("100"), new BigDecimal("2"), 5);
        var req2 = new SimulacaoRequest(new BigDecimal("100"), new BigDecimal("2"), 5);
        var reqDiferente = new SimulacaoRequest(new BigDecimal("200"), new BigDecimal("2"), 5);

        assertEquals(req1, req2);
        assertNotEquals(req1, reqDiferente);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void deveTerToStringNaoVazio() {
        var parcela = new ParcelaMensalResponse(1, BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ONE);
        assertNotNull(parcela.toString());
        assertTrue(parcela.toString().contains("1"));
    }
}