package com.qaautomation.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)
public record Booking(
    String firstName,
    String lastName,
    int totalPrice,
    boolean depositPaid,
    BookingDates bookingDates,
    String additionalNeeds
) {
  @JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)
  public record BookingDates(String checkIn, String checkOut) {
  }
}
