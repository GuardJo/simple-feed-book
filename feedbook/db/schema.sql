create sequence account_id_seq;
create sequence feed_id_seq;

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
    id          bigint       default nextval('feed_id_seq')
        primary key,
    account_id  bigint
        constraint fkk8dm0jua2n0cqhxdedr6fdvq2
            references account,
    created_at  timestamp(6) default current_timestamp,
    modified_at timestamp(6) default current_timestamp,
    content     varchar(255),
    title       varchar(255) not null
);


