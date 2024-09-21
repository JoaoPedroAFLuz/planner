CREATE TABLE refresh_tokens
(
    id      SERIAL PRIMARY KEY,
    user_id INT          NOT NULL,
    token   VARCHAR(255) NOT NULL,

    CONSTRAINT idx_refresh_tokens_token UNIQUE (token),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
)