create table flights_load_factor (
                                     flight_id bigint,
                                     load_factor decimal not null ,
                                     constraint flight_id_fk foreign key (flight_id) references flights (flight_id)
);