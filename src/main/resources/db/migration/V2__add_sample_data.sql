-- pwd is 'secret'
INSERT INTO USERS (id, email, password, name, role)
VALUES
    (1, 'siva@gmail.com', '$2a$10$N67CcMRogzY9hNicu/sSGubZnlMu.b0niQkLk/rT1i57xERkvdbj6', 'Siva Prasad', 'ROLE_ADMIN'),
    (2, 'geovanny.mendoza@example.com', '$2a$10$N67CcMRogzY9hNicu/sSGubZnlMu.b0niQkLk/rT1i57xERkvdbj6', 'Geovanny Mendoza', 'ROLE_USER');

INSERT INTO POSTS (id, title, slug, short_description, content_markdown, content_html, status, created_by)
VALUES
    (1, 'First Post', 'first-post', 'This is the first post', '# Markdown content', '<p>HTML content</p>', 'DRAFT', 1),
    (2, 'Second Post', 'second-post', 'This is the second post', '# Markdown content 2', '<p>HTML content 2</p>', 'PUBLISHED', 2),
    (3, 'Third Post', 'third-post', 'This is the third post', '# Markdown content 3', '<p>HTML content 3</p>', 'DRAFT', 1),
    (4, 'Fourth Post', 'fourth-post', 'This is the fourth post', '# Markdown content 4', '<p>HTML content 4</p>', 'PUBLISHED', 2),
    (5, 'Fifth Post', 'fifth-post', 'This is the fifth post', '# Markdown content 5', '<p>HTML content 5</p>', 'DRAFT', 1),
    (6, 'Sixth Post', 'sixth-post', 'This is the sixth post', '# Markdown content 6', '<p>HTML content 6</p>', 'PUBLISHED', 2),
    (7, 'Seventh Post', 'seventh-post', 'This is the seventh post', '# Markdown content 7', '<p>HTML content 7</p>', 'DRAFT', 1),
    (8, 'Eighth Post', 'eighth-post', 'This is the eighth post', '# Markdown content 8', '<p>HTML content 8</p>', 'PUBLISHED', 2),
    (9, 'Ninth Post', 'ninth-post', 'This is the ninth post', '# Markdown content 9', '<p>HTML content 9</p>', 'DRAFT', 2),
    (10, 'Tenth Post', 'tenth-post', 'This is the tenth post', '# Markdown content 10', '<p>HTML content 10</p>', 'PUBLISHED', 1),
    (11,'Eleventh Post', 'eleventh-post', 'This is the eleventh post', '# Markdown content 11', '<p>HTML content 11</p>', 'DRAFT', 2),
    (12,'Twelfth Post', 'twelfth-post', 'This is the twelfth post', '# Markdown content 12', '<p>HTML content 12</p>', 'PUBLISHED', 1),
    (13, 'Thirteenth Post', 'thirteenth-post', 'This is the thirteenth post', '# Markdown content 13', '<p>HTML content 13</p>', 'DRAFT', 2),
    (14, 'Fourteenth Post', 'fourteenth-post', 'This is the fourteenth post', '# Markdown content 14', '<p>HTML content 14</p>', 'PUBLISHED', 1),
    (15, 'Fifteenth Post', 'fifteenth-post', 'This is the fifteenth post', '# Markdown content 15', '<p>HTML content 15</p>', 'DRAFT', 1),
    (16, 'Sixteenth Post', 'sixteenth-post', 'This is the sixteenth post', '# Markdown content 16', '<p>HTML content 16</p>', 'PUBLISHED', 2),
    (17, 'Seventeenth Post', 'seventeenth-post', 'This is the seventeenth post', '# Markdown content 17', '<p>HTML content 17</p>', 'DRAFT', 1),
    (18, 'Eighteenth Post', 'eighteenth-post', 'This is the eighteenth post', '# Markdown content 18', '<p>HTML content 18</p>', 'PUBLISHED', 2),
    (19, 'Nineteenth Post', 'nineteenth-post', 'This is the nineteenth post', '# Markdown content 19', '<p>HTML content 19</p>', 'DRAFT', 1),
    (20, 'Twentieth Post', 'twentieth-post', 'This is the twentieth post', '# Markdown content 20', '<p>HTML content 20</p>', 'PUBLISHED', 2),
    (21, 'Twenty-first Post', 'twenty-first-post', 'This is the twenty-first post', '# Markdown content 21', '<p>HTML content 21</p>', 'DRAFT', 1),
    (22, 'Twenty-second Post', 'twenty-second-post', 'This is the twenty-second post', '# Markdown content 22', '<p>HTML content 22</p>', 'PUBLISHED', 1);

INSERT INTO COMMENTS (id, name, content, post_id, created_by)
VALUES
    (1, 'Geovanny', 'This is a comment on the first post.', 1, 2),
    (2, 'Siva', 'This is another comment on the first post.', 1, 1);