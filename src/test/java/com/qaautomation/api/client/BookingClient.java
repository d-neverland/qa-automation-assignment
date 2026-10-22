package com.qaautomation.api.client;

import static io.restassured.RestAssured.given;

import com.qaautomation.api.dto.Booking;
import com.qaautomation.config.TestConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BookingClient {

  private static final String BOOKING_PATH = "/booking";
  private static final String BOOKING_BY_ID_PATH = BOOKING_PATH + "/{id}";

  private final RequestSpecification requestSpec = new RequestSpecBuilder()
      .setBaseUri(TestConfig.getRestfulBookerBaseUrl())
      .setContentType(ContentType.JSON)
      .setAccept("application/json")
      .addFilter(new AllureRestAssured())
      .build();

  public Response createBooking(Booking booking) {
    return given()
        .spec(requestSpec)
        .body(booking)
        .when()
        .post(BOOKING_PATH);
  }

  public Response getBooking(int bookingId) {
    return given()
        .spec(requestSpec)
        .pathParam("id", bookingId)
        .when()
        .get(BOOKING_BY_ID_PATH);
  }
}
