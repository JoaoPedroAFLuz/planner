CREATE TABLE links
(
    id          SERIAL PRIMARY KEY,
    code        UUID         NOT NULL UNIQUE,
    activity_id INT          NOT NULL,
    title       VARCHAR(255) NOT NULL,
    url         VARCHAR(255) NOT NULL,

    FOREIGN KEY (activity_id) REFERENCES activities (id) ON DELETE CASCADE
)