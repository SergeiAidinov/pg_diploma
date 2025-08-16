create index if not exists flights_load_factor_idx ON flights_load_factor USING btree (load_factor);
--drop index if exists flights_load_factor_idx;