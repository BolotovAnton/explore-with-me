CREATE TABLE IF NOT EXISTS stats
(
    id        int NOT NULL GENERATED ALWAYS AS IDENTITY,
    app       character varying(200)      NOT NULL,
    uri       character varying(200)      NOT NULL,
    ip        character varying(100)      NOT NULL,
    timestamp timestamp WITHOUT TIME ZONE NOT NULL
);