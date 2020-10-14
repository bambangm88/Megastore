package com.rsah.watermeter.Model.response;

import com.google.gson.annotations.SerializedName;

public class ResponseDescription {


    @SerializedName("id_description")
    private String id_description;


    @SerializedName("description")
    private String description;


    public String getId_description() {
        return id_description;
    }

    public void setId_description(String id_description) {
        this.id_description = id_description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }





}
