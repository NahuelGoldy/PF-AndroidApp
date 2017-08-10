package com.example.puchoo.mapmaterial.Utils.Api;

/**
 * Created by Nahuel SG on 09/08/2017.
 */

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    //TODO cambiar el Path cuando tengamos la url de la API!
    private static String BASE_URL = "http://localhost:8080";

    public static Retrofit getClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

}