CREATE TABLE recipes
(
    id          SERIAL PRIMARY KEY,
    user_id     BIGINT,
    title       VARCHAR(55) NOT NULL,
    preparation TEXT,
    image_url   VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE ingredients
(
    id        SERIAL PRIMARY KEY,
    recipe_id BIGINT,
    name      VARCHAR(55) NOT NULL,
    quantity  VARCHAR(55) NOT NULL,
    FOREIGN KEY (recipe_id) REFERENCES recipes (id)
);