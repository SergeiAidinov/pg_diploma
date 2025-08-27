create or replace trigger ticket_no_trigger
    before insert
    on tickets
    for each row
execute function ticket_no();
