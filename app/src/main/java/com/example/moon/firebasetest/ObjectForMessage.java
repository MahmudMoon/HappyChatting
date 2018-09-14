package com.example.moon.firebasetest;

public class ObjectForMessage  {
   private String Message;
    private  String UserName;
   private String url;

    public ObjectForMessage() {
    }

    public ObjectForMessage(String message, String userName, String url) {
        Message = message;
        UserName = userName;
        this.url = url;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
