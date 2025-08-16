create or replace function passenger_load_factor_optimized(
    p_load_factor_min decimal,
    p_load_factor_max decimal
)
    returns table
            (
                flight_id     int,
                aircraft_code char(3),
                passenger_lf  decimal,
                tickets_sold  smallint,
                totally_seats smallint


            )
    language plpgsql
as
$$
begin
return query
select
    cast (flights_load_factor.flight_id as int)
     , cast (flights.aircraft_code as char(3))
     , cast (flights_load_factor.load_factor as decimal)

     , cast (
        (select count(ticket_flights.ticket_no)
        from ticket_flights
        where ticket_flights.flight_id = flights_load_factor.flight_id)
         as smallint) as tickets_sold
     , cast (
        (select count(seats.seat_no)
        from seats
        where seats.aircraft_code = flights.aircraft_code
        group by seats.aircraft_code  ) as smallint) as seats_totally
from flights_load_factor
         join flights on flights_load_factor.flight_id = flights.flight_id
         join ticket_flights on ticket_flights.flight_id = flights.flight_id
         join seats on seats.aircraft_code = flights.aircraft_code
where flights_load_factor.load_factor between  p_load_factor_min and p_load_factor_max
group by flights_load_factor.flight_id, flights_load_factor.load_factor
       , flights.aircraft_code

;

end;
$$;