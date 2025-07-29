create or replace function quantity_of_bookings_by_quantity_of_passengers_in_one_booking()
    returns table
            (
                passengers_in_one_booking bigint,
                quantity_of_bookings      bigint
            )
    language plpgsql
as
$$
begin
    return query select ticket_qty, count(br) from (select book_ref as br, count(ticket_no) as ticket_qty from tickets group by book_ref) as q
                 group by ticket_qty order by ticket_qty;
end;
$$;