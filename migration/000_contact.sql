CREATE TABLE contacts(
    Id BIGSERIAL PRIMARY KEY NOT NULL,
    image varchar(100) NOT NULL,
    name varchar(20) NOT NULL,
    description varchar(40) NOT NULL,
    phone varchar(12) NOT NULL
);