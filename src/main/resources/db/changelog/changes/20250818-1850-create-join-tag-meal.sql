CREATE TABLE tag_meal
(
    meal_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    CONSTRAINT fk_tag_meal_on_meal FOREIGN KEY (meal_id) REFERENCES meal (id) ON DELETE CASCADE,
    CONSTRAINT fk_tag_meal_on_tag FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE,
    PRIMARY KEY (meal_id, tag_id)
);
