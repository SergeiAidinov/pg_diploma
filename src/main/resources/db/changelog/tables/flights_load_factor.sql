create table flights_load_factor (
                                     flight_id     bigint,
                                     aircraft_code varchar(3),
                                     seats_totally smallint,
                                     seats_sold    smallint,
                                     load_factor   decimal not null,
                                     constraint flight_id_fk foreign key (flight_id) references flights (flight_id)
);

--drop table if exists flights_load_factor;