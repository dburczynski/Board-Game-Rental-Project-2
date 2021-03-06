create table client(
    id integer generated by default as identity (start with 1),
    name varchar(50) not null,
    dateOfJoin date not null,
    phoneNumber varchar(9) not null,
    address varchar(50 not null)
    primary key (id)
)

create table game (
    id integer generated by default as identity (start with 1),
    name varchar(50) not null,
    genre varchar(50) not null,
    client_id integer,
    primary key (id)
)

create table rental (
    id integer generated by default as identity (start with 1),
    dateOfRent date,
    dateOfReturn date,
    client_id integer,
    game_id integer,
    returned boolean,
    destroyed boolean,
    depositPayed boolean,
    primary key (id)
)

alter table game
       add constraint FK513q01unmmjxkrgo23jp151pd
       foreign key (client_id)
       references client

alter table game
       add constraint FKvlrtfd4vg5vo07qml8w5lgd5
       foreign key (games_id)
       references client

alter table rental
       add constraint FKfurpp295i3dhumquorur054dw
       foreign key (client_id)
       references client

alter table rental
       add constraint FKqrfpixy27jnuildebkj72q784
       foreign key (game_id)
       references game

alter table rental
       add constraint FK73d5241n8r0v3vltcbl1e5l88
       foreign key (rentals_id)
       references game