package com.example.puchoo.mapmaterial.Utils.Api;

/**
 * Created by Nahuel SG on 09/08/2017.
 */

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    private static String BASE_URL = "http://dondeestacionop.cloudapp.net:8080";



    public static Retrofit getClient() {
         OkHttpClient okHttpClient = new OkHttpClient.Builder()
                 .readTimeout(60, TimeUnit.SECONDS)
                 .connectTimeout(60,TimeUnit.SECONDS)
                 .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

}
