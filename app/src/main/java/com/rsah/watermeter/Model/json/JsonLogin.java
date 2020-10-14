package com.rsah.watermeter.Model.json;


import com.google.gson.annotations.SerializedName;

public class JsonLogin {

    @SerializedName("uid")
    private String uid;

    @SerializedName("signature")
    private String signature;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;


    public JsonLogin(String uid , String siganture , String username , String password) {
        this.uid= uid;
        this.signature= siganture;
        this.username= username;
        this.password= password;
    }


}


