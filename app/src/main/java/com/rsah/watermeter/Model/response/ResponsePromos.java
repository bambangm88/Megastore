package com.rsah.watermeter.Model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponsePromos {

    @SerializedName("promo")
    @Expose
    private String promo;
    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }




}
