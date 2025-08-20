CREATE TABLE coupon
(
    id             BIGSERIAL PRIMARY KEY,
    code           VARCHAR(255) NOT NULL UNIQUE,
    member_id      BIGINT       NOT NULL REFERENCES member (id),
    achievement_id BIGINT       NOT NULL REFERENCES achievement (id),
    coupon_type    VARCHAR(50)  NOT NULL,
    expires_at     TIMESTAMP    NOT NULL,
    is_used        BOOLEAN      NOT NULL DEFAULT FALSE,
    used_at        TIMESTAMP,
    created_at     TIMESTAMP    NOT NULL,
    updated_at     TIMESTAMP    NOT NULL
);
