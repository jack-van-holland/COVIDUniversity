package com.example.coviduniversity;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.storage.StorageReference;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mPicName;
    private String mUserName;

    public Upload() {
        //empty constructor needed
    }
    public Upload(String name, String imageUrl, String picName, String userName) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        mName = name;
        mImageUrl = imageUrl;
        mPicName = picName;
        mUserName = userName;
    }
    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
    public String getUserName() { return mUserName; }
    public void setUserName(String name) {
        mUserName = name;
    }
    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
    public String getPicName() {
        return mPicName;
    }
    public void setPicName(String name) {
        mPicName = name;
    }

}
