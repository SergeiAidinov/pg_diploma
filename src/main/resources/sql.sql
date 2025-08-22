select create_booking(2880, 'EDUTTR',
                      Array [
                          row('Sergei Aidinov', '7002 842823', 12000, '{"phone": "+70131833325"}')::passenger_and_ticket_price_type
                          ]
       );

CREATE TABLE orders (
                        ticket_no      char(13)    not null
                            primary key,
                        description text
);

CREATE or replace TRIGGER orders_set_id_trg
    BEFORE INSERT ON tickets
    FOR EACH ROW
EXECUTE FUNCTION orders_set_id();


CREATE OR REPLACE FUNCTION orders_set_id()
    RETURNS trigger
    LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.ticket_no IS NULL THEN
        NEW.ticket_no := custom_sequence_generator_tickets_id();
    END IF;
    RETURN NEW;
END;
$$;

INSERT INTO tickets(book_ref, passenger_id, passenger_name, contact_data)
VALUES ('EDUTTR', '7002 843823', 'Sergei Aidinov', '{}');
--INSERT INTO orders(description) VALUES ('Второй заказ');

select * from tickets order by ticket_no desc limit 7;

{
    "flightId": 2880,
    "bookingReference": "EDUTTR",
    "passengerWithTicketList": [
        {
            "passengerName": "Sergei Aidinov",
            "passengerId": "7002 842823",
            "ticketPrice": 12000,
            "contactInfo": {
                "phone": "+79168132746"
            }
        },
        {
            "passengerName": "James Arthur Gosling",
            "passengerId": "12345 QQ",
            "ticketPrice": 12000,
            "contactInfo": {
                "phone": "+19573219753"
            }
        },
        {
            "passengerName": "Richard Matthew Stallman",
            "passengerId": "1603 1953g",
            "ticketPrice": 1400,
            "contactInfo": {
                "phone": "+1593218653075"
            }
        }
    ]
}