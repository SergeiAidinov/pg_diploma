package ru.yandex.incoming34.pg_diploma.service;

import lombok.Getter;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.yandex.incoming34.pg_diploma.dto.MetaData;
import ru.yandex.incoming34.pg_diploma.dto.PassengerLoadFactor;
import ru.yandex.incoming34.pg_diploma.dto.PassengerLoadFactorQuery;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@Service
@Getter
public class CustomInMemoryCache {

    private PassengerLoadFactorQuery currentQuery;
    private TreeMap<Integer, PassengerLoadFactor> cache = new TreeMap<>();

    public synchronized boolean hasSameSearchCriteria(PassengerLoadFactorQuery newQuery) {
        return Objects.equals(currentQuery.getLoadFactorMin(), newQuery.getLoadFactorMin())
                && Objects.equals(currentQuery.getLoadFactorMax(), newQuery.getLoadFactorMax());
        /*return passengerLoadFactorQuery.getLoadFactorMin().equals(newQuery.getLoadFactorMin()) &&
                passengerLoadFactorQuery.getLoadFactorMax().equals(newQuery.getLoadFactorMax());*/
    }

    public synchronized boolean refreshCache(PassengerLoadFactorQuery passengerLoadFactorQuery, Map<Integer, PassengerLoadFactor> cache){
        return true;
    }

    public synchronized Pair<MetaData, List<PassengerLoadFactor>> getPage(PassengerLoadFactorQuery passengerLoadFactorQuery){
        return null;
    }
}
