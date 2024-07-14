CREATE TABLE participants
(
    id           SERIAL PRIMARY KEY,
    trip_id      INT          NOT NULL,
    code         UUID         NOT NULL UNIQUE,
    name         VARCHAR(255),
    email        VARCHAR(255) NOT NULL,
    confirmed_at TIMESTAMP,

    FOREIGN KEY (trip_id) REFERENCES trips (id) ON DELETE CASCADE
);