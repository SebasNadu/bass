CREATE TABLE coupon
(
    id            BIGSERIAL PRIMARY KEY,
    member_id     BIGINT      NOT NULL REFERENCES member (id),
    name          VARCHAR(255) NOT NULL,
    discount_rate VARCHAR(50)  NOT NULL,
    created_at   TIMESTAMP   NOT NULL,
    updated_at   TIMESTAMP   NOT NULL,
    expires_at    TIMESTAMP    NOT NULL
);
