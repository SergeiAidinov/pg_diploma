create or replace function fill_table_flights_load_factor() returns void
    language plpgsql as

$$
declare
    var_totally_seats         smallint;
    var_tickets_sold          smallint;
    var_passenger_load_factor decimal;
    var_aircraft_code  varchar(3);
    rec                     record;
begin
    drop table if exists aircraft_quantity_seats;
    create temp table aircraft_quantity_seats
    (
        aircraft_code  varchar,
        seats_quantity smallint
    ) on commit drop;

    for rec in select distinct aircraft_code  from seats loop
            select into var_totally_seats count(seats.seat_no) from seats where aircraft_code = rec.aircraft_code;
            insert into aircraft_quantity_seats (aircraft_code, seats_quantity) values (rec.aircraft_code, var_totally_seats);
        end loop;

    /*for rec in select aircraft_quantity_seats.aircraft_code, aircraft_quantity_seats.seats_quantity from aircraft_quantity_seats loop

            raise notice 'CODE: %, SEATS: %', rec.aircraft_code, rec.seats_quantity;

        end loop;*/

    /*drop table if exists flight_id_tickets_sold;

    create temp table flight_id_tickets_sold
    (
        aircraft_code  varchar,
        tickets_sold smallint
    );*/




    for rec in select flight_id from bookings.flights loop
            select into var_tickets_sold /*ticket_flights.flight_id,*/ count(ticket_flights.ticket_no) from ticket_flights
            where ticket_flights.flight_id = rec.flight_id;
            select flights.aircraft_code into var_aircraft_code  from flights where flights.flight_id = rec.flight_id;
            select aircraft_quantity_seats.seats_quantity into var_totally_seats from aircraft_quantity_seats where var_aircraft_code = aircraft_quantity_seats.aircraft_code;
            select into var_passenger_load_factor round(((cast(var_tickets_sold as decimal)) /
                                                     (cast(var_totally_seats as decimal))),
                                                    2);
            --raise notice 'f_id: %, tickets_sold: %, aircraft_code: %, passenger_load_factor: % ', rec.flight_id, var_tickets_sold, var_aircraft_code, var_passenger_load_factor;
            insert into flights_load_factor ( aircraft_code
                                            , flight_id
                                            , load_factor
                                            , seats_sold
                                            , seats_totally)
            values ( var_aircraft_code
                   , rec.flight_id
                   , var_passenger_load_factor
                   , var_tickets_sold
                   , var_totally_seats );
        end loop;
    drop table if exists aircraft_quantity_seats;
end;
$$;

--drop function if exists fill_table_flights_load_factor();

