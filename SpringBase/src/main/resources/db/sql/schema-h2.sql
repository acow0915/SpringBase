--create schema if not exists testdb;  
--create schema if not exists writedb;  
--create schema if not exists readdb;  

create table ACCOUNT (
id         INTEGER auto_increment PRIMARY KEY ,
name VARCHAR(30),
age  INTEGER
);
