package caixa.verso.repository;

import caixa.verso.model.Simulacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimulacaoRepository implements PanacheRepository<Simulacao> {
    // Panache já oferece persist, findById, etc.
}
