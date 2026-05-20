package caixa.verso.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "parcela_mensal")
public class ParcelaMensal extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulacao_id", nullable = false)
    public Simulacao simulacao;

    public Integer mes;

    @Column(precision = 15, scale = 2, nullable = false)
    public BigDecimal saldoInicial;

    @Column(precision = 15, scale = 2, nullable = false)
    public BigDecimal juros;

    @Column(precision = 15, scale = 2, nullable = false)
    public BigDecimal saldoFinal;
}
