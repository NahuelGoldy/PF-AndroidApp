package com.example.puchoo.mapmaterial.Utils.Api;

import android.util.Log;

import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nahuel SG on 09/08/2017.
 */

public class EstacionamientoEndpointClient {

    ArrayList<Estacionamiento> list;

    //TODO aca habria que ver si devolver la entidad Estacionamiento o EstacionamientoDTO !!!
    // ver como se maneja eso con la deserializacion del json y la info

    public Estacionamiento getEstacionamientoById(int id) throws Exception {
        ApiEndpointInterface apiInterface = ApiClient.getClient().create(ApiEndpointInterface.class);
        Call<Estacionamiento> estacionamiento = apiInterface.getEstacionamientoById(id);
        return estacionamiento.execute().body();
    }

    public List<Estacionamiento> getAllEstacionamientos() throws Exception {

       list = new ArrayList<>();

        ApiEndpointInterface apiInterface = ApiClient.getClient().create(ApiEndpointInterface.class);
        Call<List<Estacionamiento>> call = apiInterface.getAllEstacionamientos();
        call.enqueue(new Callback<List<Estacionamiento>>() {
                @Override
                public void onResponse(Call<List<Estacionamiento>> asyncCall, Response<List<Estacionamiento>> response) {
                    try{
                        list.addAll(response.body());
                    }
                    catch(Exception e){
                        Log.d("onResponse", "Hubo un error al hacer el Request a la API");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<List<Estacionamiento>> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });
        return list;
    }
}
