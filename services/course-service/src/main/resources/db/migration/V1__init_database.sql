create table if not exists category
(
    id serial       not null primary key,
    name        varchar(255) not null,
    description varchar(255) not null
);

create table if not exists course
(
    id          serial      not null primary key,
    title       varchar(255) not null,
    description varchar(255) not null,
    category_id integer
        constraint fkcategorycourse references category
);

create table if not exists lesson
(
    id            serial      not null primary key,
    title         varchar(255) not null,
    description   varchar(255) not null,
    course_id     integer
        constraint fkcourselesson references course,
    resource_type varchar(50)  not null
);

create sequence if not exists category_seq increment by 50;
create sequence if not exists course_seq increment by 50;
create sequence if not exists lesson_seq increment by 50;