create or replace function passenger_load_factor_optimized(
    p_load_factor_min decimal,
    p_load_factor_max decimal
)
    returns table
            (
                flight_id     bigint,
                aircraft_code varchar(3),
                totally_seats smallint,
                tickets_sold  smallint,
                passenger_lf  decimal


            )
    language plpgsql
as
$$
    declare start_time timestamp with time zone;
begin
        select into start_time now();
        raise notice 'Выборка данных началась: %', start_time;
    return query
        select flights_load_factor.flight_id
               , flights_load_factor.aircraft_code
               , flights_load_factor.seats_totally /*as totally_seats*/
               , flights_load_factor.seats_sold /*as tickets_sold*/
               , flights_load_factor.load_factor
        from flights_load_factor where load_factor between p_load_factor_min and p_load_factor_max;
        raise notice 'Выборка данных заняла: %', (start_time - now());
end;
$$;

--drop function passenger_load_factor_optimized(numeric, numeric);