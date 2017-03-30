package com.paulshantanu.lifesaver.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.BufferUnderflowException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Shantanu Paul on 3/30/2017.
 */

public class ConnectionUtil {
    private static final String TAG = "ConnectionUtil";

    private static final String ENCODING_UTF8 = "UTF-8";

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    private String url;
    private String requestType;
    private Map<String,String> payload;

    public ConnectionUtil(String url, String requestType, Map<String,String> payload) {
        this.url = url;
        this.payload =payload;

        if(requestType.equals(METHOD_GET)||requestType.equals(METHOD_POST)){
            this.requestType = requestType;
        }
        else{
            throw new IllegalArgumentException(requestType + " is not a valid request type");
        }
    }


    public APIResponseObject getAPIResponse(){
        int responseCode = HttpURLConnection.HTTP_NOT_FOUND ;
        StringBuilder sb = new StringBuilder();

        Log.d(TAG,"getAPIResponse");
        try{
            if(requestType.equals(METHOD_GET)){
                url = url + "?" + getQueryParamString(payload);
            }

            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod(requestType);

            //urlConnection.connect();

            if(requestType.equals(METHOD_POST)) {
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, ENCODING_UTF8));
                writer.write(getQueryParamString(payload));
                writer.flush();
                writer.close();
                out.close();
            }


            urlConnection.connect();
            responseCode = urlConnection.getResponseCode();
            InputStream in;

            if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST){
                in = new BufferedInputStream(urlConnection.getErrorStream());
            }
            else {
                in = new BufferedInputStream(urlConnection.getInputStream());
            }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING_UTF8));
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            Log.d(TAG,String.valueOf(responseCode));
            Log.d(TAG,sb.toString());
        }
        catch (Exception ex){
            Log.d(TAG, ex.getLocalizedMessage());
        }
        return new APIResponseObject(responseCode,sb.toString());
    }


    private String getQueryParamString(Map<String,String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator itr = params.entrySet().iterator();
        while (itr.hasNext()) {
            if (first)
                first = false;
            else
                result.append("&");
            Map.Entry pair = (Map.Entry)itr.next();
            result.append(URLEncoder.encode(pair.getKey().toString(), ENCODING_UTF8));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue().toString(), ENCODING_UTF8));
        }
        return result.toString();
    }
}
