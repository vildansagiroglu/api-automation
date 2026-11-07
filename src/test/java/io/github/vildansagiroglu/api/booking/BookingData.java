package io.github.vildansagiroglu.api.booking;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Test data builder for booking payloads. Every call returns unique names,
 * so tests never depend on data created by other tests.
 */
public final class BookingData {

    private BookingData() {
    }

    public static Map<String, Object> validBooking() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        LocalDate checkin = LocalDate.now().plusDays(10);

        Map<String, Object> dates = new LinkedHashMap<>();
        dates.put("checkin", checkin.toString());
        dates.put("checkout", checkin.plusDays(3).toString());

        Map<String, Object> booking = new LinkedHashMap<>();
        booking.put("firstname", "Test" + suffix);
        booking.put("lastname", "User" + suffix);
        booking.put("totalprice", 450);
        booking.put("depositpaid", true);
        booking.put("bookingdates", dates);
        booking.put("additionalneeds", "Breakfast");
        return booking;
    }

    public static Map<String, Object> credentials(String username, String password) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("username", username);
        body.put("password", password);
        return body;
    }
}
