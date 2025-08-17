create type passenger_and_ticket_price_type as
(
    name  text,
    passenger_id varchar(20),
    price int,
    contact_info jsonb
);