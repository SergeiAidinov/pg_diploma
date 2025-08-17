create or replace function next_ticket_number(p_last_ticket text)
    returns text
    language plpgsql as
$$
declare
    chars            text := '0123456789abcdefghijklmnopqrstuvwxyz';
    numeral_system   int  = length(chars);
    len              int;
    v_max_identifier text;
    result           text;
    len_max          int  := 13; -- фиксированная длина билета
    i                int;
    carry            boolean;
    idx_in_chars     int;
    letter           text;
    next_letter      character;
begin

    raise notice 'NUMERAL SYSTEM:, %', numeral_system;
    --v_max_identifier := (select ticket_no from tickets order by ticket_no desc limit 1);
    v_max_identifier := p_last_ticket;
    -- Если билетов ещё нет, начинаем с '0000000000001'
    if v_max_identifier is null then
        return '0000000000001';
    end if;

    -- Если длина последнего билета меньше len, дополняем слева нулем
    if length(v_max_identifier) < len_max then
        v_max_identifier := '0' || v_max_identifier;
        return v_max_identifier;
    end if;

    result := v_max_identifier;
    carry := true;
    len = length(v_max_identifier);
    -- идём с конца строки
    for radix in reverse len..1
        loop
            raise notice 'RADIX: %', radix;
            letter := substr(v_max_identifier, radix, 1)::text;
            raise notice 'LETTER: %', letter;
            idx_in_chars := position(letter in chars);
            raise notice 'IDX: %', idx_in_chars;

            --ищем первый символ, который можно увеличить, и увеличиваем его
            if (radix = len_max and idx_in_chars < numeral_system) then
                raise notice 'увеличиваем последний символ';
                next_letter = substr(chars, idx_in_chars + 1, 1);
                raise notice 'NEXT_ LETTER: %', next_letter;
                select into result overlay(v_max_identifier placing next_letter from radix for 1);
                /* select into result left(result, len_max - 1);
                 select into result rpad(result, len_max, '0');*/
                raise notice 'RESULT: %', result;
                exit;
            end if;
                --если символ не является последним, то увеличиваем его, а следующие за ним символы обнуляем
                if (radix < len_max) then
                    raise notice 'увеличиваем символ в середине строки';
                    next_letter = substr(chars, idx_in_chars + 1, 1);
                    raise notice 'NEXT_ LETTER: %', next_letter;
                    select into result overlay(v_max_identifier placing next_letter from radix for 1);
                    select into result left(result, radix);
                    raise notice 'left(result, radix - 1) % ', result;
                    select into result rpad(result, len_max, '0');
                    exit;
                end if;
        end loop;
    return result;
end;
$$;
