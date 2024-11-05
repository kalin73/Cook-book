CREATE TABLE login_logs
(
    id      VARCHAR(255),
    user_id VARCHAR(255),
    date    TIMESTAMP(6),
    CONSTRAINT fk_users_user_id
        FOREIGN KEY (user_id) REFERENCES users (id)
);