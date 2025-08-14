create index if not exists flight_id_idx ON bookings.ticket_flights USING btree (flight_id);
--drop index if exists flight_id_idx;