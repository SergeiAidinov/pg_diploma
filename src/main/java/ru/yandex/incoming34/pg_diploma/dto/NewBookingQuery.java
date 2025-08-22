package ru.yandex.incoming34.pg_diploma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@AllArgsConstructor
@Getter
public class NewBookingQuery {

    private final Integer flightId;
    private final String bookingReference;
    private final List<PassengerWithTicket> passengerWithTicketList;
}
