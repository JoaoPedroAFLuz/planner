CREATE TABLE links
(
    id          SERIAL PRIMARY KEY,
    activity_id INT          NOT NULL,
    code        UUID         NOT NULL UNIQUE,
    title       VARCHAR(255) NOT NULL,
    url         VARCHAR(255) NOT NULL,

    FOREIGN KEY (activity_id) REFERENCES activities (id) ON DELETE CASCADE
)