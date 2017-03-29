package com.paulshantanu.lifesaver.database;

/**
 * Created by Shantanu Paul on 3/19/2017.
 */

public class DBConstants {

    public static final String DBNAME = "MainDatabase.db";
    public static final int DBVERSION = 1;

    public static final String TABLE_NAME = "USERS";

    public static final String COMMA_SEP = ", ";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NAME_TYPE = "TEXT";

    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_EMAIL_TYPE = "TEXT";

    public static final String COLUMN_BLOODGROUP = "bloodgroup";
    public static final String COLUMN_BLOODGROUP_TYPE = "TEXT";

    public static final String COLUMN_LOCATION_LATITUDE = "latitude";
    public static final String COLUMN_LOCATION_LATITUDE_TYPE = "DOUBLE";

    public static final String COLUMN_LOCATION_LONGITUDE = "longitude";
    public static final String COLUMN_LOCATION_LONGITUDE_TYPE = "DOUBLE";

    public static final String COLUMN_TIMESTAMP_ADDED = "timestamp";
    public static final String COLUMN_TIMESTAMP_ADDED_TYPE = "DATETIME DEFAULT CURRENT_TIMESTAMP";

    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL,
            COLUMN_BLOODGROUP, COLUMN_LOCATION_LATITUDE, COLUMN_LOCATION_LONGITUDE, COLUMN_TIMESTAMP_ADDED};

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " " + COLUMN_ID_TYPE + COMMA_SEP +
            COLUMN_NAME + " " + COLUMN_NAME_TYPE + COMMA_SEP +
            COLUMN_EMAIL + " " + COLUMN_EMAIL_TYPE + COMMA_SEP +
            COLUMN_BLOODGROUP + " " + COLUMN_BLOODGROUP_TYPE + COMMA_SEP +
            COLUMN_LOCATION_LATITUDE + " " + COLUMN_LOCATION_LATITUDE_TYPE + COMMA_SEP +
            COLUMN_LOCATION_LONGITUDE + " " + COLUMN_LOCATION_LONGITUDE_TYPE + COMMA_SEP +
            COLUMN_TIMESTAMP_ADDED + " " + COLUMN_TIMESTAMP_ADDED_TYPE +
            ")";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //Private constructor to prevent instantiation
    private DBConstants(){}


}
