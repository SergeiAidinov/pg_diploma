create or replace function fill_table_flights_load_factor() returns void
    language plpgsql as

$$
declare
    f_id                  bigint;
    totally_seats         smallint;
    tickets_sold          smallint;
    passenger_load_factor decimal;
    var_aircraft_code  varchar(3);
    --aircraft_quantity_seats record;
    r                     record;


begin
    drop table if exists aircraft_quantity_seats;

    create temp table aircraft_quantity_seats
    (
        aircraft_code  varchar,
        seats_quantity smallint
    );


    for r in select distinct aircraft_code  from seats loop
            --raise notice 'aircraft_code: %', r.aircraft_code;
            select into totally_seats count(seats.seat_no) from seats where aircraft_code = r.aircraft_code;
            insert into aircraft_quantity_seats (aircraft_code, seats_quantity) values (r.aircraft_code, totally_seats);
        end loop;


    for r in select aircraft_quantity_seats.aircraft_code, aircraft_quantity_seats.seats_quantity from aircraft_quantity_seats loop

            raise notice 'CODE: %, SEATS: %', r.aircraft_code, r.seats_quantity;

        end loop;

    /*drop table if exists flight_id_tickets_sold;

    create temp table flight_id_tickets_sold
    (
        aircraft_code  varchar,
        tickets_sold smallint
    );*/




    for r in select flight_id from bookings.flights loop
            select into tickets_sold /*ticket_flights.flight_id,*/ count(ticket_flights.ticket_no) from ticket_flights
            where ticket_flights.flight_id = r.flight_id;
            select flights.aircraft_code into var_aircraft_code  from flights where flights.flight_id = r.flight_id;
            select aircraft_quantity_seats.seats_quantity into totally_seats from aircraft_quantity_seats where var_aircraft_code = aircraft_quantity_seats.aircraft_code;
            select into passenger_load_factor round(((cast(tickets_sold as decimal)) /
                                                     (cast(totally_seats as decimal))),
                                                    2);
            raise notice 'f_id: %, tickets_sold: %, aircraft_code: %, passenger_load_factor: % ', r.flight_id, tickets_sold, var_aircraft_code, passenger_load_factor;
            -- select into tickets_sold 0;
            insert into flights_load_factor ( aircraft_code
                                            , flight_id
                                            , load_factor
                                            , seats_sold
                                            , seats_totally)
            values ( var_aircraft_code
                   , r.flight_id
                   , passenger_load_factor
                   , tickets_sold
                   , totally_seats );
        end loop;
end;

$$;

--select fill_table_flights_load_factor();

--create index flight_id_idx on ticket_flights (flight_id);

