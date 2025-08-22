package ru.yandex.incoming34.pg_diploma.service;

import org.postgresql.util.PGobject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.yandex.incoming34.pg_diploma.dto.NewBookingQuery;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class DataBaseAccessService {

    private final DataSource dataSource;

    public DataBaseAccessService(ApplicationContext applicationContext) {
        dataSource = (DataSource) applicationContext.getBean("dataSource");
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

    public boolean newBooking(NewBookingQuery newBookingQuery) {
        try (Connection connection = dataSource.getConnection()){
            CallableStatement callableStatement = connection.prepareCall("{call create_booking(?, ?, ?)}");
            callableStatement.setLong(1, newBookingQuery.getFlightId());
            callableStatement.setString(2, newBookingQuery.getBookingReference());
            PGobject passenger = new PGobject();
            passenger.setType("passenger_and_ticket_price_type");
            //passenger.setValue("(12345, 678.90)");
            PGobject p1 = new PGobject();
            p1.setType("passenger_and_ticket_price_type");
            //p1.setValue("(1, 100.50)");
           /* String passengerName = "\\\"" + newBookingQuery.getPassengerWithTicketList().get(0).getPassengerName() + "\\\"";
            String passengerId = "\\\"" + newBookingQuery.getPassengerWithTicketList().get(0).getPassengerId()+ "\\\"";
            String contactInfo = "{\\" + newBookingQuery.getPassengerWithTicketList().get(0).getContactInfo() + "\"}";*/
            String funcData = createPgComposite(newBookingQuery.getPassengerWithTicketList().get(0).getPassengerName(),
                    newBookingQuery.getPassengerWithTicketList().get(0).getPassengerId(),
                    newBookingQuery.getPassengerWithTicketList().get(0).getTicketPrice(),
                    newBookingQuery.getPassengerWithTicketList().get(0).getContactInfo()
            );
            System.out.println(funcData);
            //p1.setValue("(\"Sergei Aidinov\", \"7002 842823\", 12000, \"{\\\"phone\\\": \\\"+79168132746\\\"}\")");
            p1.setValue(funcData);
            PGobject[] passengers = new PGobject[] { p1};
            java.sql.Array arr = connection.createArrayOf("passenger_and_ticket_price_type", passengers);
            callableStatement.setArray(3, arr);
            ResultSet rs = callableStatement.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private  String createPgComposite(String name, String passport, BigDecimal amount, String json) {
        // Экранируем двойные кавычки внутри строк
        String escapedName = name.replace("\"", "\\\"");
        String escapedPassport = passport.replace("\"", "\\\"");
        String escapedJson = json.replace("\"", "\\\\\\\"");

        // Формируем строку с нужными кавычками для Postgres
        return String.format("(\\\"%s\\\", \\\"%s\\\", %s, \\\"%s\\\")",
                escapedName, escapedPassport, amount, escapedJson);
    }
}
