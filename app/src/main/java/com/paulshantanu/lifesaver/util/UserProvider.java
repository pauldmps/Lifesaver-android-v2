package com.paulshantanu.lifesaver.util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.paulshantanu.lifesaver.database.DBConstants;
import com.paulshantanu.lifesaver.database.DBHelper;

/**
 * Created by Shantanu Paul on 3/20/2017.
 */

public class UserProvider extends ContentProvider {

    private static final String TAG = "UserProvider";

    private DBHelper helper;
    private SQLiteDatabase database;

    private static final String AUTHORITY = "com.paulshantanu.lifesaver.UserListProvider";
    public static final String USERS_BASE_PATH = "users";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + USERS_BASE_PATH);

    private static final int USERS = 1;
    private static final int USER_ID = 2;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        URI_MATCHER.addURI(AUTHORITY, USERS_BASE_PATH, USERS);
        URI_MATCHER.addURI(AUTHORITY,USERS_BASE_PATH + "/#", USER_ID);
    }

    @Override
    public boolean onCreate() {
        helper = DBHelper.getInstance(getContext());
        database = helper.getWritableDatabase();
        Log.d(TAG,"OnCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor = database
                .query(DBConstants.TABLE_NAME, DBConstants.ALL_COLUMNS,
                        selection,null,null,null,
                        DBConstants.COLUMN_TIMESTAMP_ADDED + " DESC");
        Log.d(TAG,String.valueOf(cursor.getCount()));
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long id = database.insert(DBConstants.TABLE_NAME,null,contentValues);
        return Uri.parse(USERS_BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete(DBConstants.TABLE_NAME,selection,selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update(DBConstants.TABLE_NAME,contentValues,selection,selectionArgs);
    }


}
