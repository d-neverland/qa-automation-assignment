package com.qaautomation.api.tests;

import static org.testng.Assert.assertTrue;

import com.qaautomation.api.client.BookingClient;
import com.qaautomation.api.dto.Booking;
import com.qaautomation.api.dto.Booking.BookingDates;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class BookingApiTest {

  private static final int NON_EXISTENT_BOOKING_ID = 999_999_999;

  private final BookingClient bookingClient = new BookingClient();

  @Test(description = "Created booking can be retrieved by ID with the same data")
  public void shouldCreateAndRetrieveBooking() {
    Booking expectedBooking = new Booking(
        "John",
        "Smith",
        150,
        true,
        new BookingDates("2026-11-01", "2026-11-05"),
        "Breakfast"
    );

    int bookingId = bookingClient.createBooking(expectedBooking)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .extract()
        .path("bookingid");
    assertTrue(bookingId > 0, "Created booking should get a positive ID, but was " + bookingId);

    Booking actualBooking = bookingClient.getBooking(bookingId)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .extract()
        .as(Booking.class);

    SoftAssert softAssert = new SoftAssert();
    softAssert.assertEquals(actualBooking.firstName(), expectedBooking.firstName(), "First name");
    softAssert.assertEquals(actualBooking.lastName(), expectedBooking.lastName(), "Last name");
    softAssert.assertEquals(actualBooking.totalPrice(), expectedBooking.totalPrice(), "Total price");
    softAssert.assertEquals(actualBooking.depositPaid(), expectedBooking.depositPaid(), "Deposit paid");
    softAssert.assertEquals(actualBooking.bookingDates(), expectedBooking.bookingDates(), "Booking dates");
    softAssert.assertEquals(actualBooking.additionalNeeds(), expectedBooking.additionalNeeds(), "Additional needs");
    softAssert.assertAll();
  }

  @Test(description = "Requesting a booking that doesn't exist returns 404")
  public void shouldReturn404ForUnknownBooking() {
    bookingClient.getBooking(NON_EXISTENT_BOOKING_ID)
        .then()
        .statusCode(404);
  }
}
