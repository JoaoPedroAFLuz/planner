CREATE TABLE trips
(
    id           SERIAL PRIMARY KEY,
    code         UUID         NOT NULL UNIQUE,
    owner_id     INT          NOT NULL,
    destination  VARCHAR(255) NOT NULL,
    starts_at    TIMESTAMP    NOT NULL,
    ends_at      TIMESTAMP    NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    confirmed_at TIMESTAMP,

    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);