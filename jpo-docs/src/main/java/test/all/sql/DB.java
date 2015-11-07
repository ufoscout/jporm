package test.all.sql;

/**
 * Created by ufo on 25/07/15.
 */
public class DB {

    public static String CREATE_USER_TABLE = "CREATE TABLE USERS\n" + "(ID INTEGER NOT NULL,\n" + "FIRST_NAME VARCHAR(25) NOT NULL,\n"
            + "LAST_NAME VARCHAR(25) NOT NULL,\n" + "AGE INTEGER,\n" + "PRIMARY KEY (ID)) ";

    public static String CREATE_USER_SEQUENCE = "CREATE SEQUENCE USERS_SEQUENCE";

    public static String CREATE_JOB_TABLE = "CREATE TABLE USERS\n" + "(ID VARCHAR(50) NOT NULL,\n" + "CAP VARCHAR(50),\n" + "CITY VARCHAR(50),\n"
            + "NUMBER INTEGER,\n" + "STREET VARCHAR(250),\n" + "USER_ID INTEGER,\n" + "PRIMARY KEY (ID)),\n " + "FOREIGN KEY (USER_ID) REFERENCES USERS(ID))";
}
