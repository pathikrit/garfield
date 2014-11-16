# --- !Ups

CREATE TABLE people
(
    id SERIAL PRIMARY KEY NOT NULL
);

# --- !Downs

DROP TABLE people;
