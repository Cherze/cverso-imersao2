import caixa.verso.dto.SimulacaoRequest;
import caixa.verso.model.Simulacao;
import caixa.verso.service.SimulacaoService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
//@QuarkusTestResource(H2DatabaseTestResource.class) // H2 em memória para testes
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
}