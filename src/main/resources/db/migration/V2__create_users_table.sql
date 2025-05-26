create table users(
  id        serial        primary key,
  email     varchar(128)  not null unique,
  password  varchar(512)  not null
)