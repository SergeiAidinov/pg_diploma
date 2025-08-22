package ru.yandex.incoming34.pg_diploma.dto;

import io.swagger.v3.core.util.Json;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONObject;

import java.math.BigDecimal;
@AllArgsConstructor
@Getter
public class PassengerWithTicket {

    private final String passengerName;
    private final String passengerId;
    private final BigDecimal ticketPrice;
    private final String contactInfo;
}
