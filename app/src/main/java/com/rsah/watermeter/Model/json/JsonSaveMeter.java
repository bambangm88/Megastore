package com.rsah.watermeter.Model.json;


import com.google.gson.annotations.SerializedName;

public class JsonSaveMeter {

    @SerializedName("uid")
    private String uid;

    @SerializedName("signature")
    private String signature;

    @SerializedName("customer_id")
    private String customer_id;

    @SerializedName("reference")
    private String reference;

    @SerializedName("customer_name")
    private String customer_name;

    @SerializedName("area")
    private String area;

    @SerializedName("address")
    private String address;

    @SerializedName("initial_meter")
    private String initial_meter;

    @SerializedName("final_meter")
    private String final_meter;

    @SerializedName("tglcreate")
    private String tglcreate;

    @SerializedName("userid")
    private String userid;

    @SerializedName("imgfile")
    private String imgfile;

    @SerializedName("imgfile_2")
    private String imgfile_2;

    @SerializedName("description")
    private String description;

    @SerializedName("period_id")
    private String period_id;

    @SerializedName("status")
    private String status;

    @SerializedName("id")
    private String id;

    public JsonSaveMeter(String uid , String signature , String customer_id , String reference , String customer_name , String area , String address , String initial_meter , String final_meter, String tglcreate , String userid , String imgfile , String imgfile_2, String desc , String period_id,String id, String status) {
        this.uid= uid;
        this.signature= signature;
        this.customer_id = customer_id;
        this.reference= reference;
        this.customer_name= customer_name;
        this.area= area;
        this.address = address;
        this.initial_meter= initial_meter;
        this.final_meter= final_meter ;
        this.tglcreate= tglcreate;
        this.userid = userid;
        this.imgfile= imgfile;
        this.imgfile_2= imgfile_2;
        this.description= desc;
        this.status= status;
        this.id= id;
        this.period_id = period_id ;
    }


}


