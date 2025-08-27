create or replace function custom_sequence_generator_tickets_id()
    returns text
    language plpgsql as
$$
declare
    char_sequence           text = '0123456789abcdefghijklmnopqrstuvwxyz';
    numeral_system          int  = length(char_sequence);
    last_symbol_in_sequence char = substr(char_sequence, numeral_system, 1);
    len              int;
    v_max_identifier text;
    result           text;
    digits_in_ticket_id int = 13;
    idx_in_chars     int;
    current_symbol   character;
    next_symbol      character;
begin
    select into v_max_identifier tickets.ticket_no from tickets order by ticket_no desc limit 1;
    -- Если билетов ещё нет, начинаем с '0000000000001'
    if v_max_identifier is null then
        return '0000000000001';
    end if;
    -- Если длина последнего билета меньше len, дополняем слева нулями
    if length(v_max_identifier) < digits_in_ticket_id then
        select into v_max_identifier lpad(v_max_identifier, digits_in_ticket_id, '0');
    end if;

    result = v_max_identifier;
    len = length(v_max_identifier);
    for radix in reverse len..1
        loop
            current_symbol = substr(v_max_identifier, radix, 1)::text;
            if (current_symbol <> last_symbol_in_sequence) then
                idx_in_chars = position(current_symbol in char_sequence);
                next_symbol = substr(char_sequence, idx_in_chars + 1, 1);
                select into result overlay(v_max_identifier placing next_symbol from radix for 1);
                select into result substr(result, 1, radix);
                select into result rpad(result, digits_in_ticket_id, '0');
            exit;
            end if;
        end loop;
    return result;
end;
$$;
