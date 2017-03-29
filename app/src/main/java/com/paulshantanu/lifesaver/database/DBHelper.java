package com.paulshantanu.lifesaver.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Shantanu Paul on 3/19/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    private DBHelper(Context context) {
        super(context, DBConstants.DBNAME, null, DBConstants.DBVERSION);
    }

    public static DBHelper getInstance(Context context){
        if(instance == null){
            instance = new DBHelper(context);
        }

        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DBConstants.DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
