CREATE SEQUENCE publication_id_seq START 1 INCREMENT BY 50;

CREATE TABLE PUBLICATIONS
(
    id           BIGINT DEFAULT nextval('publication_id_seq') not null,
    body         TEXT NOT NULL,
    PRIMARY KEY (id),
    publisher_id BIGINT not null REFERENCES PERSONS (id)
);