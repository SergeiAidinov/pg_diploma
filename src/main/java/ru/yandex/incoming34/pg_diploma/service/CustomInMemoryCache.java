package ru.yandex.incoming34.pg_diploma.service;

import lombok.Getter;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.yandex.incoming34.pg_diploma.dto.LoadFactorsWithMetaData;
import ru.yandex.incoming34.pg_diploma.dto.MetaData;
import ru.yandex.incoming34.pg_diploma.dto.PassengerLoadFactor;
import ru.yandex.incoming34.pg_diploma.dto.PassengerLoadFactorQuery;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
@Getter
public class CustomInMemoryCache {

    private final DataSource dataSource;
    private PassengerLoadFactorQuery currentQuery;
    private MetaData currentMetaData;
    private String lastCalledFunctionName;
    private final TreeMap<Integer, PassengerLoadFactor> cache = new TreeMap<>();

    public CustomInMemoryCache(ApplicationContext applicationContext) {
        dataSource = (DataSource) applicationContext.getBean("dataSource");
    }



    private boolean hasSameSearchCriteriaAndFunctionName(PassengerLoadFactorQuery newQuery, String functionName) {
        if (Objects.isNull(currentQuery)) return false;
        return Objects.equals(currentQuery.getLoadFactorMin(), newQuery.getLoadFactorMin())
                && Objects.equals(currentQuery.getLoadFactorMax(), newQuery.getLoadFactorMax())
                && Objects.equals(lastCalledFunctionName, functionName);
    }

    public synchronized LoadFactorsWithMetaData getDataFromCacheOrCallLoadFactorFunction(PassengerLoadFactorQuery passengerLoadFactorQuery, String functionName) {
        if (!hasSameSearchCriteriaAndFunctionName(passengerLoadFactorQuery, functionName)) refreshCache(passengerLoadFactorQuery, functionName);
        List<PassengerLoadFactor> loadFactors = new ArrayList<>(cache.subMap(
                MetaData.PAGE_SIZE * (passengerLoadFactorQuery.getPageNumber() - 1),
                true,
                MetaData.PAGE_SIZE * (passengerLoadFactorQuery.getPageNumber() - 1) + MetaData.PAGE_SIZE - 1,
                true).values());
        return new LoadFactorsWithMetaData(currentMetaData, loadFactors);
    }

    private void refreshCache(PassengerLoadFactorQuery passengerLoadFactorQuery, String functionName){
        cache.clear();
        currentQuery = passengerLoadFactorQuery;
        lastCalledFunctionName = functionName;
        JSONObject jsonObjectMetaData = null;
        int order = 0;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "{call " +
                    functionName +
                    "(?, ?)}";
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setBigDecimal(1, BigDecimal.valueOf(passengerLoadFactorQuery.getLoadFactorMin()));
            callableStatement.setBigDecimal(2, BigDecimal.valueOf(passengerLoadFactorQuery.getLoadFactorMax()));
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                if (Objects.nonNull(rs.getString(MetaData.METADATA_FIELD_NAME)))
                    jsonObjectMetaData = new JSONObject(rs.getString(MetaData.METADATA_FIELD_NAME));
                cache.put(++order,
                        new PassengerLoadFactor(rs.getInt("flight_id"),
                                rs.getString("aircraft_code"),
                                rs.getShort("totally_seats"),
                                rs.getShort("tickets_sold"),
                                rs.getDouble("passenger_lf")
                        ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        currentMetaData = new MetaData(jsonObjectMetaData, (long) order);
    }

}
