package caixa.verso.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "simulacao")
public class Simulacao extends PanacheEntity {

    @Column(precision = 15, scale = 2, nullable = false)
    public BigDecimal valorInicial;

    @Column(nullable = false)
    public BigDecimal taxaJurosMensal; // percentual, ex: 1.5

    @Column(nullable = false)
    public Integer prazoMeses;

    @Column(precision = 15, scale = 2, nullable = false)
    public BigDecimal valorTotalFinal;

    @Column(precision = 15, scale = 2, nullable = false)
    public BigDecimal valorTotalJuros;

    @Column(nullable = false)
    public LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "simulacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("mes ASC")
    public List<ParcelaMensal> parcelas = new ArrayList<>();
}


