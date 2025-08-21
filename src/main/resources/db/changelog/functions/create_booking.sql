create or replace function create_booking(
    p_flight_id bigint, p_book_ref varchar(6),
    arr passenger_and_ticket_price_type[]) returns boolean
    language plpgsql as
$$
declare
    elem passenger_and_ticket_price_type;
    v_total_amount int = 0;
begin
    raise notice 'STATUS: %' , (select status from flights where flight_id = p_flight_id);
    if (select status from flights where flight_id = p_flight_id) <> 'Scheduled' then
        raise exception 'Flight status has to be Scheduled';
    end if;
    if exists(select * from bookings where bookings.book_ref = p_book_ref) then
        raise exception 'Booking already exists';
    end if;
    insert into bookings (book_ref, book_date, total_amount) values (p_book_ref, (select current_timestamp), 1.00);

    foreach elem in array arr
        loop
            RAISE NOTICE 'Name: %', elem.name;
            RAISE NOTICE 'Sum: %', elem.price;
            RAISE NOTICE 'Info: %', elem.contact_info;
            if not exists(select passenger_id from tickets where passenger_id = elem.passenger_id) then
                RAISE NOTICE 'In cycle';
                v_total_amount = v_total_amount + elem.price;
                insert into tickets (book_ref, passenger_id, passenger_name, contact_data)
                values ( p_book_ref
                       , elem.passenger_id
                       , elem.name
                       , elem.contact_info);
            end if;
        end loop;
    update bookings set total_amount = v_total_amount where book_ref = p_book_ref;
    return true;
end;
$$