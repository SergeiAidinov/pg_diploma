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
                metadata json
            )
    language plpgsql

as
$$
declare
    start_time     timestamp;
    end_time       timestamp;
    execution_time interval;
    metadata json;
begin
    select into start_time clock_timestamp();
        raise notice 'Выборка данных началась: %', start_time;
    /*return query select NULL::bigint,
                        null::varchar(3),
                        null::smallint,
                        null::smallint,
                        null::decimal,
                        now()::timestamp with time zone;*/
    return query
        select flights_load_factor.flight_id
               , flights_load_factor.aircraft_code
               , flights_load_factor.seats_totally /*as totally_seats*/
               , flights_load_factor.seats_sold /*as tickets_sold*/
               , flights_load_factor.load_factor
               , NULL::json
        from flights_load_factor where load_factor between p_load_factor_min and p_load_factor_max;
    select into end_time clock_timestamp();
    raise notice 'Выборка данных закончилась: %', end_time;
    /*select into execution_time
        extract(epoch from (t1 - t2)) * 1000 as diff_millis
    from (
             values (
                        end_time::timetz,
                        start_time::timetz
                    )
         ) as v(t1, t2);*/
    select into execution_time extract(epoch from end_time) - extract(epoch from start_time) ;
    raise notice 'Выборка данных заняла: %', execution_time;


    select into metadata json_build_object(
            'start_time', start_time,
            'end_time', end_time,
        'function_name', 'passenger_load_factor_optimized(numeric, numeric)',
    'execution_time', execution_time
    );


    --select into end_time date_trunc('milliseconds', NOW());
    --return query select NULL::bigint, null::varchar(3), null::smallint, null::smallint, null::decimal, end_time - start_time;
    return query select NULL::bigint,
                        null::varchar(3),
                        null::smallint,
                        null::smallint,
                        null::decimal,
                        metadata;

end;
$$;

--drop function passenger_load_factor_optimized(numeric, numeric);