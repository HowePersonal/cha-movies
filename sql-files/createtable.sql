CREATE DATABASE IF NOT EXISTS moviedb;

USE moviedb;

CREATE TABLE IF NOT EXISTS movies (
    id varchar(10) NOT NULL,
    title varchar(100) NOT NULL,
    year integer NOT NULL,
    director varchar(100) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS stars (
    id varchar(10) NOT NULL,
    name varchar(100) NOT NULL,
    birthYear integer,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS stars_in_movies(
    starId varchar(10) NOT NULL,
    movieId varchar(10) NOT NULL,
    FOREIGN KEY(starId) REFERENCES stars(id),
    FOREIGN KEY(movieId) REFERENCES movies(id)
);

CREATE TABLE IF NOT EXISTS genres (
    id integer auto_increment NOT NULL,
    name varchar(32) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS genres_in_movies(
    genreId integer NOT NULL,
    movieId varchar(10) NOT NULL,
    FOREIGN KEY(genreId) REFERENCES genres(id),
    FOREIGN KEY(movieId) REFERENCES movies(id)
);


CREATE TABLE IF NOT EXISTS creditcards(
    id varchar(20) NOT NULL,
    firstName varchar(50) NOT NULL,
    lastName varchar(50) NOT NULL,
    expiration date NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS customers(
    id integer NOT NULL AUTO_INCREMENT,
    firstName varchar(50) NOT NULL,
    lastName varchar(50) NOT NULL,
    ccId varchar(20) NOT NULL,
    address varchar(200)  NOT NULL,
    email varchar(50) NOT NULL,
    password varchar(20) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(ccId) REFERENCES creditcards(id)
);

CREATE TABLE IF NOT EXISTS sales(
    id integer NOT NULL AUTO_INCREMENT,
    customerId integer NOT NULL,
    movieId varchar(10) NOT NULL,
    saleDate date NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(customerId) REFERENCES customers(id),
    FOREIGN KEY(movieId) REFERENCES movies(id)
);

CREATE TABLE IF NOT EXISTS ratings(
    movieId varchar(10) NOT NULL,
    rating float NOT NULL,
    numVotes integer NOT NULL,
    FOREIGN KEY(movieId) REFERENCES movies(id)
);

CREATE TABLE IF NOT EXISTS employees(
    email varchar(50) not null,
    password varchar(20) not null,
    fullname varchar(100),
    primary key(email)
);

CREATE UNIQUE INDEX index_movies ON  movies ( id, year);
ALTER TABLE movies ADD FULLTEXT(title);
ALTER TABLE movies ADD FULLTEXT(director);

CREATE UNIQUE INDEX index_stars ON  stars ( id, birthYear);
ALTER TABLE stars ADD FULLTEXT(name);

CREATE UNIQUE INDEX index_genres ON  genres ( id);
ALTER TABLE genres ADD FULLTEXT(name);

CREATE UNIQUE INDEX index_ratings ON  ratings ( Movieid, rating, numVotes);

CREATE UNIQUE INDEX index_genres_in_movies ON genres_in_movies(genreId, movieId);

CREATE UNIQUE INDEX index_stars_in_movies ON stars_in_movies(starId, movieId);

