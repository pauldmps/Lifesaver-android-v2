package com.paulshantanu.lifesaver.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paulshantanu.lifesaver.models.User;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Shantanu Paul on 3/23/2017.
 */

public class UserService extends IntentService {

    private static final String TAG = "UserService";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.JDJhJDA1JHF0SE9LQ1ZyRzg1NGVzUTFrSm85c3UuLnNFeFhZTFpoRlAuQ1Z3TGxqYkZHMzJoTnEzelNP." +
            "pQlxqfi2eLoDpPFb2RFYSk5Vu8WBaoIUKBkJ9jlDcVs"; //TODO Remove hardcoded token
    private static String url = "https://lifesaver-paulshantanu.rhcloud.com/auth/nearbyUsers";

    public UserService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        HashMap<String,String> map = new HashMap<>();
        map.put("email","shantanupaul@live.com");
        map.put("token",TOKEN);

        try {
            String postData = getPostDataString(map);

            String method = "GET";
            if("GET".equals(method)){
                url = url + "?" + postData;
            }

            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod(method);

            urlConnection.connect();

            StringBuilder sb = new StringBuilder();

            if("POST".equals(method)) {
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                out.close();
            }


            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                Log.d(TAG,sb.toString());


                parseJsonResponse(sb.toString());

            }
            Log.d(TAG,String.valueOf(responseCode));


        }
        catch (Exception ex){
            Log.e(TAG, ex.getLocalizedMessage());
        }

    }

    private void parseJsonResponse(String response) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<User>>(){}.getType();
        List<User> users = gson.fromJson(response,listType);

    }


    private String getPostDataString(HashMap<String,String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator itr = params.entrySet().iterator();
        while (itr.hasNext()) {
            if (first)
                first = false;
            else
                result.append("&");
            Map.Entry pair = (Map.Entry)itr.next();
            result.append(URLEncoder.encode(pair.getKey().toString(),"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue().toString(), "UTF-8"));
        }
        return result.toString();
    }
}