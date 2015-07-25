package test.all.sql;

/**
 * Created by ufo on 25/07/15.
 */
public class DB {

    public static String CREATE_USER_TABLE =
            "CREATE TABLE USERS\n" +
            "(ID INTEGER NOT NULL,\n" +
            "FIRST_NAME VARCHAR(25) NOT NULL,\n" +
            "LAST_NAME VARCHAR(25) NOT NULL,\n" +
            "PRIMARY KEY (ID)) ";

    public static String CREATE_USER_SEQUENCE =
            "CREATE SEQUENCE USERS_SEQUENCE";


}
