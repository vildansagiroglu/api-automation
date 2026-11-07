package io.github.vildansagiroglu.api.swagger;

import io.github.vildansagiroglu.api.core.ApiSpecs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Swagger Codegen API")
class SwaggerGeneratorTest {

    private static final String MINIMAL_SPEC = """
            {
              "spec": {
                "swagger": "2.0",
                "info": { "description": "Test API", "version": "1.0.0", "title": "Test" },
                "host": "petstore.swagger.io",
                "basePath": "/v2",
                "schemes": ["https"],
                "paths": {}
              }
            }
            """;

    @Test
    @DisplayName("GET /api/gen/clients lists the supported client languages")
    void listClientLanguages() {
        List<String> clients = given()
                .spec(ApiSpecs.swaggerGenerator())
                .when()
                .get("/api/gen/clients")
                .then()
                .statusCode(200)
                .body("$", hasItem("java"))
                .extract()
                .jsonPath()
                .getList("$", String.class);

        assertFalse(clients.isEmpty(), "Client language list should not be empty");
    }

    @Test
    @DisplayName("POST /api/gen/clients/{language} returns a download link for the generated SDK")
    void generateJavaClient() {
        given()
                .spec(ApiSpecs.swaggerGenerator())
                .body(MINIMAL_SPEC)
                .when()
                .post("/api/gen/clients/{language}", "java")
                .then()
                .statusCode(200)
                .body("code", not(emptyOrNullString()))
                .body("link", startsWith("http"));
    }
}
