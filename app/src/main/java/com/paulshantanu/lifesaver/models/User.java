package com.paulshantanu.lifesaver.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shantanu Paul on 3/20/2017.
 */

public class User implements Parcelable{

    private String name;
    private String email;
    private String bloodGroup;
    private Double[] location;

    public User(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodgroup() {
        return bloodGroup;
    }

    public void setBloodgroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Double[] getLocation() { return location; }

    public void setLocation(Double[] location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.bloodGroup);
        dest.writeArray(this.location);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.bloodGroup = in.readString();
        this.location = (Double[]) in.readArray(Double[].class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
