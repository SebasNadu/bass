CREATE TABLE achievement
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL UNIQUE,
    image_url        VARCHAR(255),
    streaks_required INT          NOT NULL UNIQUE,
    coupon_type      VARCHAR(50),
    description      TEXT         NOT NULL,
    created_at       TIMESTAMP    NOT NULL,
    updated_at       TIMESTAMP    NOT NULL
);

CREATE TABLE member_achievement
(
    member_id      BIGINT NOT NULL,
    achievement_id BIGINT NOT NULL,
    PRIMARY KEY (member_id, achievement_id),
    CONSTRAINT fk_member_achievement_member
        FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    CONSTRAINT fk_member_achievement_achievement
        FOREIGN KEY (achievement_id) REFERENCES achievement (id) ON DELETE CASCADE
);

-- Indexes for faster lookup
CREATE INDEX idx_member_achievement_member_id
    ON member_achievement (member_id);

CREATE INDEX idx_member_achievement_achievement_id
    ON member_achievement (achievement_id);
