package com.rsah.watermeter.Model.json;


import com.google.gson.annotations.SerializedName;

public class JsonCustomer {

    @SerializedName("uid")
    private String uid;

    @SerializedName("signature")
    private String signature;

    @SerializedName("reference")
    private String reference;

    @SerializedName("time")
    private String time;

    @SerializedName("period_id")
    private String period_id;


    public JsonCustomer(String uid , String siganture ,  String reference , String time , String period_id) {
        this.uid= uid;
        this.signature= siganture;
        this.reference= reference;
        this.time= time;
        this.period_id= period_id;

    }


}


