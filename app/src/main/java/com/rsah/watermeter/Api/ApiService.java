package com.rsah.watermeter.Api;









import com.rsah.watermeter.Model.json.Json;
import com.rsah.watermeter.Model.json.JsonCustomer;
import com.rsah.watermeter.Model.json.JsonLogin;
import com.rsah.watermeter.Model.json.JsonPeriod;
import com.rsah.watermeter.Model.json.JsonSaveMeter;
import com.rsah.watermeter.Model.response.ResponseData;
import com.rsah.watermeter.Model.response.ResponseDataDescription;
import com.rsah.watermeter.Model.response.ResponseLogin;
import com.rsah.watermeter.Model.response.ResponseRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface ApiService {



    @Headers("Content-Type: application/json")
    @POST("login")
    Call<ResponseLogin> requestLogin(@Body JsonLogin body);


    @Headers("Content-Type: application/json")
    @POST("report")
    Call<ResponseData> requestGetCustomer(@Body JsonCustomer body);

    @Headers("Content-Type: application/json")
    @POST("savemeter")
    Call<ResponseRequest> requestSaveMeter(@Body JsonSaveMeter body);

    @Headers("Content-Type: application/json")
    @POST("description")
    Call<ResponseDataDescription> requestGetDescription(@Body Json body);

    @Headers("Content-Type: application/json")
    @POST("period")
    Call<ResponseData> requestGetPeriod(@Body JsonPeriod body);


}
