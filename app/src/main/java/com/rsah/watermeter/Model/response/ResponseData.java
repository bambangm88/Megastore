package com.rsah.watermeter.Model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseData {

    @SerializedName("data")
    @Expose
    private List<ResponseCustomer> data = null;

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;


    public List<ResponseCustomer> getData() {
        return data;
    }

    public void setData(List<ResponseCustomer> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }







}
