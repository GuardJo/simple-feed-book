INSERT INTO public.account (created_at, id, modified_at, nickname, password, username)
VALUES ('2024-03-20 15:30:31.250000', 1, '2024-03-20 15:30:31.250000', 'Tester',
        '$2a$10$IVh7EmbOtd/UdAEdpcfH6.94mVDhRrplXojPJd/66zSSGVq4.nIxG', 'test1');

INSERT INTO public.feed (account_id, created_at, id, modified_at, content, title)
VALUES (1, '2024-03-20 15:36:44.607000', 1, '2024-03-20 15:36:44.607000', 'content1', 'test1');
INSERT INTO public.feed (account_id, created_at, id, modified_at, content, title)
VALUES (1, '2024-03-20 15:36:45.419000', 2, '2024-03-20 15:36:45.419000', 'content2', 'test2');
INSERT INTO public.feed (account_id, created_at, id, modified_at, content, title)
VALUES (1, '2024-03-20 15:36:46.141000', 3, '2024-03-20 15:36:46.141000', 'content3', 'test3');
