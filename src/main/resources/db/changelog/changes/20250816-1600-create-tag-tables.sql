CREATE TABLE tag
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    created_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tag_meal
(
    meal_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    CONSTRAINT fk_tag_meal_on_meal FOREIGN KEY (meal_id) REFERENCES meal (id) ON DELETE CASCADE,
    CONSTRAINT fk_tag_meal_on_tag FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE,
    PRIMARY KEY (meal_id, tag_id)
);

CREATE TABLE member_tag
(
    member_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    CONSTRAINT fk_member_tag_on_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    CONSTRAINT fk_member_tag_on_tag FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE,
    PRIMARY KEY (member_id, tag_id)
);
