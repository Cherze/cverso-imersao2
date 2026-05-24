package caixa.verso.service;

import caixa.verso.dto.SimulacaoRequest;
import caixa.verso.dto.SimulacaoResponse;
import caixa.verso.dto.mapper.SimulacaoMapper;
import caixa.verso.model.Simulacao;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class) // H2 em memória para testes
class SimulacaoServiceTest {

    @Inject
    SimulacaoService service;

    @Test
    void deveCalcularJurosCompostosCorretamente() {
        SimulacaoRequest req = new SimulacaoRequest(
                new BigDecimal("1000.00"), new BigDecimal("1.5"), 12);
        Simulacao sim = service.criarSimulacao(req);

        // Valor final esperado: 1000 * (1.015)^12 ≈ 1195.62
        assertEquals(new BigDecimal("1195.63"), sim.valorTotalFinal);
        // Juros totais: 1195.62 - 1000 = 195.62
        assertEquals(new BigDecimal("195.63"), sim.valorTotalJuros);
        assertEquals(12, sim.parcelas.size());
        // Primeiro mês
        assertEquals(new BigDecimal("1000.00"), sim.parcelas.get(0).saldoInicial);
        assertEquals(new BigDecimal("15.00"), sim.parcelas.get(0).juros);
        assertEquals(new BigDecimal("1015.00"), sim.parcelas.get(0).saldoFinal);
    }

    @Test
    void deveLidarComTaxaZero() {
        SimulacaoRequest req = new SimulacaoRequest(
                new BigDecimal("500.00"), new BigDecimal("0.00"), 5);
        Simulacao sim = service.criarSimulacao(req);

        assertEquals(new BigDecimal("500.00"), sim.valorTotalFinal);
        assertEquals(new BigDecimal("0.00"), sim.valorTotalJuros);
        sim.parcelas.forEach(p -> assertEquals(new BigDecimal("0.00"), p.juros));
    }

    @Test
    void deveLancarExcecaoQuandoSimulacaoNaoEncontrada() {
        assertThrows(NotFoundException.class, () -> service.buscarPorId(999L));
    }

    @Test
    void deveMapearSimulacaoParaResponseCorretamente() {
        Simulacao simulacao = new Simulacao();
        simulacao.id = 1L;
        simulacao.valorInicial = BigDecimal.TEN;
        // ... preencher parcelas ...
        SimulacaoResponse response = SimulacaoMapper.toResponse(simulacao);
        assertEquals(1L, response.id());
        // ... asserts adicionais
    }

    @Test
    void deveGarantirQueSomaDosJurosMensaisIgualTotalJuros() {
        SimulacaoRequest req = new SimulacaoRequest(
                new BigDecimal("5000.00"), new BigDecimal("2.0"), 6);
        Simulacao sim = service.criarSimulacao(req);

        BigDecimal somaJurosMensais = sim.parcelas.stream()
                .map(p -> p.juros)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(sim.valorTotalJuros, somaJurosMensais,
                "A soma dos juros mensais deve ser igual ao total de juros");
    }

    @Test
    void deveTratarTaxaFracionariaComArredondamentoCorreto() {
        // Taxa de 3,33% ao mês, principal 2000, 3 meses
        SimulacaoRequest req = new SimulacaoRequest(
                new BigDecimal("2000.00"), new BigDecimal("3.33"), 3);
        Simulacao sim = service.criarSimulacao(req);

        // Cálculo mês a mês com arredondamento esperado:
        // Mês 1: juros = 2000 * 0.0333 = 66.60 → saldo 2066.60
        // Mês 2: juros = 2066.60 * 0.0333 ≈ 68.816 ≈ 68.82 → saldo 2135.42
        // Mês 3: juros = 2135.42 * 0.0333 ≈ 71.109 ≈ 71.11 → saldo 2206.53
        // Total juros: 66.60 + 68.82 + 71.11 = 206.53

        assertEquals(3, sim.parcelas.size());
        assertEquals(new BigDecimal("2000.00"), sim.parcelas.get(0).saldoInicial);
        assertEquals(new BigDecimal("66.60"), sim.parcelas.get(0).juros);
        assertEquals(new BigDecimal("2066.60"), sim.parcelas.get(0).saldoFinal);

        assertEquals(new BigDecimal("2066.60"), sim.parcelas.get(1).saldoInicial);
        assertEquals(new BigDecimal("68.82"), sim.parcelas.get(1).juros);
        assertEquals(new BigDecimal("2135.42"), sim.parcelas.get(1).saldoFinal);

        assertEquals(new BigDecimal("2135.42"), sim.parcelas.get(2).saldoInicial);
        assertEquals(new BigDecimal("71.11"), sim.parcelas.get(2).juros);
        assertEquals(new BigDecimal("2206.53"), sim.parcelas.get(2).saldoFinal);

        assertEquals(new BigDecimal("206.53"), sim.valorTotalJuros);
        assertEquals(new BigDecimal("2206.53"), sim.valorTotalFinal);
    }

    @Test
    void deveSuportarPrazoLongoEValorAlto() {
        SimulacaoRequest req = new SimulacaoRequest(
                new BigDecimal("1000000.00"), new BigDecimal("0.5"), 360); // 30 anos
        Simulacao sim = service.criarSimulacao(req);

        // Verifica apenas que o número de meses é correto e que os valores são consistentes
        assertEquals(360, sim.parcelas.size());
        assertTrue(sim.valorTotalFinal.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(sim.valorTotalJuros.compareTo(BigDecimal.ZERO) > 0);
        // Última parcela deve ter saldo final igual ao total
        assertEquals(sim.valorTotalFinal, sim.parcelas.get(359).saldoFinal);
    }

    @Test
    void deveCalcularCorretamentePrazoMinimoDeUmMes() {
        SimulacaoRequest req = new SimulacaoRequest(
                new BigDecimal("1500.00"), new BigDecimal("10.0"), 1);
        Simulacao sim = service.criarSimulacao(req);

        assertEquals(1, sim.parcelas.size());
        assertEquals(new BigDecimal("1500.00"), sim.parcelas.get(0).saldoInicial);
        assertEquals(new BigDecimal("150.00"), sim.parcelas.get(0).juros); // 1500 * 0.10 = 150.00
        assertEquals(new BigDecimal("1650.00"), sim.parcelas.get(0).saldoFinal);
        assertEquals(new BigDecimal("1650.00"), sim.valorTotalFinal);
        assertEquals(new BigDecimal("150.00"), sim.valorTotalJuros);
    }
}