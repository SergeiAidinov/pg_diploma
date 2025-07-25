--liquibase formatted sql
--changeset sergei:ee0ca42d-9486-4383-bd1d-7a2ff8f493b0

create or replace function get_range_by_id (p_id text) returns text as
$$
declare
    res text;
begin
    select into res range from aircrafts_data where aircraft_code = p_id;
    return res;
end
$$ language 'plpgsql';

--rollback drop function get_range_by_id (p_id text);