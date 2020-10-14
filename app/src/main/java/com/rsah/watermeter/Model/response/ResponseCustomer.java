package com.rsah.watermeter.Model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCustomer {


    @SerializedName("id")
    private String id;

    @SerializedName("customer_id")
    private String customer_id;

    @SerializedName("customer_name")
    private String customer_name;

    @SerializedName("reference")
    private String reference;

    @SerializedName("area")
    private String area;

    @SerializedName("address")
    private String address;



    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private String status;

    @SerializedName("tglcreate")
    private String tglcreate;

    @SerializedName("input_by")
    private String input_by;

    @SerializedName("createby")
    private String createby;

    @SerializedName("input_date")
    private String input_date;

    @SerializedName("update_by")
    private String update_by;

    @SerializedName("update_date")
    private String update_date;

    @SerializedName("last_login")
    private String last_login;

    @SerializedName("image")
    private String image;



    @SerializedName("image_2")
    private String image_2;

    @SerializedName("date")
    private String date;




    @SerializedName("user_id")
    private String user_id;

    @SerializedName("status_sync")
    private String status_sync;


    @SerializedName("period_id")
    private String period_id;


    @SerializedName("period")
    private String period;


    @SerializedName("prev_initial_meter")
    private String prev_initial_meter;

    @SerializedName("prev_final_meter")
    private String prev_final_meter;


    @SerializedName("prev_date_scan")
    private String prev_date_scan;

    @SerializedName("initial_meter")
    private String initial_meter;

    @SerializedName("final_meter")
    private String final_meter;

    @SerializedName("count_meter")
    private String count_meter;

    @SerializedName("status_server")
    private String status_server;


    public String getCount_meter() {
        return count_meter;
    }

    public void setCount_meter(String count_meter) {
        this.count_meter = count_meter;
    }


    public String getInitial_meter() {
        return initial_meter;
    }

    public void setInitial_meter(String initial_meter) {
        this.initial_meter = initial_meter;
    }

    public String getFinal_meter() {
        return final_meter;
    }

    public void setFinal_meter(String final_meter) {
        this.final_meter = final_meter;
    }



    public String getStatus_server() {
        return status_server;
    }

    public void setStatus_server(String status_server) {
        this.status_server = status_server;
    }



    public String getPrev_final_meter() {
        return prev_final_meter;
    }

    public void setPrev_final_meter(String prev_final_meter) {
        this.prev_final_meter = prev_final_meter;
    }

    public String getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(String period_id) {
        this.period_id = period_id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }


    public String getPrev_initial_meter() {
        return prev_initial_meter;
    }

    public void setPrev_initial_meter(String prev_initial_meter) {
        this.prev_initial_meter = prev_initial_meter;
    }




    public String getStatus_sync() {
        return status_sync;
    }

    public void setStatus_sync(String status_sync) {
        this.status_sync = status_sync;
    }




    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTglcreate() {
        return tglcreate;
    }

    public void setTglcreate(String tglcreate) {
        this.tglcreate = tglcreate;
    }

    public String getInput_by() {
        return input_by;
    }

    public void setInput_by(String input_by) {
        this.input_by = input_by;
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public String getInput_date() {
        return input_date;
    }

    public void setInput_date(String input_date) {
        this.input_date = input_date;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getImage_2() {
        return image_2;
    }

    public void setImage_2(String image_2) {
        this.image_2 = image_2;
    }
    public String getPrev_date_scan() {
        return prev_date_scan;
    }

    public void setPrev_date_scan(String prev_date_scan) {
        this.prev_date_scan = prev_date_scan;
    }




}
