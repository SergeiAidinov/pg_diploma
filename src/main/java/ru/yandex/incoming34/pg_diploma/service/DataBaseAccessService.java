package ru.yandex.incoming34.pg_diploma.service;

import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.yandex.incoming34.pg_diploma.dto.MetaData;
import ru.yandex.incoming34.pg_diploma.dto.PassengerLoadFactor;
import ru.yandex.incoming34.pg_diploma.dto.PassengerLoadFactorQuery;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DataBaseAccessService {

    private final DataSource dataSource;
    private final CustomInMemoryCache customInMemoryCache;

    public DataBaseAccessService(ApplicationContext applicationContext, CustomInMemoryCache customInMemoryCache) {
        dataSource = (DataSource) applicationContext.getBean("dataSource");
        this.customInMemoryCache = customInMemoryCache;
    }

    public Integer getRangeByAircraftId(String aircraftId) {
        Integer distance = null;
        try (Connection connection = dataSource.getConnection()){
            CallableStatement callableStatement = connection.prepareCall("{call get_range_by_id(?)}");
            callableStatement.setString(1, aircraftId);
            ResultSet rs = callableStatement.executeQuery();
            if (rs.next()) distance = rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return distance;
    }

    public Map<Integer, Integer> quantityOfBookingsByQuantityOfPassengersInOneBooking() {
        Map<Integer, Integer> result = new HashMap<>();
        try (Connection connection = dataSource.getConnection()){
            CallableStatement callableStatement = connection.prepareCall("{call quantity_of_bookings_by_quantity_of_passengers_in_one_booking()}");
            ResultSet rs = callableStatement.executeQuery();

            while (rs.next()) {
                result.put(rs.getInt(1), rs.getInt(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<PassengerLoadFactor> passengerLoadFactor(PassengerLoadFactorQuery passengerLoadFactorQuery) {
        List<PassengerLoadFactor> passengerLoadFactors;
        try (Connection connection = dataSource.getConnection()) {
            CallableStatement callableStatement = connection.prepareCall("{call passenger_load_factor(?, ?)}");
            callableStatement.setBigDecimal(1, BigDecimal.valueOf(passengerLoadFactorQuery.getLoadFactorMin()));
            callableStatement.setBigDecimal(2, BigDecimal.valueOf(passengerLoadFactorQuery.getLoadFactorMax()));
            System.out.println("===> " + LocalDateTime.now());
            ResultSet rs = callableStatement.executeQuery();
            System.out.println("===> " + LocalDateTime.now());
            passengerLoadFactors = new ArrayList<>();
            while (rs.next()) {
                passengerLoadFactors.add(
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
        return passengerLoadFactors;
    }

    public List<PassengerLoadFactor> passengerLoadFactorOptimized(PassengerLoadFactorQuery passengerLoadFactorQuery) {
        List<PassengerLoadFactor> passengerLoadFactors = new ArrayList<>();
        ;
        try (Connection connection = dataSource.getConnection()) {
            CallableStatement callableStatement = connection.prepareCall("{call passenger_load_factor_optimized(?, ?)}");
            callableStatement.setBigDecimal(1, BigDecimal.valueOf(passengerLoadFactorQuery.getLoadFactorMin()));
            callableStatement.setBigDecimal(2, BigDecimal.valueOf(passengerLoadFactorQuery.getLoadFactorMax()));
            ResultSet rs = callableStatement.executeQuery();
            //passengerLoadFactors
            int order = 0;
            JSONObject jsonObjectMetaData;
            while (rs.next()) {
                if (Objects.nonNull(rs.getString(MetaData.METADATA_FIELD_NAME)))
                    jsonObjectMetaData = new JSONObject(rs.getString(MetaData.METADATA_FIELD_NAME));

                passengerLoadFactors.add(

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
        return passengerLoadFactors;
    }
}
