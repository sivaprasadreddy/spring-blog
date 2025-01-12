truncate table users CASCADE;

INSERT INTO USERS (email, password, name, role)
VALUES
    ('siva@gmail.com', '$2a$10$N67CcMRogzY9hNicu/sSGubZnlMu.b0niQkLk/rT1i57xERkvdbj6', 'Siva Prasad', 'ADMIN'),
    ('geovanny.mendoza@example.com', '$2a$10$N67CcMRogzY9hNicu/sSGubZnlMu.b0niQkLk/rT1i57xERkvdbj6', 'Geovanny Mendoza', 'USER');
