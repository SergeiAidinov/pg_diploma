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