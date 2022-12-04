create table game_room
(
    id          bigserial primary key,
    name        varchar(60)  not null,
    description varchar(200) not null,
    total_price decimal      not null default 0.0,
    size        varchar(14)  not null
);

create table ages
(
    id           serial primary key,
    game_room_id bigint      not null,
    age          varchar(14) not null,
    foreign key (game_room_id) references game_room ON DELETE CASCADE
);

create table toy
(
    --main id which is present in abstract toy
    id          bigserial primary key,
    type        varchar(30)  not null,
    name        varchar(60)  not null,
    description varchar(200) not null,
    price       decimal     default 0.0,
    size        varchar(14)  not null,
    age         varchar(14) default 'SMALL'
);

create table car
(
    --sub-id only for database
    id       bigserial,
    toy_id   bigint      not null,
    speed    decimal default 0.0,
    material varchar(30) not null,
    color    varchar(30) not null,
    foreign key (toy_id) references toy ON DELETE CASCADE
);

create table doll
(
    id              bigserial,
    toy_id          bigint      not null,
    hair_color      varchar(30) not null,
    special_ability varchar(30) not null,
    jewellery       varchar(30) not null,
    foreign key (toy_id) references toy ON DELETE CASCADE
);

create table cubes
(
    --sub-id only for database
    id           bigserial,
    toy_id       bigint  not null,
    amount       integer not null,
    price_by_one decimal not null,
    weight       decimal not null,
    foreign key (toy_id) references toy ON DELETE CASCADE
);

create table musical
(
    --sub-id only for database
    id                   bigserial,
    toy_id               bigint      not null,
    is_auto_play_enabled boolean default false,
    music_type           varchar(40) not null,
    material             varchar(30) not null,
    foreign key (toy_id) references toy ON DELETE CASCADE
);

create table toys
(
    id           serial primary key,
    game_room_id bigint not null,
    toy_id       bigint not null,
    foreign key (game_room_id) references game_room ON DELETE CASCADE,
    foreign key (toy_id) references toy ON DELETE CASCADE
);

---------------
