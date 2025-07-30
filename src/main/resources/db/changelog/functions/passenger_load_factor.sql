create or replace function passenger_load_factor(
    p_load_factor_min decimal,
    p_load_factor_max decimal
)
    returns table
            (
                flight_id     int,
                aircraft_code char(3),
                totaly_seats  smallint,
                tickets_sold  smallint,
                passenger_lf  decimal
            )
    language plpgsql
as
$$
    --declare data_table record;
begin
    return query
        with totaly_seats as (select seats.aircraft_code, count(seats.seat_no) as seats_totaly
                              from seats
                              group by seats.aircraft_code)
           , data_from_db as (select ticket_flights.flight_id
                                   , count(ticket_flights.ticket_no)                            as tickets_sold
                                   , flights.aircraft_code                                      as aircraft_code
                                   , (select seats_totaly
                                      from totaly_seats
                                      where flights.aircraft_code = totaly_seats.aircraft_code) as seats_totaly
                              --, tickets_sold   as fraction
                              from ticket_flights
                                       join flights on flights.flight_id = ticket_flights.flight_id
                              --join seats on seats.aircraft_code = flights.aircraft_code
                              group by ticket_flights.flight_id, flights.aircraft_code)
           , calculations as (select data_from_db.flight_id
                                   , data_from_db.aircraft_code
                                   , cast(data_from_db.seats_totaly as smallint)                       as totaly_seats
                                   , cast(data_from_db.tickets_sold as smallint)
                                   , (select round(((cast(data_from_db.tickets_sold as decimal)) /
                                                    (cast(data_from_db.seats_totaly as decimal))),
                                                   2))                                                 as passenger_load_factor
                              from data_from_db)
        select *
        from calculations
        where passenger_load_factor between p_load_factor_min and p_load_factor_max;
end;
$$;
