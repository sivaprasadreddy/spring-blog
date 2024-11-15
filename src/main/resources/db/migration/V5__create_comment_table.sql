CREATE SEQUENCE comment_id_seq START 1 INCREMENT BY 50;

CREATE TABLE COMMENTS
(
    id           BIGINT       NOT NULL DEFAULT nextval('comment_id_seq'),
    name         VARCHAR(100) NOT NULL,
    content      TEXT         NOT NULL,
    post_id      BIGINT       NOT NULL REFERENCES POSTS (id),
    created_by   BIGINT       NOT NULL REFERENCES USERS (id),
    created_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);