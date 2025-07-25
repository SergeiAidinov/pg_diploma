--liquibase formatted sql
--changeset sergei:ee0ca42d-9486-4383-bd1d-7a2ff8f493b0

CREATE TABLE IF NOT EXISTS users_table (
                                           id    BIGINT PRIMARY KEY,
    given_name    VARCHAR(25) NOT NULL,
    family_name    VARCHAR(25) NOT NULL,
    email  VARCHAR(35) NOT NULL,
    password    VARCHAR(64) NOT NULL,
    verbs TEXT

    );

--rollback DROP TABLE user;