-- BEGIN SECTION 1 --------------------------------------
create table EMPLOYEE
(
  ID        bigint not null,
  EMPLOYEE_NUMBER VARCHAR(100),
  NAME      VARCHAR(200),
  SURNAME   VARCHAR(200),
  AGE	bigint not null,
  PRIMARY KEY (ID)
);

create table TEMP_TABLE 
(
  ID        bigint not null,
  NAME      VARCHAR(200)
);

-- END SECTION 1 ----------------------------------------


-- BEGIN SECTION 2 --------------------------------------
-- Create table
create table PEOPLE
(
  ID          bigint not null GENERATED BY DEFAULT AS IDENTITY (start with 1),
  firstname        varchar(100),
  lastname     varchar(100),
  birthdate timestamp,
  deathdate date,
  firstblob	BLOB ,
  secondblob BLOB, 
  firstclob CLOB
);

alter table PEOPLE  add constraint IDX_PEOPLE primary key (ID);


-- Create table
create table BLOBCLOB
(
  id          bigint not null GENERATED BY DEFAULT AS IDENTITY (start with 1),
  BLOB_FIELD BLOB ,
  CLOB_FIELD CLOB
);

alter table BLOBCLOB add constraint IDX_BLOBCLOB primary key (ID);


-- END SECTION 2 ----------------------------------------


-- BEGIN SECTION 3 --------------------------------------
-- Create table
create table AUTHORS
(
  id          bigint not null GENERATED BY DEFAULT AS IDENTITY (start with 1),
  firstname        varchar(100),
  lastname     varchar(100),
  birthdate timestamp,
  PRIMARY KEY (ID)
);
  


-- Create table
create table PUBLICATIONS
(
  ID                bigint not null GENERATED BY DEFAULT AS IDENTITY (start with 1),
  IDAUTHOR	bigint not null,
  TITLE            VARCHAR(200),
  PUBLICATIONDATE DATE,
  PRIMARY KEY (ID)
);

ALTER TABLE PUBLICATIONS ADD FOREIGN KEY (IDAUTHOR) REFERENCES AUTHORS (ID) on delete cascade;


-- Create table
create table ADDRESS
(
  ID                bigint not null GENERATED BY DEFAULT AS IDENTITY (start with 1),
  IDAUTHOR			bigint not null,
  ADDRESS            VARCHAR(200),
  CITY            VARCHAR(200),
  PRIMARY KEY (ID)
);

ALTER TABLE ADDRESS ADD FOREIGN KEY (IDAUTHOR) REFERENCES AUTHORS (ID) on delete cascade;

-- END SECTION 3 ----------------------------------------


-- BEGIN SECTION 4 --------------------------------------
-- Create schema 
create schema ZOO;

-- Create table
create table ZOO.PEOPLE
(
  id          bigint not null GENERATED BY DEFAULT AS IDENTITY (start with 1),
  firstname        varchar(100),
  lastname     varchar(100),
  birthdate timestamp,
  deathdate date,
  firstblob	BLOB ,
  secondblob BLOB, 
  firstclob CLOB
);

alter table ZOO.PEOPLE  add constraint ZOO_IDX_PEOPLE primary key (ID);

-- END SECTION 4 ----------------------------------------


-- BEGIN SECTION 5 --------------------------------------

-- Create table with autogenerated pk
create table AUTO_ID
(
  ID bigint GENERATED BY DEFAULT AS IDENTITY (start with 1),
  VALUE varchar(100),
  PRIMARY KEY (ID)
);

-- END SECTION 5 ----------------------------------------


-- BEGIN SECTION 6 --------------------------------------
-- tables to test the @VERSION annotation

create table DATA_VERSION_INT
(
  ID bigint GENERATED BY DEFAULT AS IDENTITY (start with 1),
  DATA varchar(100),
  VERSION NUMERIC,
  PRIMARY KEY (ID)
);

create table DATA_VERSION_TIMESTAMP
(
  ID bigint GENERATED BY DEFAULT AS IDENTITY (start with 1),
  DATA varchar(100),
  VERSION timestamp,
  PRIMARY KEY (ID)
);

-- END SECTION 6 ----------------------------------------


-- BEGIN SECTION 7 --------------------------------------
-- tables to test queries using WrapperTypes

create table WRAPPER_TYPE_TABLE
(
  ID bigint GENERATED BY DEFAULT AS IDENTITY (start with 1),
  START_DATE timestamp,
  NOW timestamp,
  END_DATE timestamp,
  VALID numeric(1),
  PRIMARY KEY (ID)
);

-- END SECTION 7 ----------------------------------------

-- BEGIN SECTION 8 --------------------------------------
-- aggregated beans

create table USERS
(
  ID bigint GENERATED BY DEFAULT AS IDENTITY (start with 1),
  FIRSTNAME      VARCHAR(200) not null,
  LASTNAME   VARCHAR(200) not null,
  AGE        bigint,
  VERSION NUMERIC not null,
  PRIMARY KEY (ID)
);

create table USER_COUNTRY
(
  ID bigint GENERATED BY DEFAULT AS IDENTITY (start with 1),
  NAME VARCHAR(200) not null,
  PRIMARY KEY (ID)
);

create table USER_ADDRESS
(
  USER_ID bigint not null,
  COUNTRY_ID bigint not null,
  FOREIGN KEY (USER_ID) REFERENCES USERS (ID),
  FOREIGN KEY (COUNTRY_ID) REFERENCES USER_COUNTRY (ID)
);

create table USER_JOB
(
  ID bigint GENERATED BY DEFAULT AS IDENTITY (start with 1),
  USER_ID bigint not null,
  NAME VARCHAR(200),
  FOREIGN KEY (USER_ID) REFERENCES USERS (ID),
  PRIMARY KEY (ID)
);

create table USER_JOB_TASK
(
  ID bigint GENERATED BY DEFAULT AS IDENTITY (start with 1),
  USER_JOB_ID bigint not null,
  NAME VARCHAR(200),
  FOREIGN KEY (USER_JOB_ID) REFERENCES USER_JOB (ID),
  PRIMARY KEY (ID)
);

-- BEGIN SECTION 8 --------------------------------------

