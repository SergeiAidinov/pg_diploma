create or replace function ticket_no()
    returns trigger
    language plpgsql
as
$$
begin
    if new.ticket_no is null then
        new.ticket_no := custom_sequence_generator_tickets_id();
    end if;
    return new;
end;
$$;
