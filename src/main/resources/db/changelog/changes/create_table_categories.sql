--liquibase formatted sql

--changeset jevgenijs:1

CREATE TABLE categories
(
    category_id   INT PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL
);