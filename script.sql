create table personel
(
    id            serial
        primary key,
    nom           varchar(255),
    prenom        varchar(255),
    datenaissance date,
    tel           varchar(255)
);

alter table personel
    owner to postgres;

create table client
(
    id      integer not null
        primary key
        references personel,
    address varchar(255)
);

alter table client
    owner to postgres;

create table employee
(
    id              integer not null
        primary key
        references personel,
    email           varchar(255),
    daterecrutement date
);

alter table employee
    owner to postgres;

create table compte
(
    numero       serial
        primary key,
    solde        double precision,
    datecreation date,
    etat         varchar(255),
    id_cl        integer
        references client,
    id_employee  integer
        references employee
);

alter table compte
    owner to postgres;

create table comptescourants
(
    numero    serial
        primary key
        constraint fk_compte_courant_compte
            references compte
            on delete cascade,
    decouvert double precision
);

alter table comptescourants
    owner to postgres;

create table comptesepargnes
(
    numero      serial
        primary key
        constraint fk_compte_courant_compte
            references compte
            on delete cascade,
    tauxinteret double precision
);

alter table comptesepargnes
    owner to postgres;

create table mission
(
    id          serial
        primary key,
    nom         varchar(255),
    description text
);

alter table mission
    owner to postgres;

create table missionemploye
(
    id         serial
        primary key,
    datedebut  date,
    datefin    date,
    mission_id integer
        constraint fk_mission_id
            references mission
            on update cascade on delete cascade,
    employe_id integer
        constraint fk_employe_id
            references employee
            on update cascade on delete cascade
);

alter table missionemploye
    owner to postgres;

create table operation
(
    id            serial
        primary key,
    type          varchar(255),
    datecreation  date,
    montant       double precision,
    compte_numero integer
        references compte,
    employe_id    integer
        references employee
);

alter table operation
    owner to postgres;


