CREATE TABLE links
(
    id      SERIAL PRIMARY KEY,
    trip_id INT          NOT NULL,
    code    UUID         NOT NULL UNIQUE,
    title   VARCHAR(255) NOT NULL,
    url     VARCHAR(255) NOT NULL,

    FOREIGN KEY (trip_id) REFERENCES trips (id) ON DELETE CASCADE
)