-- BEGIN SECTION 1 --------------------------------------

create table EMPLOYEE
(
  ID        NUMBER not null,
  EMPLOYEE_NUMBER VARCHAR2(100),
  NAME      VARCHAR2(200),
  SURNAME   VARCHAR2(200),
  AGE	NUMBER not null,
  PRIMARY KEY (ID)
);

create table TEMP_TABLE 
(
  ID        NUMBER not null,
  NAME      VARCHAR2(200)
);

-- END SECTION 1 ----------------------------------------


-- BEGIN SECTION 2 --------------------------------------

-- Create table
create table PEOPLE
(
  id          number not null,
  firstname        varchar2(100),
  lastname     varchar2(100),
  birthdate timestamp(6),
  deathdate date,
  firstblob	BLOB ,
  secondblob BLOB, 
  firstclob CLOB
);

alter table PEOPLE  add constraint IDX_PEOPLE primary key (ID);

create sequence SEQ_PEOPLE start with 1 increment by 1;

-- Create table
create table BLOBCLOB
(
  id          number not null,
  blob_field BLOB ,
  clob_field CLOB
);

alter table BLOBCLOB add constraint IDX_BLOBCLOB primary key (ID);

create sequence SEQ_BLOBCLOB start with 1 increment by 1;
-- END SECTION 2 ----------------------------------------


-- BEGIN SECTION 3 --------------------------------------

-- Create table
create table AUTHORS
(
  id          number not null,
  firstname        varchar2(100),
  lastname     varchar2(100),
  birthdate timestamp(6),
  PRIMARY KEY (ID)
);
  
-- Create sequence 
create sequence SEQ_AUTHORS start with 1 increment by 1;


-- Create table
create table PUBLICATIONS
(
  ID                NUMBER not null,
  IDAUTHOR	NUMBER not null,
  TITLE            VARCHAR2(200),
  PUBLICATIONDATE DATE,
  PRIMARY KEY (ID)
);

ALTER TABLE PUBLICATIONS ADD FOREIGN KEY (IDAUTHOR) REFERENCES AUTHORS (ID) on delete cascade;
  
-- Create sequence 
create sequence SEQ_PUBLICATIONS start with 1 increment by 1;  


-- Create table
create table ADDRESS
(
  ID                NUMBER not null,
  IDAUTHOR			NUMBER not null,
  ADDRESS            VARCHAR2(200),
  CITY            VARCHAR2(200),
  PRIMARY KEY (ID)
);

ALTER TABLE ADDRESS ADD FOREIGN KEY (IDAUTHOR) REFERENCES AUTHORS (ID) on delete cascade;

create sequence SEQ_ADDRESS start with 1 increment by 1;  
-- END SECTION 3 ----------------------------------------


-- BEGIN SECTION 4 --------------------------------------
-- END SECTION 4 ----------------------------------------


-- BEGIN SECTION 5 --------------------------------------

create table AUTO_ID
(
  ID INTEGER not null,
  VALUE varchar2(100),
  PRIMARY KEY (ID)
);

create sequence SEQ_AUTO_ID start with 1 increment by 1;  

-- END SECTION 5 ----------------------------------------


-- BEGIN SECTION 6 --------------------------------------
-- tables to test the @VERSION annotation

create table DATA_VERSION_INT
(
  ID INTEGER not null,
  DATA varchar2(100),
  VERSION NUMBER,
  PRIMARY KEY (ID)
);

create sequence SEQ_DATA_VERSION_INT start with 1 increment by 1;  

create table DATA_VERSION_TIMESTAMP
(
  ID INTEGER not null,
  DATA varchar2(100),
  VERSION timestamp(8),
  PRIMARY KEY (ID)
);

create sequence SEQ_DATA_VERSION_TIMESTAMP start with 1 increment by 1; 
-- END SECTION 6 ----------------------------------------


-- BEGIN SECTION 7 --------------------------------------
-- tables to test queries using WrapperTypes

create table WRAPPER_TYPE_TABLE
(
  ID INTEGER not null,
  START_DATE timestamp(8),
  NOW timestamp(8),
  END_DATE timestamp(8),
  VALID NUMBER(1),
  PRIMARY KEY (ID)
);

create sequence SEQ_WRAPPER_TYPE_TABLE start with 1 increment by 1; 

-- END SECTION 7 ----------------------------------------



-- BEGIN SECTION 8 --------------------------------------
-- aggregated beans

create table USERS
(
  ID INTEGER not null,
  FIRSTNAME      VARCHAR2(200) not null,
  LASTNAME   VARCHAR2(200) not null,
  AGE INTEGER,
  VERSION NUMBER not null,
  PRIMARY KEY (ID)
);
create sequence SEQ_USERS start with 1 increment by 1; 

create table USER_COUNTRY
(
  ID INTEGER not null,
  NAME VARCHAR2(200) not null,
  PRIMARY KEY (ID)
);
create sequence SEQ_USER_COUNTRY start with 1 increment by 1; 

create table USER_ADDRESS
(
  USER_ID INTEGER not null,
  COUNTRY_ID INTEGER not null,
  FOREIGN KEY (USER_ID) REFERENCES USERS (ID),
  FOREIGN KEY (COUNTRY_ID) REFERENCES USER_COUNTRY (ID)
);
create sequence SEQ_USER_ADDRESS start with 1 increment by 1; 

create table USER_JOB
(
  ID INTEGER not null,
  USER_ID INTEGER not null,
  NAME VARCHAR2(200),
  FOREIGN KEY (USER_ID) REFERENCES USERS (ID),
  PRIMARY KEY (ID)
);
create sequence SEQ_USER_JOB start with 1 increment by 1; 

create table USER_JOB_TASK
(
  ID INTEGER not null,
  USER_JOB_ID INTEGER not null,
  NAME VARCHAR2(200),
  FOREIGN KEY (USER_JOB_ID) REFERENCES USER_JOB (ID),
  PRIMARY KEY (ID)
);
create sequence SEQ_USER_JOB_TASK start with 1 increment by 1; 


-- BEGIN SECTION 8 --------------------------------------