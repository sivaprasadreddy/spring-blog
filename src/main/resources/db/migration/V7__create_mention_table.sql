CREATE SEQUENCE mention_id_seq START 1 INCREMENT BY 50;

CREATE TABLE MENTIONS
(
    id             BIGINT DEFAULT nextval('mention_id_seq'),
    text           VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    publication_id BIGINT NOT NULL REFERENCES PUBLICATIONS (id)

);