package ru.yandex.incoming34.pg_diploma.service;

import org.postgresql.util.PGobject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.yandex.incoming34.pg_diploma.dto.NewBookingQuery;
import ru.yandex.incoming34.pg_diploma.dto.PassengerWithTicket;

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
           /* PGobject p1 = new PGobject();
            p1.setType("passenger_and_ticket_price_type");
            String funcData = createPgComposite(newBookingQuery.getPassengerWithTicketList().get(0).getPassengerName(),
                    newBookingQuery.getPassengerWithTicketList().get(0).getPassengerId(),
                    newBookingQuery.getPassengerWithTicketList().get(0).getTicketPrice(),
                    newBookingQuery.getPassengerWithTicketList().get(0).getContactInfo()
            );
            System.out.println(funcData);
            p1.setValue(funcData);*/
            PGobject[] passengers = createPassengerWithTicketArray(newBookingQuery.getPassengerWithTicketList());
            java.sql.Array arr = connection.createArrayOf("passenger_and_ticket_price_type", passengers);
            callableStatement.setArray(3, arr);
            ResultSet rs = callableStatement.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private PGobject[] createPassengerWithTicketArray(List<PassengerWithTicket> passengerWithTicketList) throws SQLException {
        List<PGobject> passengers = new ArrayList<>();
        for (PassengerWithTicket passengerWithTicket : passengerWithTicketList) {
            PGobject pGobject = new PGobject();
            pGobject.setType("passenger_and_ticket_price_type");
            String funcData = createPgComposite(passengerWithTicket.getPassengerName(),
                    passengerWithTicket.getPassengerId(),
                    passengerWithTicket.getTicketPrice(),
                    passengerWithTicket.getContactInfo().toString()
            );
            System.out.println(funcData);
            pGobject.setValue(funcData);
            passengers.add(pGobject);
        }
        return Arrays.copyOf(passengers.toArray(), passengers.size(), PGobject[].class);
    }

    private  String createPgComposite(String name, String passengerId, BigDecimal ticketPrice, String contactInfo) {
        return String.format("(\\\"%s\\\", \\\"%s\\\", %s, \\\"%s\\\")",
                name.replace("\"", "\\\""),
                passengerId.replace("\"", "\\\""),
                ticketPrice,
                contactInfo.replace("\"", "\\\\\\\""));
    }
}
