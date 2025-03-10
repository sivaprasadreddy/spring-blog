-- pwd is 'secret'
INSERT INTO USERS (id, email, password, name, role) VALUES
(1, 'siva@gmail.com', '$2a$10$N67CcMRogzY9hNicu/sSGubZnlMu.b0niQkLk/rT1i57xERkvdbj6', 'Siva Prasad', 'ROLE_ADMIN'),
(2, 'geovanny.mendoza@example.com', '$2a$10$N67CcMRogzY9hNicu/sSGubZnlMu.b0niQkLk/rT1i57xERkvdbj6', 'Geovanny Mendoza', 'ROLE_USER');

INSERT INTO CATEGORIES(id, name, slug) VALUES
(1, 'Java', 'java'),
(2, 'Python', 'python'),
(3, 'Node.js', 'nodejs'),
(4, 'Kotlin', 'kotlin'),
(5, 'DevOps', 'devops'),
(6, 'Go', 'go'),
(7, 'AI', 'ai'),
(8, 'Gen AI', 'gen-ai'),
(9, 'Rust', 'rust'),
(10, 'TypeScript', 'typescript')
;

INSERT INTO TAGS(id, name, slug) VALUES
(1, 'Java', 'java'),
(2, 'SpringBoot', 'spring-boot'),
(3, 'Quarkus', 'quarkus'),
(4, 'JUnit', 'junit'),
(5, 'Python', 'python'),
(6, 'Node.js', 'nodejs'),
(7, 'Kotlin', 'kotlin'),
(8, 'DevOps', 'devops'),
(9, 'Security', 'security'),
(10, 'AI', 'ai')
;
