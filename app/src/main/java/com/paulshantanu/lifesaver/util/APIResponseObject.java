package com.paulshantanu.lifesaver.util;

/**
 * Created by Shantanu Paul on 3/29/2017.
 */

public class APIResponseObject {

    private int responseCode;
    private String response;

    APIResponseObject(int responseCode, String response){
        this.responseCode = responseCode;
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
