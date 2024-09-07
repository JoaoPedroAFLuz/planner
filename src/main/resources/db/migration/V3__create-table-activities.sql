CREATE TABLE activities
(
    id          SERIAL PRIMARY KEY,
    trip_id     INT          NOT NULL,
    code        UUID         NOT NULL UNIQUE,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    occurs_at   TIMESTAMP    NOT NULL,

    FOREIGN KEY (trip_id) REFERENCES trips (id) ON DELETE CASCADE
);