-- BEGIN SECTION 1 --------------------------------------
drop table EMPLOYEE cascade constraints;
drop table TEMP_TABLE cascade constraints;

-- BEGIN SECTION 2 --------------------------------------
drop table PEOPLE cascade constraints;
drop sequence SEQ_PEOPLE;
drop table BLOBCLOB cascade constraints;
drop sequence SEQ_BLOBCLOB;

-- BEGIN SECTION 3 --------------------------------------
drop table PUBLICATIONS cascade constraints;
drop sequence SEQ_PUBLICATIONS;
drop table AUTHORS cascade constraints;
drop sequence SEQ_AUTHORS;
drop table ADDRESS cascade constraints;
drop sequence SEQ_ADDRESS;

-- BEGIN SECTION 4 --------------------------------------


-- BEGIN SECTION 5 --------------------------------------
drop table AUTO_ID cascade constraints;
drop sequence SEQ_AUTO_ID;

-- BEGIN SECTION 6 --------------------------------------
drop table DATA_VERSION_INT cascade constraints;
drop sequence SEQ_DATA_VERSION_INT;
drop table DATA_VERSION_TIMESTAMP cascade constraints;
drop sequence SEQ_DATA_VERSION_TIMESTAMP;


-- BEGIN SECTION 7 --------------------------------------
drop table WRAPPER_TYPE_TABLE cascade constraints;
drop sequence SEQ_WRAPPER_TYPE_TABLE;


-- BEGIN SECTION 8 --------------------------------------
drop table USER_JOB_TASK cascade constraints;
drop sequence SEQ_USER_JOB_TASK;
drop table USER_JOB cascade constraints;
drop sequence SEQ_USER_JOB;
drop table USER_ADDRESS cascade constraints;
drop sequence SEQ_USER_ADDRESS;
drop table USER_COUNTRY cascade constraints;
drop sequence SEQ_USER_COUNTRY;
drop table USERS cascade constraints;
drop sequence SEQ_USERS;
