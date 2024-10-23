CREATE TABLE users
(
    id         SERIAL PRIMARY KEY ,
    first_name VARCHAR(55),
    last_name  VARCHAR(55),
    email      VARCHAR(55),
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6)
);