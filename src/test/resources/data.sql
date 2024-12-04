INSERT INTO categories(`id`, `name`)
VALUES
(1, 'name');

INSERT INTO users(`id`, `is_account_non_expired`, `is_account_non_locked`,
`is_credentials_non_expired`, `is_enabled`, `username`, `email`, `password`, `provider`)
VALUES
('786a5aeb-1c12-4552-8d98-e576071d02c8', true, true, true, true, 'thunder', 'test@abv.bg', 'password', 'MusicHub');

INSERT INTO songs(`id`, `title`, `song_partial_url`, `uploaded_on`,
`category_id`, `uploader`)
VALUES
(1, 'Sagi Abitbul - Stanga', 'partialUrl', '2017-04-01 17:14:04', 1, '786a5aeb-1c12-4552-8d98-e576071d02c8'),
(2, 'Sean Paul - No Lie', 'partialUrl', '2017-04-01 17:33:04', 1, '786a5aeb-1c12-4552-8d98-e576071d02c8'),
(3, 'Filatov & Karas - Satellite', 'partialUrl', '2017-04-01 17:14:04', 1, '786a5aeb-1c12-4552-8d98-e576071d02c8'),
(4, 'WRLD - Little too close', 'partialUrl', '2017-05-01 17:14:04', 1, '786a5aeb-1c12-4552-8d98-e576071d02c8'),
(5, '50 Cent - My Life', 'partialUrl', '2017-05-01 17:30:04', 1, '786a5aeb-1c12-4552-8d98-e576071d02c8'),
(6, 'Flo Rida - Wild Ones', 'partialUrl', '2017-09-01 17:11:04', 1, '786a5aeb-1c12-4552-8d98-e576071d02c8');

INSERT INTO comments(`id`, `content`, `published_on`, `status`,
`author_id`, `song_id`)
VALUES
(1, 'content', '2018-04-01 17:40:04', 'PENDING','786a5aeb-1c12-4552-8d98-e576071d02c8', 1),
(2, 'content', '2018-04-02 17:40:04', 'PENDING','786a5aeb-1c12-4552-8d98-e576071d02c8', 1),
(3, 'content', '2018-04-01 17:40:23', 'REJECTED','786a5aeb-1c12-4552-8d98-e576071d02c8', 1),
(4, 'content', '2018-01-01 17:40:23', 'APPROVED','786a5aeb-1c12-4552-8d98-e576071d02c8', 1);

INSERT INTO roles(`id`, `name`)
VALUES
('eac5d95c-8876-4b6e-8ca7-7c0824b8ab7d', 'ROLE_ADMIN'),
('e7b4da2e-c811-492b-b2b7-50fc276e0ab8', 'ROLE_MODERATOR');

INSERT INTO tags(`id`, `name`)
VALUES
(1, 'Dj'),
(2, '50 Cent'),
(3, 'Test');

INSERT INTO songs_tags(`song_id`, `tag_id`)
VALUES
  (1, 1),
  (2, 2),
  (3, 2),
  (3, 1);



