create table if not exists User
(
    id bigint auto_increment primary key,
    created datetime null,
    modified datetime null,
    email varchar(255) not null,
    lastLogin datetime null,
    passwordHash varchar(255) null,
    username varchar(255) null,
    lastPasswordReset datetime null
);