CREATE SEQUENCE comment_id_seq START 1 INCREMENT BY 50;

CREATE TABLE COMMENTS
(
    id           BIGINT                   DEFAULT nextval('comment_id_seq') not null,
    name         VARCHAR(100)                 NOT NULL,
    content      TEXT                         NOT NULL,
    post_id      BIGINT REFERENCES POSTS (id) NOT NULL,
    created_by   BIGINT REFERENCES USERS (id) NOT NULL,
    created_date TIMESTAMP                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);