package caixa.verso.dto.mapper;

public class SimulacaoMapper {

    public static SimulacaoResponse toResponse(Simulacao simulacao) {
        List<ParcelaMensalResponse> parcelas = simulacao.parcelas.stream()
                .map(p -> new ParcelaMensalResponse(
                        p.mes, p.saldoInicial, p.juros, p.saldoFinal))
                .toList();

        return new SimulacaoResponse(
                simulacao.id,
                simulacao.valorInicial,
                simulacao.taxaJurosMensal,
                simulacao.prazoMeses,
                simulacao.valorTotalFinal,
                simulacao.valorTotalJuros,
                parcelas
        );
    }
}