create sequence account_id_seq;
create sequence feed_id_seq;
create sequence feed_comment_id_seq;
create sequence feed_alarm_id_seq;

create table account
(
    id          bigint       not null default nextval('account_id_seq')
        primary key,
    created_at  timestamp(6)          default current_timestamp,
    modified_at timestamp(6)          default current_timestamp,
    nickname    varchar(255),
    password    varchar(255),
    username    varchar(255) not null unique
        unique
);

create table feed
(
    id          bigint                default nextval('feed_id_seq')
        primary key,
    account_id  bigint
        constraint fkk8dm0jua2n0cqhxdedr6fdvq2
            references account,
    created_at  timestamp(6)          default current_timestamp,
    modified_at timestamp(6)          default current_timestamp,
    content     varchar(255),
    title       varchar(255) not null,
    favorites   integer      not null default 0
);

create table account_favorite_feed
(
    account_id bigint
        constraint account_favorite_feed_account_id_fk
            references account,
    feed_id    bigint
        constraint account_favorite_feed_feed_id_fk
            references feed
);

create table feed_comment
(
    id          bigint       not null default nextval('feed_comment_id_seq') primary key,
    created_at  timestamp(6)          default current_timestamp,
    modified_at timestamp(6)          default current_timestamp,
    content     varchar(500) not null,
    feed_id     bigint       not null
        constraint feed_comment_feed_id_fk references feed,
    account_id  bigint       not null
        constraint feed_comment_account_id_fk references account
);

create table feed_alarm
(
    id          bigint       default nextval('feed_alarm_id_seq') not null primary key,
    alarm_type  varchar(50)                                       not null,
    args        jsonb                                             not null,
    created_at  timestamp(6) default current_timestamp,
    modified_at timestamp(6) default current_timestamp,
    feed_id     bigint
        constraint "feed_alarm_feed_id_fk"
            references feed
)