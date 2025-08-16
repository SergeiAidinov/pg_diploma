create or replace function passenger_load_factor(
    p_load_factor_min decimal,
    p_load_factor_max decimal
)
    returns table
            (
                flight_id     bigint,
                aircraft_code varchar(3),
                totally_seats smallint,
                tickets_sold  smallint,
                passenger_lf decimal,
                metadata     json
            )
    language plpgsql
as
$$
declare
    start_time     timestamp;
    end_time       timestamp;
    execution_time interval;
    metadata       json;
begin
    select into start_time clock_timestamp();
    raise notice 'Выборка данных началась: %', start_time;
    return query
        with totally_seats as (select seats.aircraft_code, count(seats.seat_no) as seats_totally
                              from seats
                              group by seats.aircraft_code)
           , data_from_db as (select ticket_flights.flight_id
                                   , count(ticket_flights.ticket_no)                            as tickets_sold
                                   , flights.aircraft_code                                      as aircraft_code
                                   , (select seats_totally
                                      from totally_seats
                                      where flights.aircraft_code = totally_seats.aircraft_code) as seats_totally
                              from ticket_flights
                                       join flights on flights.flight_id = ticket_flights.flight_id
                              group by ticket_flights.flight_id, flights.aircraft_code)
           , calculations as (select data_from_db.flight_id::bigint
                                   , data_from_db.aircraft_code::varchar(3)
                                   , cast(data_from_db.seats_totally as smallint)                      as totally_seats
                                   , cast(data_from_db.tickets_sold as smallint)
                                   , (select round(((cast(data_from_db.tickets_sold as decimal)) /
                                                    (cast(data_from_db.seats_totally as decimal))),
                                                   2))                                                 as passenger_load_factor
                                   , (select null::json)
                              from data_from_db)

        select flight_id, aircraft_code, totally_seats, tickets_sold, passenger_lf, metadata
        from calculations
        where passenger_load_factor between p_load_factor_min and p_load_factor_max;

    select into end_time clock_timestamp();
    select into execution_time extract(epoch from end_time) - extract(epoch from start_time);
    raise notice 'Выборка данных заняла: %', execution_time;
    select into metadata json_build_object(
                                 'start_time', start_time,
                                 'end_time', end_time,
                                 'function_name', 'demo.bookings.passenger_load_factor',
                                 'execution_time', execution_time
                         );
     return query select null::bigint,
                         null::varchar(3),
                         null::smallint,
                         null::smallint,
                         null::decimal,
                         metadata;
end;
$$;

--rollback drop function passenger_load_factor(decimal, decimal);
