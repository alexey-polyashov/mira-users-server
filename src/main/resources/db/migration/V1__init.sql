
--CREATE TABLES

--USERS & ROLES
create table users
(
    id         bigserial primary key,
    login    varchar(30) not null unique,
    password    varchar(80) not null,
    email       varchar(150) unique,
    name   varchar(150)
);

create table roles
(
    id         bigserial primary key,
    name       varchar(50) not null
);

CREATE TABLE users_roles
(
    user_id bigint not null references users (id),
    role_id bigint not null references roles (id),
    primary key (user_id, role_id)
);

--TEST DATA

insert into users (login, password, email, name)
values ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'bob_johnson@gmail.com', 'Lorem'),
       ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'john_johnson@gmail.com', 'Ipsum'),
       ('user2', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'nick_davidson@gmail.com', 'Ivan');

insert into roles (name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');


insert into users_roles (user_id, role_id)
values (1, 1),
       (2, 2),
       (3, 1);
