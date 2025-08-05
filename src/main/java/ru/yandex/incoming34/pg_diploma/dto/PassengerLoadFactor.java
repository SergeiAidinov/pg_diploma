package ru.yandex.incoming34.pg_diploma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PassengerLoadFactor {
    private final int fligthId;
    private final String aircraftCode;
    private final short seatsTotally;
    private final short ticketsSold;
    private final double loadFactor;
}
