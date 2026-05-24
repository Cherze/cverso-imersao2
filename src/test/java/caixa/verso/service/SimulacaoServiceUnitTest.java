package caixa.verso.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import caixa.verso.dto.SimulacaoRequest;
import caixa.verso.model.Simulacao;
import caixa.verso.repository.SimulacaoRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@QuarkusTest
class SimulacaoServiceUnitTest {

    @InjectMock
    SimulacaoRepository repository;

    @Inject
    SimulacaoService service;

    @Test
    void deveCalcularJurosCompostoSemBanco() {
        // Dados de entrada
        SimulacaoRequest req = new SimulacaoRequest(
                new BigDecimal("1000.00"), new BigDecimal("1.5"), 2);

        // Configura o mock do repositório para capturar a persistência
        doAnswer(invocation -> {
            Simulacao s = invocation.getArgument(0);
            s.id = 1L;  // simula geração de ID
            return null;
        }).when(repository).persist(any(Simulacao.class));

        Simulacao resultado = service.criarSimulacao(req);

        // Validações independentes de banco
        assertEquals(1L, resultado.id);
        assertEquals(new BigDecimal("1000.00"), resultado.valorInicial);
        assertEquals(new BigDecimal("1030.23"), resultado.valorTotalFinal); // (1000*1.015^2)
        assertEquals(new BigDecimal("30.23"), resultado.valorTotalJuros);
        assertEquals(2, resultado.parcelas.size());

        // Confirma que o repositório foi invocado
        verify(repository).persist(any(Simulacao.class));
    }

    @Test
    void deveLancarNotFoundExceptionParaIdInexistente() {
        when(repository.findByIdOptional(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.buscarPorId(99L));
    }
}
