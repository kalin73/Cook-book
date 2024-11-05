CREATE TABLE users
(
    id         VARCHAR(255) PRIMARY KEY,
    email      VARCHAR(55) UNIQUE NOT NULL,
    first_name VARCHAR(55),
    last_name  VARCHAR(55),
    password   VARCHAR(255)       NOT NULL,
    created_at TIMESTAMP(6)
);

CREATE TABLE recipes
(
    id          VARCHAR(255) PRIMARY KEY,
    user_id     VARCHAR(255),
    title       VARCHAR(55) NOT NULL,
    preparation TEXT,
    image_url   VARCHAR(255),
    created_at  TIMESTAMP(6),
    CONSTRAINT pk_users_id
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE ingredients
(
    id        VARCHAR(255) PRIMARY KEY,
    recipe_id VARCHAR(255),
    name      VARCHAR(55) NOT NULL,
    quantity  VARCHAR(55) NOT NULL,
    CONSTRAINT fk_recipes_id
        FOREIGN KEY (recipe_id) REFERENCES recipes (id)

);