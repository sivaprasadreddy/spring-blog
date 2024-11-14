CREATE SEQUENCE tag_id_seq START 1 INCREMENT BY 50;

CREATE TABLE TAGS(
                     id BIGINT DEFAULT nextval('tag_id_seq'),
                     text VARCHAR(50) NOT NULL,
                     PRIMARY KEY (id),
                     publication_id BIGINT REFERENCES PUBLICATIONS(id)
);