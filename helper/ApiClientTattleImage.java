package com.tattleup.app.tattleup.helper;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 12/20/2017.
 */

public class ApiClientTattleImage {

    public static final String BaseUrl = "\n" +
            "http://api.tattleup.leoinfotech.in/index.php/api/version1/setTattleImage/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient()
    {
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
