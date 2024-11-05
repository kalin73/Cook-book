CREATE TABLE users
(
    id         VARCHAR(255) PRIMARY KEY,
    email      VARCHAR(55) UNIQUE NOT NULL,
    first_name VARCHAR(55),
    last_name  VARCHAR(55),
    password   VARCHAR(255)       NOT NULL,
    created_at TIMESTAMP(6)
);