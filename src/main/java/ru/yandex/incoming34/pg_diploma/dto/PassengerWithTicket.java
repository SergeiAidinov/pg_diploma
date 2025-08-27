package ru.yandex.incoming34.pg_diploma.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
@AllArgsConstructor
@Getter
public class PassengerWithTicket {

    private final String passengerName;
    private final String passengerId;
    private final BigDecimal ticketPrice;
    private final JsonNode contactInfo;
}
