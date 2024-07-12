CREATE DATABASE belajar_spring_restful_api;

use belajar_spring_restful_api;

CREATE table users
(
    username         VARCHAR(100) not null,
    password         VARCHAR(100) not null,
    name             VARCHAR(100) not null,
    token            VARCHAR(100),
    token_expired_at BIGINT,
    PRIMARY KEY (username),
    UNIQUE (token)
) ENGINE InnoDB;

SELECT *
from users;

Desc users;

CREATE table contacts
(
    id         varchar(100) NOT NULL,
    username   varchar(100) not null,
    first_name varchar(100) not null,
    last_name  varchar(100),
    phone      varchar(100),
    email      varchar(100),
    primary key (id),
    foreign key fk_users_contacts (username) references users (username)
) ENGINE InnoDB;

SELECT *
from contacts;

create table addresses
(
    id        varchar(100) NOT NULL,
    contact_id varchar(100) not null,
    street varchar(200),
    city  varchar(100),
    country varchar(100)not null,
    post_code varchar(10),
    primary key (id),
    foreign key fk_contacts_addresses (contact_id) references contacts (id)
) ENGINE InnoDB;

select * from addresses;

desc addresses;
