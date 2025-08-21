CREATE TABLE day_of_week
(
     id           BIGSERIAL PRIMARY KEY,
     day_name     VARCHAR(20) NOT NULL,
     member_id    BIGINT      NOT NULL REFERENCES member (id),
     created_at   TIMESTAMP   NOT NULL,
     updated_at   TIMESTAMP   NOT NULL
);
