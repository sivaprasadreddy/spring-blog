CREATE SEQUENCE person_id_seq START 1 INCREMENT BY 50;

CREATE TABLE PERSONS
(
    id         BIGINT DEFAULT nextval('person_id_seq'),
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    gender     VARCHAR(10),
    country    VARCHAR(50),
    address    VARCHAR(100),
    PRIMARY KEY (id)
);