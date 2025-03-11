DELETE FROM post_tags;
DELETE FROM comments;
DELETE FROM posts;
DELETE FROM tags;
DELETE FROM categories;
DELETE FROM users;

INSERT INTO USERS (id, email, password, name, role)
VALUES (1, 'siva@gmail.com', '$2a$10$N67CcMRogzY9hNicu/sSGubZnlMu.b0niQkLk/rT1i57xERkvdbj6', 'Siva Prasad',
        'ROLE_ADMIN'),
       (2, 'geovanny.mendoza@example.com', '$2a$10$N67CcMRogzY9hNicu/sSGubZnlMu.b0niQkLk/rT1i57xERkvdbj6',
        'Geovanny Mendoza', 'ROLE_USER');

INSERT INTO CATEGORIES(id, name, slug)
VALUES (1, 'Java', 'java'),
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

INSERT INTO TAGS(id, name, slug)
VALUES (1, 'Java', 'java'),
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

INSERT INTO POSTS (id, title, slug, short_description, content_markdown, content_html, category_id, status, created_by)
VALUES (1, 'First Post', 'first-post', 'This is the first post', '# Markdown content', '<p>HTML content</p>', 1,
        'DRAFT', 1),
       (2, 'Second Post', 'second-post', 'This is the second post', '# Markdown content 2', '<p>HTML content 2</p>', 2,
        'PUBLISHED', 2),
       (3, 'Third Post', 'third-post', 'This is the third post', '# Markdown content 3', '<p>HTML content 3</p>', 3,
        'DRAFT', 1),
       (4, 'Fourth Post', 'fourth-post', 'This is the fourth post', '# Markdown content 4', '<p>HTML content 4</p>', 2,
        'PUBLISHED', 2),
       (5, 'Fifth Post', 'fifth-post', 'This is the fifth post', '# Markdown content 5', '<p>HTML content 5</p>', 4,
        'DRAFT', 1),
       (6, 'Sixth Post', 'sixth-post', 'This is the sixth post', '# Markdown content 6', '<p>HTML content 6</p>', 1,
        'PUBLISHED', 2),
       (7, 'Seventh Post', 'seventh-post', 'This is the seventh post', '# Markdown content 7', '<p>HTML content 7</p>',
        1, 'DRAFT', 1),
       (8, 'Eighth Post', 'eighth-post', 'This is the eighth post', '# Markdown content 8', '<p>HTML content 8</p>', 5,
        'PUBLISHED', 2),
       (9, 'Ninth Post', 'ninth-post', 'This is the ninth post', '# Markdown content 9', '<p>HTML content 9</p>', 5,
        'DRAFT', 2),
       (10, 'Tenth Post', 'tenth-post', 'This is the tenth post', '# Markdown content 10', '<p>HTML content 10</p>', 4,
        'PUBLISHED', 1),
       (11, 'Eleventh Post', 'eleventh-post', 'This is the eleventh post', '# Markdown content 11',
        '<p>HTML content 11</p>', 3, 'DRAFT', 2),
       (12, 'Twelfth Post', 'twelfth-post', 'This is the twelfth post', '# Markdown content 12',
        '<p>HTML content 12</p>', 6, 'PUBLISHED', 1),
       (13, 'Thirteenth Post', 'thirteenth-post', 'This is the thirteenth post', '# Markdown content 13',
        '<p>HTML content 13</p>', 7, 'DRAFT', 2),
       (14, 'Fourteenth Post', 'fourteenth-post', 'This is the fourteenth post', '# Markdown content 14',
        '<p>HTML content 14</p>', 3, 'PUBLISHED', 1),
       (15, 'Fifteenth Post', 'fifteenth-post', 'This is the fifteenth post', '# Markdown content 15',
        '<p>HTML content 15</p>', 2, 'DRAFT', 1),
       (16, 'Sixteenth Post', 'sixteenth-post', 'This is the sixteenth post', '# Markdown content 16',
        '<p>HTML content 16</p>', 1, 'PUBLISHED', 2),
       (17, 'Seventeenth Post', 'seventeenth-post', 'This is the seventeenth post', '# Markdown content 17',
        '<p>HTML content 17</p>', 8, 'DRAFT', 1),
       (18, 'Eighteenth Post', 'eighteenth-post', 'This is the eighteenth post', '# Markdown content 18',
        '<p>HTML content 18</p>', 9, 'PUBLISHED', 2),
       (19, 'Nineteenth Post', 'nineteenth-post', 'This is the nineteenth post', '# Markdown content 19',
        '<p>HTML content 19</p>', 1, 'DRAFT', 1),
       (20, 'Twentieth Post', 'twentieth-post', 'This is the twentieth post', '# Markdown content 20',
        '<p>HTML content 20</p>', 1, 'PUBLISHED', 2),
       (21, 'Twenty-first Post', 'twenty-first-post', 'This is the twenty-first post', '# Markdown content 21',
        '<p>HTML content 21</p>', 2, 'DRAFT', 1),
       (22, 'Twenty-second Post', 'twenty-second-post', 'This is the twenty-second post', '# Markdown content 22',
        '<p>HTML content 22</p>', 3, 'PUBLISHED', 1);

INSERT INTO post_tags (post_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 1),
       (3, 3);

INSERT INTO COMMENTS (id, content, post_id, created_by)
VALUES (1, 'This is a comment on the first post.', 1, 2),
       (2, 'This is another comment on the first post.', 1, 1);
