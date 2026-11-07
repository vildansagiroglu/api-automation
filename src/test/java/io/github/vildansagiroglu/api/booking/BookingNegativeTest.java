package io.github.vildansagiroglu.api.booking;

import io.github.vildansagiroglu.api.core.ApiSpecs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Booking API - negative and security checks")
class BookingNegativeTest {

    @Test
    @DisplayName("POST /auth with wrong password returns no token")
    void authWithBadCredentials() {
        given()
                .spec(ApiSpecs.booking())
                .body(BookingData.credentials(BookingClient.ADMIN_USER, "wrong-password"))
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("token", nullValue())
                .body("reason", equalTo("Bad credentials"));
    }

    @Test
    @DisplayName("PUT without a token is rejected with 403")
    void updateWithoutToken() {
        int id = BookingClient.createBooking(BookingData.validBooking());

        given()
                .spec(ApiSpecs.booking())
                .body(BookingData.validBooking())
                .when()
                .put("/booking/{id}", id)
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("DELETE with an invalid token is rejected with 403")
    void deleteWithInvalidToken() {
        int id = BookingClient.createBooking(BookingData.validBooking());

        given()
                .spec(ApiSpecs.booking())
                .cookie("token", "invalid-token")
                .when()
                .delete("/booking/{id}", id)
                .then()
                .statusCode(403);

        // The booking must still exist after the rejected delete
        given()
                .spec(ApiSpecs.booking())
                .when()
                .get("/booking/{id}", id)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("GET a booking id that does not exist returns 404")
    void getUnknownBooking() {
        given()
                .spec(ApiSpecs.booking())
                .when()
                .get("/booking/{id}", 999_999_999)
                .then()
                .statusCode(404);
    }
}
