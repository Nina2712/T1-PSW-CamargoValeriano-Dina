package com.pruebas.bdd.steps;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class InscripcionStepDefinitions {

    // DATOS DEL ALUMNO
    private static final String ALUMNO = "Dina Ynocencia Camargo Valeriano";
    private static final String CODIGO = "i202502356";

    private String codigoEstudiante;
    private Response respuesta;

    @Before
    public void configurar() {
        System.out.println("Alumno: " + ALUMNO + " Código: " + CODIGO);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Given("un estudiante nuevo que no está inscrito en el curso {int}")
    public void unEstudianteNuevo(int cursoId) {
        // código distinto en cada ejecución
        codigoEstudiante = "I" + System.currentTimeMillis();
    }

    @Given("el estudiante {string} ya está inscrito en el curso {int}")
    public void elEstudianteYaEstaInscrito(String codigo, int cursoId) {
        // I2024001 ya está inscrito en el curso 1 (cargado por DataLoader)
        codigoEstudiante = codigo;
    }

    @When("solicita su inscripción en el curso {int}")
    public void solicitaSuInscripcion(int cursoId) {
        String body = """
                {"codigoEstudiante": "%s", "cursoId": %d}
                """.formatted(codigoEstudiante, cursoId);

        respuesta = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/inscripciones");
    }

    @Then("el sistema responde con código {int}")
    public void elSistemaRespondeConCodigo(int codigo) {
        assertEquals(codigo, respuesta.statusCode());
    }

    @Then("el sistema rechaza la inscripción")
    public void elSistemaRechazaLaInscripcion() {
        assertTrue(respuesta.statusCode() >= 400,
                "Se esperaba un error 4xx/5xx, pero llegó " + respuesta.statusCode());
    }
}
