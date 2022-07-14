CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE contacts(
    id UUID NOT NULL DEFAULT uuid_generate_v4() ,
    CONSTRAINT contacts_pkey PRIMARY KEY (id),
    image varchar(100),
    name varchar(20) NOT NULL,
    description varchar(40),
    phone varchar(12) NOT NULL
);