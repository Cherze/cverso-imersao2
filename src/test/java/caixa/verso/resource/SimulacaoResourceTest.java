package caixa.verso.resource;

import caixa.verso.dto.SimulacaoResponse;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class SimulacaoResourceTest {

    @Test
    void testCriarEConsultarSimulacao() {
        // Requisição válida
        String body = """
            {
                "valorInicial": 1000.00,
                "taxaJurosMensal": 1.5,
                "prazoMeses": 12
            }
            """;

        Response postResponse = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(201)
                .extract().response();

        SimulacaoResponse simulacao = postResponse.as(SimulacaoResponse.class);
        assertNotNull(simulacao.id());
        assertEquals(12, simulacao.parcelas().size());

        // Consulta por ID
        given()
                .pathParam("id", simulacao.id())
                .when()
                .get("/simulacoes/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(simulacao.id().intValue()));
    }

    @Test
    void testValidacaoRetorna400() {
        String bodyInvalido = """
            { "valorInicial": -10, "taxaJurosMensal": 1.5, "prazoMeses": 0 }
            """;
        given()
                .contentType(ContentType.JSON)
                .body(bodyInvalido)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(400);
    }

    @Test
    void testConsultaComIdInexistenteRetorna404() {
        given()
                .pathParam("id", 999)
                .when()
                .get("/simulacoes/{id}")
                .then()
                .statusCode(404);
    }
    @Test
    void testTaxaNegativaRetorna400() {
        String body = """
        {
            "valorInicial": 1000.00,
            "taxaJurosMensal": -1.0,
            "prazoMeses": 12
        }
        """;
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(400);
    }
}
