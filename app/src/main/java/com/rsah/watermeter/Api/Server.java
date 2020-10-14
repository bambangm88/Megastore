package com.rsah.watermeter.Api;



import static com.rsah.watermeter.Util.Utility.BASE_URL_API;

public class Server {
    public static ApiService getAPIService() {
        return Client.getClient(BASE_URL_API).create(ApiService.class);
    }





}
