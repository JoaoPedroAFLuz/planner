CREATE TABLE trips
(
    id           SERIAL PRIMARY KEY,
    code         UUID         NOT NULL UNIQUE,
    owner_name   VARCHAR(255) NOT NULL,
    owner_email  VARCHAR(255) NOT NULL,
    destination  VARCHAR(255) NOT NULL,
    starts_at    TIMESTAMP    NOT NULL,
    ends_at      TIMESTAMP    NOT NULL,
    confirmed_at TIMESTAMP
);