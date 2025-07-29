with tickets_sold as
         (
             select flight_id, count(ticket_flights.ticket_no) as ts from ticket_flights
             group by flight_id order by ticket_flights.flight_id
         )

select
    ticket_flights.flight_id
     , flights.aircraft_code
     , (select (count(seats.seat_no)) from seats where seats.aircraft_code =  flights.aircraft_code) as total_seats
     , (select ts from tickets_sold where flight_id = ticket_flights.flight_id ) as seats_sold
from ticket_flights
--where

         join flights on flights.flight_id = ticket_flights.flight_id
         join seats on flights.aircraft_code = seats.aircraft_code

group by ticket_flights.flight_id, flights.aircraft_code order by flight_id

;