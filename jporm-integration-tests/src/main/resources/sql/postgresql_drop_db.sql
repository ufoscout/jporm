-- BEGIN SECTION 1 --------------------------------------
drop table IF EXISTS EMPLOYEE cascade;
drop table IF EXISTS TEMP_TABLE cascade;

-- BEGIN SECTION 2 --------------------------------------
drop table IF EXISTS PEOPLE cascade;
drop sequence IF EXISTS SEQ_PEOPLE;
drop table IF EXISTS BLOBCLOB cascade;
drop sequence IF EXISTS SEQ_BLOBCLOB;

-- BEGIN SECTION 3 --------------------------------------
drop table IF EXISTS PUBLICATIONS cascade;
drop sequence IF EXISTS SEQ_PUBLICATIONS;
drop table IF EXISTS AUTHORS cascade;
drop sequence IF EXISTS SEQ_AUTHORS;
drop table IF EXISTS ADDRESS cascade;
drop sequence IF EXISTS SEQ_ADDRESS;
drop sequence IF EXISTS ZOO_SEQ_PEOPLE;
drop schema IF EXISTS ZOO cascade;
-- BEGIN SECTION 4 --------------------------------------


-- BEGIN SECTION 5 --------------------------------------
drop table IF EXISTS AUTO_ID cascade;
drop sequence IF EXISTS SEQ_AUTO_ID;

-- BEGIN SECTION 6 --------------------------------------
drop table IF EXISTS DATA_VERSION_INT cascade;
drop sequence IF EXISTS SEQ_DATA_VERSION_INT;
drop table IF EXISTS DATA_VERSION_TIMESTAMP cascade;
drop sequence IF EXISTS SEQ_DATA_VERSION_TIMESTAMP;


-- BEGIN SECTION 7 --------------------------------------
drop table IF EXISTS WRAPPER_TYPE_TABLE cascade;
drop sequence IF EXISTS SEQ_WRAPPER_TYPE_TABLE;


-- BEGIN SECTION 8 --------------------------------------
drop table IF EXISTS USER_JOB_TASK cascade;
drop sequence IF EXISTS SEQ_USER_JOB_TASK;
drop table IF EXISTS USER_JOB cascade;
drop sequence IF EXISTS SEQ_USER_JOB;
drop table IF EXISTS USER_ADDRESS cascade;
drop sequence IF EXISTS SEQ_USER_ADDRESS;
drop table IF EXISTS USER_COUNTRY cascade;
drop sequence IF EXISTS SEQ_USER_COUNTRY;
drop table IF EXISTS USERS cascade;
drop sequence IF EXISTS SEQ_USERS;

