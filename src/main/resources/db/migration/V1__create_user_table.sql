CREATE SEQUENCE user_id_seq START 1 INCREMENT BY 50;

CREATE TABLE USERS
(
    id       BIGINT DEFAULT nextval('user_id_seq') not null,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    name     VARCHAR(100)        NOT NULL,
    role     VARCHAR(50)         NOT NULL,
    PRIMARY KEY (id)
);