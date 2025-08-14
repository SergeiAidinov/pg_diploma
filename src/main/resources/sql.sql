select flights_load_factor.flight_id
     , flights_load_factor.load_factor
     , flights.aircraft_code
     , (select count(ticket_flights.ticket_no)
        from ticket_flights
        where ticket_flights.flight_id = flights_load_factor.flight_id) as tickets_sold
     , (select count(seats.seat_no)
        from seats
        where seats.aircraft_code = flights.aircraft_code
        group by seats.aircraft_code  ) as seats_totally
from flights_load_factor
         join flights on flights_load_factor.flight_id = flights.flight_id
         join ticket_flights on ticket_flights.flight_id = flights.flight_id
         join seats on seats.aircraft_code = flights.aircraft_code
where flights_load_factor.load_factor between 0.6 and 0.7
group by flights_load_factor.flight_id, flights_load_factor.load_factor
       , flights.aircraft_code

;