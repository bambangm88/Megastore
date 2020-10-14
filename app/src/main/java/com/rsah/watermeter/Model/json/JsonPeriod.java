package com.rsah.watermeter.Model.json;


import com.google.gson.annotations.SerializedName;

public class JsonPeriod {

    @SerializedName("uid")
    private String uid;

    @SerializedName("signature")
    private String signature;

    @SerializedName("method")
    private String method;

    @SerializedName("time")
    private String time;


    public JsonPeriod(String uid , String siganture , String method , String time) {
        this.uid= uid;
        this.signature= siganture;
        this.method= method;
        this.time= time;

    }


}


