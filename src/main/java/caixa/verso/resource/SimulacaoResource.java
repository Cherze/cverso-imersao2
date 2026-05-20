package caixa.verso.resource;

import caixa.verso.dto.mapper.SimulacaoMapper;
import caixa.verso.model.Simulacao;
import caixa.verso.service.SimulacaoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.net.URI;

@Path("/simulacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SimulacaoResource {

    @Inject
    SimulacaoService service;

    @POST
    @Operation(summary = "Cria uma simulação de financiamento")
    @APIResponse(responseCode = "201", description = "Simulação criada com sucesso")
    @APIResponse(responseCode = "400", description = "Dados de entrada inválidos")
    public Response criar(@Valid SimulacaoRequest request) {
        Simulacao simulacao = service.criarSimulacao(request);
        return Response
                .created(URI.create("/simulacoes/" + simulacao.id))
                .entity(SimulacaoMapper.toResponse(simulacao))
                .build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Consulta uma simulação pelo ID")
    @APIResponse(responseCode = "200", description = "Simulação encontrada")
    @APIResponse(responseCode = "404", description = "Simulação não encontrada")
    public SimulacaoResponse consultar(@PathParam("id") Long id) {
        Simulacao simulacao = service.buscarPorId(id);
        return SimulacaoMapper.toResponse(simulacao);
    }
}
