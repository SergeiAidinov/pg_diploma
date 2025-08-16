package ru.yandex.incoming34.pg_diploma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@AllArgsConstructor
@Getter
public class LoadFactorsWithMetaData {

    private final MetaData metaData;
    private final List<PassengerLoadFactor> loadFactors;
}
