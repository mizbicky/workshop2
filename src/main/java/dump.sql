CREATE DATABASE workshop2
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE TABLE users (
id int(11) NOT NULL AUTO_INCREMENT primary key,
email varchar(255) unique,
username varchar(255),
password varchar(60)
);