CREATE SEQUENCE publication_follower_id_seq START 1 INCREMENT BY 50;

CREATE TABLE PUBLICATION_FOLLOWER
(
    id           BIGINT DEFAULT nextval('publication_follower_id_seq'),
    date         timestamp,
    PRIMARY KEY (id),
    publisher_id BIGINT REFERENCES PERSONS (id),
    follower_id  BIGINT REFERENCES PERSONS (id)
);