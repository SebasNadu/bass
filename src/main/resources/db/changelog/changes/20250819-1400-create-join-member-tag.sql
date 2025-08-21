CREATE TABLE member_tag
(
    member_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    CONSTRAINT fk_member_tag_on_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    CONSTRAINT fk_member_tag_on_tag FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE,
    PRIMARY KEY (member_id, tag_id)
);