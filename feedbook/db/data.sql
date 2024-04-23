insert into account(nickname, password, username)
values ('Tester1', '$2a$10$IVh7EmbOtd/UdAEdpcfH6.94mVDhRrplXojPJd/66zSSGVq4.nIxG', 'test1');

insert into account(nickname, password, username)
values ('Tester2', '$2a$10$IVh7EmbOtd/UdAEdpcfH6.94mVDhRrplXojPJd/66zSSGVq4.nIxG', 'test2');

insert into feed(account_id, content, title, favorites)
values (1, 'test content', 'title1', 1);
insert into feed(account_id, content, title)
values (1, 'test content', 'title2');
insert into feed(account_id, content, title)
values (1, 'test content', 'title3');

insert into feed(account_id, content, title)
values (2, 'ccc', 'tttt');

insert into account_favorite_feed(account_id, feed_id)
values (1, 1);