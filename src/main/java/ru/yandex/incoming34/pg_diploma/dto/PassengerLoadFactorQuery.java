package ru.yandex.incoming34.pg_diploma.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerLoadFactorQuery {
    private double LoadFactorMin;
    private double LoadFactorMax;
}
