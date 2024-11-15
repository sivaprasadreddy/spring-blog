CREATE SEQUENCE post_id_seq START 1 INCREMENT BY 50;

CREATE TABLE POSTS
(
    id                BIGINT       NOT NULL DEFAULT nextval('post_id_seq'),
    title             VARCHAR(255) NOT NULL UNIQUE,
    slug              VARCHAR(255) NOT NULL UNIQUE,
    short_description VARCHAR(255) NOT NULL,
    content_markdown  TEXT         NOT NULL,
    content_html      TEXT         NOT NULL,
    status            VARCHAR(20)  NOT NULL CHECK (status IN ('DRAFT', 'PUBLISHED')),
    created_by        BIGINT       NOT NULL REFERENCES USERS (id),
    created_date      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by        BIGINT REFERENCES USERS (id),
    updated_date      TIMESTAMP,
    PRIMARY KEY (id)
);