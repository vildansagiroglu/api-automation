package io.github.vildansagiroglu.api.booking;

import io.github.vildansagiroglu.api.core.ApiSpecs;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Small helper layer so that tests describe behaviour instead of HTTP plumbing.
 */
public final class BookingClient {

    public static final String ADMIN_USER = System.getProperty("booking.user", "admin");
    public static final String ADMIN_PASSWORD = System.getProperty("booking.password", "password123");

    private BookingClient() {
    }

    /** Creates a booking and returns its id. */
    public static int createBooking(Map<String, Object> booking) {
        return given()
                .spec(ApiSpecs.booking())
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");
    }

    /** Returns a valid auth token for update and delete calls. */
    public static String token() {
        return given()
                .spec(ApiSpecs.booking())
                .body(BookingData.credentials(ADMIN_USER, ADMIN_PASSWORD))
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
}
