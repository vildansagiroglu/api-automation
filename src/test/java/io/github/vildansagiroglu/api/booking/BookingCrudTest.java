package io.github.vildansagiroglu.api.booking;

import io.github.vildansagiroglu.api.core.ApiSpecs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@DisplayName("Booking API - happy path")
class BookingCrudTest {

    @Test
    @DisplayName("POST /booking creates a booking and echoes the payload")
    void createBooking() {
        Map<String, Object> booking = BookingData.validBooking();

        given()
                .spec(ApiSpecs.booking())
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/create-booking-response.json"))
                .body("bookingid", greaterThan(0))
                .body("booking.firstname", equalTo(booking.get("firstname")))
                .body("booking.lastname", equalTo(booking.get("lastname")))
                .body("booking.totalprice", equalTo(450))
                .body("booking.depositpaid", equalTo(true));
    }

    @Test
    @DisplayName("GET /booking/{id} returns the stored booking")
    void getBookingById() {
        Map<String, Object> booking = BookingData.validBooking();
        int id = BookingClient.createBooking(booking);

        given()
                .spec(ApiSpecs.booking())
                .when()
                .get("/booking/{id}", id)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking.json"))
                .body("firstname", equalTo(booking.get("firstname")))
                .body("bookingdates.checkin", notNullValue());
    }

    @Test
    @DisplayName("GET /booking?firstname= filters bookings by name")
    void filterBookingsByName() {
        Map<String, Object> booking = BookingData.validBooking();
        int id = BookingClient.createBooking(booking);

        given()
                .spec(ApiSpecs.booking())
                .queryParam("firstname", booking.get("firstname"))
                .queryParam("lastname", booking.get("lastname"))
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", hasItem(id));
    }

    @Test
    @DisplayName("PUT /booking/{id} with a token replaces the booking")
    void updateBooking() {
        int id = BookingClient.createBooking(BookingData.validBooking());
        Map<String, Object> updated = BookingData.validBooking();
        updated.put("totalprice", 999);
        updated.put("additionalneeds", "Late checkout");

        given()
                .spec(ApiSpecs.booking())
                .cookie("token", BookingClient.token())
                .body(updated)
                .when()
                .put("/booking/{id}", id)
                .then()
                .statusCode(200)
                .body("firstname", equalTo(updated.get("firstname")))
                .body("totalprice", equalTo(999))
                .body("additionalneeds", equalTo("Late checkout"));
    }

    @Test
    @DisplayName("PATCH /booking/{id} updates only the given fields")
    void partialUpdateBooking() {
        Map<String, Object> original = BookingData.validBooking();
        int id = BookingClient.createBooking(original);

        given()
                .spec(ApiSpecs.booking())
                .cookie("token", BookingClient.token())
                .body(Map.of("totalprice", 120))
                .when()
                .patch("/booking/{id}", id)
                .then()
                .statusCode(200)
                .body("totalprice", equalTo(120))
                .body("firstname", equalTo(original.get("firstname")));
    }

    @Test
    @DisplayName("DELETE /booking/{id} removes the booking")
    void deleteBooking() {
        int id = BookingClient.createBooking(BookingData.validBooking());

        given()
                .spec(ApiSpecs.booking())
                .cookie("token", BookingClient.token())
                .when()
                .delete("/booking/{id}", id)
                .then()
                // Restful-Booker answers a successful delete with 201
                .statusCode(201);

        given()
                .spec(ApiSpecs.booking())
                .when()
                .get("/booking/{id}", id)
                .then()
                .statusCode(404);
    }
}
