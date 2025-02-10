-- Write your Task 1 answers in this file

SELECT "Creating bedandbreakfast database...";
-- drop database if exists 
DROP DATABASE IF EXISTS bedandbreakfast;

-- create database 
CREATE DATABASE bedandbreakfast;

-- use database 
USE bedandbreakfast;

SELECT "Creating users table...";
-- create table users 
CREATE TABLE users (
    email varchar(128), 
    name varchar(128),
    CONSTRAINT pk_email PRIMARY KEY (email)
);

SELECT "Creating bookings table...";
-- create table bookings
CREATE TABLE bookings (
    booking_id char(8), 
    listing_id varchar(20), 
    duration int, 
    email varchar(128),
    CONSTRAINT pk_booking_id PRIMARY KEY (booking_id),
    CONSTRAINT fk_email 
        FOREIGN KEY (email)
        REFERENCES users(email)
);

SELECT "Creating reviews table...";
-- create table reviews 
CREATE TABLE reviews (
    id int AUTO_INCREMENT, 
    date timestamp, 
    listing_id varchar(20), 
    reviewer_name varchar(64), 
    comments text, 
    CONSTRAINT pk_review_id PRIMARY KEY (id)
);

-- batch SQL insert
-- insert all records in users.csv into the users table
LOAD DATA LOCAL INFILE 'users.csv'
INTO TABLE users
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 ROWS