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

insert into feed_comment(content, feed_id, account_id)
values ('Comment 1', 1, 1);
insert into feed_comment(content, feed_id, account_id)
values ('Comment 2', 1, 2);
insert into feed_comment(content, feed_id, account_id)
values ('Commment 3', 2, 2);

insert into feed_comment(content, feed_id, account_id)
values ('Comment 1', 1, 1);
insert into feed_comment(content, feed_id, account_id)
values ('Comment 2', 1, 2);
insert into feed_comment(content, feed_id, account_id)
values ('Commment 3', 2, 2);
insert into feed_comment(content, feed_id, account_id)
values ('Comment 1', 1, 1);
insert into feed_comment(content, feed_id, account_id)
values ('Comment 2', 1, 2);
insert into feed_comment(content, feed_id, account_id)
values ('Commment 3', 2, 2);
insert into feed_comment(content, feed_id, account_id)
values ('Comment 1', 1, 1);
insert into feed_comment(content, feed_id, account_id)
values ('Comment 2', 1, 2);
insert into feed_comment(content, feed_id, account_id)
values ('Commment 3', 2, 2);

insert into feed_alarm(alarm_type, args, feed_id)
values ('FAVORITE', '{"accountId": 1}', 1);
insert into feed_alarm(alarm_type, args, feed_id)
values ('COMMENT', '{"accountId": 1}', 1);
insert into feed_alarm(alarm_type, args, feed_id)
values ('COMMENT', '{"accountId": 2}', 1);

insert into feed_alarm(alarm_type, args, feed_id)
values ('COMMENT', '{"accountId": 2}', 2);
insert into feed_alarm(alarm_type, args, feed_id)
values ('COMMENT', '{"accountId": 1}', 1);
insert into feed_alarm(alarm_type, args, feed_id)
values ('COMMENT', '{"accountId": 2}', 1);
insert into feed_alarm(alarm_type, args, feed_id)
values ('COMMENT', '{"accountId": 2}', 2);
insert into feed_alarm(alarm_type, args, feed_id)
values ('COMMENT', '{"accountId": 1}', 1);
insert into feed_alarm(alarm_type, args, feed_id)
values ('COMMENT', '{"accountId": 2}', 1);
insert into feed_alarm(alarm_type, args, feed_id)
values ('COMMENT', '{"accountId": 2}', 2);
insert into feed_alarm(alarm_type, args, feed_id)
values ('COMMENT', '{"accountId": 1}', 1);
insert into feed_alarm(alarm_type, args, feed_id)
values ('COMMENT', '{"accountId": 2}', 1);
insert into feed_alarm(alarm_type, args, feed_id)
values ('COMMENT', '{"accountId": 2}', 2);