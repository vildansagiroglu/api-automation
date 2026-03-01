package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class SwaggerApiTest {

    static String BASE_URL = "https://generator.swagger.io";
    static String selectedLanguage;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    void testGetClients() {

        Response response =
                given()
                        .when()
                        .get("/api/gen/clients")
                        .then()
                        .extract().response();

        // HTTP Status kontrolü
        assertEquals(200, response.getStatusCode(),
                "Status code 200 değil!");

        // Response'dan bir değer al ve değişkene ata
        selectedLanguage = response.jsonPath().getString("[0]");
        System.out.println("Seçilen language: " + selectedLanguage);

        assertNotNull(selectedLanguage);
    }

    @Test
    void testPostGenerateClient() {

        // Önce GET çalışmalı
        testGetClients();

        String requestBody = """
        {
          "spec": {
            "swagger": "2.0",
            "info": {
              "description": "Test API",
              "version": "1.0.0",
              "title": "Test"
            },
            "host": "petstore.swagger.io",
            "basePath": "/v2",
            "schemes": ["https"],
            "paths": {}
          }
        }
        """;

        Response response =
                given()
                        .contentType("application/json")
                        .body(requestBody)
                        .when()
                        .post("/api/gen/clients/" + selectedLanguage)
                        .then()
                        .extract().response();

        // HTTP Status kontrolü
        assertEquals(200, response.getStatusCode(),
                "POST isteği başarısız!");

        System.out.println("POST başarılı!");
    }
}