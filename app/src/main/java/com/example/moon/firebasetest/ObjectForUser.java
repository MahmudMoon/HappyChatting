package com.example.moon.firebasetest;

public class ObjectForUser  {

    private  String Email_;
    private String UserName_;
    private String DownloadUrl;


    public ObjectForUser() {
    }

    public String getDownloadUrl() {
        return DownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        DownloadUrl = downloadUrl;
    }

    public ObjectForUser(String email_, String userName_, String downloadUrl) {
        Email_ = email_;
        UserName_ = userName_;
        DownloadUrl = downloadUrl;

    }

    public String getEmail_() {
        return Email_;
    }

    public void setEmail_(String email_) {
        Email_ = email_;
    }

    public String getUserName_() {
        return UserName_;
    }

    public void setUserName_(String userName_) {
        UserName_ = userName_;
    }
}
