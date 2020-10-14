package com.rsah.watermeter.Model.json;


import com.google.gson.annotations.SerializedName;

public class Json {

    @SerializedName("uid")
    private String uid;

    @SerializedName("signature")
    private String signature;


    @SerializedName("time")
    private String time;


    public Json(String uid , String siganture ,String time) {
        this.uid= uid;
        this.signature= siganture;
        this.time= time;

    }


}


