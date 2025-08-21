CREATE TABLE member
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE meal
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(50)      NOT NULL,
    quantity   INT              NOT NULL,
    price      DECIMAL(10, 2)   NOT NULL,
    image_url  VARCHAR(255)     NOT NULL,
    created_at TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_meal_name UNIQUE (name)
);

CREATE TABLE cart_item
(
    id         BIGSERIAL PRIMARY KEY,
    member_id  BIGINT    NOT NULL REFERENCES member (id) ON DELETE CASCADE,
    meal_id    BIGINT    NOT NULL REFERENCES meal (id) ON DELETE CASCADE,
    quantity   INT       NOT NULL,
    added_at   TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_cart_member_meal UNIQUE (member_id, meal_id)
);

CREATE TABLE "order"
(
    id           BIGSERIAL PRIMARY KEY,
    status       VARCHAR(50) NOT NULL,
    total_amount DECIMAL(10, 2)      NOT NULL,
    member_id    BIGINT      NOT NULL REFERENCES member (id),
    created_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_item
(
    id          BIGSERIAL PRIMARY KEY,
    order_id    BIGINT           NOT NULL REFERENCES "order" (id) ON DELETE CASCADE,
    meal_id     BIGINT           NOT NULL REFERENCES meal (id),
    quantity    INT              NOT NULL,
    price       DECIMAL(10, 2)   NOT NULL,
    created_at TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payment
(
    id                       BIGSERIAL PRIMARY KEY,
    stripe_payment_intent_id VARCHAR(255) NOT NULL UNIQUE,
    amount                   DECIMAL(10, 2)       NOT NULL,
    currency                 VARCHAR(50)  NOT NULL,
    status                   VARCHAR(50)  NOT NULL,
    failure_code             VARCHAR(255),
    failure_message          VARCHAR(255),
    order_id                 BIGINT       NOT NULL REFERENCES "order" (id) ON DELETE CASCADE,
    created_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
