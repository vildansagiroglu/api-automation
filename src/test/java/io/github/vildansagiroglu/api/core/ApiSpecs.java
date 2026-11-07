package io.github.vildansagiroglu.api.core;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Shared request specifications. Base URLs can be overridden from the command line,
 * e.g. {@code mvn test -Dbooking.baseUrl=http://localhost:3001}.
 */
public final class ApiSpecs {

    public static final String BOOKING_BASE_URL =
            System.getProperty("booking.baseUrl", "https://restful-booker.herokuapp.com");

    public static final String SWAGGER_BASE_URL =
            System.getProperty("swagger.baseUrl", "https://generator.swagger.io");

    static {
        // Print request and response only when an assertion fails, to keep CI logs readable
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private ApiSpecs() {
    }

    public static RequestSpecification booking() {
        return json(BOOKING_BASE_URL);
    }

    public static RequestSpecification swaggerGenerator() {
        return json(SWAGGER_BASE_URL);
    }

    private static RequestSpecification json(String baseUrl) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }
}
