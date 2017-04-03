package com.paulshantanu.lifesaver.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paulshantanu.lifesaver.database.DBConstants;
import com.paulshantanu.lifesaver.models.User;
import com.paulshantanu.lifesaver.util.APIResponseObject;
import com.paulshantanu.lifesaver.util.ConnectionUtil;
import com.paulshantanu.lifesaver.util.UserProvider;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shantanu Paul on 3/23/2017.
 */

public class UserService extends IntentService {

    private static final String TAG = "UserService";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.JDJhJDA1JHF0SE9LQ1ZyRzg1NGVzUTFrSm85c3UuLnNFeFhZTFpoRlAuQ1Z3TGxqYkZHMzJoTnEzelNP." +
            "pQlxqfi2eLoDpPFb2RFYSk5Vu8WBaoIUKBkJ9jlDcVs"; //TODO Remove hardcoded token
    private String url = "https://lifesaver-api.herokuapp.com/auth/nearbyUsers";
    public static final String BROADCAST_ACTION = "BroadcastAction";

    public UserService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        HashMap<String,String> map = new HashMap<>();
        map.put("email","shantanupaul@live.com");
        map.put("token",TOKEN);
        map.put("maxDistance","100000");

        APIResponseObject responseObject = new ConnectionUtil(url,ConnectionUtil.METHOD_GET,map)
                .getAPIResponse();

        if(responseObject.getResponseCode() == HttpURLConnection.HTTP_OK){
            parseJsonResponse(responseObject.getResponse());
        }
        else{
            //TODO - Error handling
        }
    }

    private void parseJsonResponse(String response) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<User>>(){}.getType();
        List<User> users = gson.fromJson(response,listType);
        Log.d(TAG,"Parse");
        saveToDb(users);
    }

    private void saveToDb(List<User> users) {
        getContentResolver().delete(UserProvider.CONTENT_URI,null,null);
        ContentValues values = new ContentValues();
        for(User user : users) {
            values.put(DBConstants.COLUMN_NAME, user.getName());
            values.put(DBConstants.COLUMN_EMAIL,user.getEmail());
            values.put(DBConstants.COLUMN_BLOODGROUP, user.getBloodgroup());
            values.put(DBConstants.COLUMN_LOCATION_LONGITUDE, user.getLocation()[0]);
            values.put(DBConstants.COLUMN_LOCATION_LATITUDE, user.getLocation()[1]);
            getContentResolver().insert(UserProvider.CONTENT_URI,values);
        }
        Log.d(TAG,"saveToDB");
        Intent broadcastIntent = new Intent(BROADCAST_ACTION);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(broadcastIntent);
    }




}
