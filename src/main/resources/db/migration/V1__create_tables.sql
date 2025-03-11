CREATE SEQUENCE user_id_seq START 100 INCREMENT BY 50;

CREATE TABLE USERS
(
    id           BIGINT       NOT NULL DEFAULT nextval('user_id_seq'),
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    name         VARCHAR(100) NOT NULL,
    role         VARCHAR(50)  NOT NULL,
    created_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE SEQUENCE category_id_seq START 100 INCREMENT BY 50;

CREATE TABLE CATEGORIES
(
    id   BIGINT       NOT NULL DEFAULT nextval('category_id_seq'),
    name VARCHAR(255) NOT NULL UNIQUE,
    slug VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE SEQUENCE tag_id_seq START 100 INCREMENT BY 50;

CREATE TABLE TAGS
(
    id   BIGINT       NOT NULL DEFAULT nextval('tag_id_seq'),
    name VARCHAR(255) NOT NULL UNIQUE,
    slug VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE SEQUENCE post_id_seq START 100 INCREMENT BY 50;

CREATE TABLE POSTS
(
    id                BIGINT       NOT NULL DEFAULT nextval('post_id_seq'),
    title             VARCHAR(500) NOT NULL UNIQUE,
    slug              VARCHAR(500) NOT NULL UNIQUE,
    short_description VARCHAR(500) NOT NULL,
    content_markdown  TEXT         NOT NULL,
    content_html      TEXT         NOT NULL,
    status            VARCHAR(20)  NOT NULL CHECK (status IN ('DRAFT', 'PUBLISHED')),
    category_id       BIGINT       NOT NULL REFERENCES CATEGORIES (id),
    created_by        BIGINT       NOT NULL REFERENCES USERS (id),
    created_date      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by        BIGINT REFERENCES USERS (id),
    updated_date      TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE POST_TAGS
(
    post_id BIGINT NOT NULL REFERENCES POSTS (id),
    tag_id  BIGINT NOT NULL REFERENCES TAGS (id)
);

CREATE SEQUENCE comment_id_seq START 100 INCREMENT BY 50;

CREATE TABLE COMMENTS
(
    id           BIGINT       NOT NULL DEFAULT nextval('comment_id_seq'),
    content      TEXT         NOT NULL,
    post_id      BIGINT       NOT NULL REFERENCES POSTS (id),
    created_by   BIGINT       NOT NULL REFERENCES USERS (id),
    created_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP,
    PRIMARY KEY (id)
);
