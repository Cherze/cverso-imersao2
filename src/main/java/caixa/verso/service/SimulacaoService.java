package caixa.verso.service;

import caixa.verso.dto.SimulacaoRequest;
import caixa.verso.model.ParcelaMensal;
import caixa.verso.model.Simulacao;
import caixa.verso.repository.SimulacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class SimulacaoService {

    @Inject
    SimulacaoRepository repository;

    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_UP);

    public Simulacao criarSimulacao(SimulacaoRequest request) {
        BigDecimal taxaDecimal = request.taxaJurosMensal().divide(new BigDecimal("100"), MC);
        BigDecimal saldoInicial = request.valorInicial();
        BigDecimal saldoAtual = saldoInicial;

        List<ParcelaMensal> parcelas = new ArrayList<>();
        BigDecimal jurosTotal = BigDecimal.ZERO;

        for (int mes = 1; mes <= request.prazoMeses(); mes++) {
            BigDecimal juros = saldoAtual.multiply(taxaDecimal, MC);
            BigDecimal saldoFinal = saldoAtual.add(juros).setScale(2, RoundingMode.HALF_UP);
            // Recalcula juros com base no arredondamento do saldo final
            juros = saldoFinal.subtract(saldoAtual);

            jurosTotal = jurosTotal.add(juros);

            ParcelaMensal parcela = new ParcelaMensal();
            parcela.mes = mes;
            parcela.saldoInicial = saldoAtual;
            parcela.juros = juros.setScale(2, RoundingMode.HALF_UP);
            parcela.saldoFinal = saldoFinal;

            parcelas.add(parcela);
            saldoAtual = saldoFinal;
        }

        Simulacao simulacao = new Simulacao();
        simulacao.valorInicial = saldoInicial;
        simulacao.taxaJurosMensal = request.taxaJurosMensal();
        simulacao.prazoMeses = request.prazoMeses();
        simulacao.valorTotalFinal = saldoAtual;
        simulacao.valorTotalJuros = jurosTotal.setScale(2, RoundingMode.HALF_UP);
        simulacao.dataCriacao = LocalDateTime.now();
        simulacao.parcelas = parcelas;
        parcelas.forEach(p -> p.simulacao = simulacao);

        repository.persist(simulacao);
        return simulacao;
    }

    public Simulacao buscarPorId(Long id) {
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Simulação não encontrada"));
    }
}
