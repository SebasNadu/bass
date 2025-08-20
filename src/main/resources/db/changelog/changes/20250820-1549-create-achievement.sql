CREATE TABLE achievement
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL UNIQUE,
    image_url        VARCHAR(255),
    strikes_required INT          NOT NULL UNIQUE,
    coupon_type      VARCHAR(50),
    description      TEXT         NOT NULL,
    created_at       TIMESTAMP    NOT NULL,
    updated_at       TIMESTAMP    NOT NULL
);
