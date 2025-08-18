create or replace function next_ticket_number(p_last_ticket text)
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
    len_max          int  = 13; -- фиксированная длина билета
    i                int;
    carry            boolean;
    idx_in_chars     int;
    current_symbol          character;
    next_letter      character;
begin
    v_max_identifier = p_last_ticket;
    -- Если билетов ещё нет, начинаем с '0000000000001'
    if v_max_identifier is null then
        return '0000000000001';
    end if;
    -- Если длина последнего билета меньше len, дополняем слева нулями
    if length(v_max_identifier) < len_max then
        select into v_max_identifier lpad(v_max_identifier, len_max, '0');
    end if;

    result = v_max_identifier;
    carry = true;
    len = length(v_max_identifier);
    for radix in reverse len..1
        loop
            current_symbol = substr(v_max_identifier, radix, 1)::text;
            if (current_symbol <> last_symbol_in_sequence) then
                idx_in_chars = position(current_symbol in char_sequence);
                next_letter = substr(char_sequence, idx_in_chars + 1, 1);
                select into result overlay(v_max_identifier placing next_letter from radix for 1);
                select into result substr(result, 1, radix);
                select into result rpad(result, len_max, '0');
            exit;
            end if;
        end loop;
    return result;
end;
$$;
