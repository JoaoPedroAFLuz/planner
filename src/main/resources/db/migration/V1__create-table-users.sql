CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    code       UUID         NOT NULL UNIQUE,
    name       VARCHAR(255),
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255),
    created_at TIMESTAMP    NOT NULL
);
