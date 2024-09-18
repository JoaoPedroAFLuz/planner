CREATE TABLE activities
(
    id          SERIAL PRIMARY KEY,
    code        UUID         NOT NULL UNIQUE,
    trip_id     INT          NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    occurs_at   TIMESTAMP    NOT NULL,

    FOREIGN KEY (trip_id) REFERENCES trips (id) ON DELETE CASCADE
);