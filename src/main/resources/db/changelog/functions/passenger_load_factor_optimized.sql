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
                passenger_lf decimal,
                exec_time    timestamp with time zone
            )
    language plpgsql

as
$$
/*declare
    start_time     time with time zone;
    end_time       time with time zone;
    execution_time interval ;*/
begin
    /*select into start_time date_trunc('milliseconds', NOW());
        raise notice 'Выборка данных началась: %', start_time;*/
    return query select NULL::bigint,
                        null::varchar(3),
                        null::smallint,
                        null::smallint,
                        null::decimal,
                        now()::timestamp with time zone;
    return query
        select flights_load_factor.flight_id
               , flights_load_factor.aircraft_code
               , flights_load_factor.seats_totally /*as totally_seats*/
               , flights_load_factor.seats_sold /*as tickets_sold*/
               , flights_load_factor.load_factor
               , NULL::timestamp with time zone
        from flights_load_factor where load_factor between p_load_factor_min and p_load_factor_max;
    --select into execution_time date_trunc('milliseconds', (NOW() - start_time));
    --raise notice 'Выборка данных заняла: %', execution_time;
    --select into end_time date_trunc('milliseconds', NOW());
    --return query select NULL::bigint, null::varchar(3), null::smallint, null::smallint, null::decimal, end_time - start_time;
    return query select NULL::bigint,
                        null::varchar(3),
                        null::smallint,
                        null::smallint,
                        null::decimal,
                        now()::timestamp with time zone;

end;
$$;

--drop function passenger_load_factor_optimized(numeric, numeric);