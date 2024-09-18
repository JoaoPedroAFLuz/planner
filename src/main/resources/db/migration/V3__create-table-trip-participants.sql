CREATE TABLE trip_participants
(
    id           SERIAL PRIMARY KEY,
    user_id      INT NOT NULL,
    trip_id      INT NOT NULL,
    confirmed_at TIMESTAMP,

    FOREIGN KEY (trip_id) REFERENCES trips (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
