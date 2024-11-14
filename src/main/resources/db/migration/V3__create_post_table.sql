CREATE SEQUENCE post_id_seq START 1 INCREMENT BY 50;

CREATE TABLE POSTS
(
    id                BIGINT                                                        DEFAULT nextval('post_id_seq') NOT NULL,
    title             VARCHAR(255) UNIQUE                                  NOT NULL,
    slug              VARCHAR(255) UNIQUE                                  NOT NULL,
    short_description VARCHAR(255) UNIQUE                                  NOT NULL,
    content_markdown  TEXT                                                 NOT NULL,
    content_html      TEXT                                                 NOT NULL,
    status            VARCHAR(20) CHECK (status IN ('DRAFT', 'PUBLISHED')) NOT NULL,
    created_by        BIGINT REFERENCES USERS (id)                         NOT NULL,
    created_date      TIMESTAMP                                            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by        BIGINT REFERENCES USERS (id),
    updated_date      TIMESTAMP,
    PRIMARY KEY (id)
);