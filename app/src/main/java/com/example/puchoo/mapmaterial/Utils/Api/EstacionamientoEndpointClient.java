package com.example.puchoo.mapmaterial.Utils.Api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.puchoo.mapmaterial.Dto.EstacionamientoDTO;
import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nahuel SG on 09/08/2017.
 */

public class EstacionamientoEndpointClient {

    ArrayList<EstacionamientoDTO> list;

    //TODO aca habria que ver si devolver la entidad Estacionamiento o EstacionamientoDTO !!!
    // ver como se maneja eso con la deserializacion del json y la info

    public EstacionamientoDTO getEstacionamientoById(int id) throws Exception {
        ApiEndpointInterface apiInterface = ApiClient.getClient().create(ApiEndpointInterface.class);
        Call<EstacionamientoDTO> estacionamiento = apiInterface.getEstacionamientoById(id);
        return estacionamiento.execute().body();
    }

    public  List<Estacionamiento> getAllEstacionamientos() throws IOException {
        List<Estacionamiento> listEstacionamiento = new ArrayList<>();
        String token = null;
        //TODO setear el token

        ApiEndpointInterface apiInterface = ApiClient.getClient().create(ApiEndpointInterface.class);
        Call<List<EstacionamientoDTO>> call = apiInterface.getAllEstacionamientos(token);
        list = (ArrayList<EstacionamientoDTO>) call.execute().body();

        for (EstacionamientoDTO estacionamientoDTO : list){
            listEstacionamiento.add(estacionamientoDTO.toEstacionamiento());
        }

        return listEstacionamiento;
    }

    public List<Estacionamiento> getAllEstacionamientosAsync() throws Exception {

       list = new ArrayList<>();
        String token = null;
        //TODO setear el token

        ApiEndpointInterface apiInterface = ApiClient.getClient().create(ApiEndpointInterface.class);
        Call<List<EstacionamientoDTO>> call = apiInterface.getAllEstacionamientos(token);
        call.enqueue(new Callback<List<EstacionamientoDTO>>() {
                @Override
                public void onResponse(Call<List<EstacionamientoDTO>> asyncCall, Response<List<EstacionamientoDTO>> response) {
                    try{
                        list.addAll(response.body());
                    }
                    catch(Exception e){
                        Log.d("onResponse", "Hubo un error al hacer el Request a la API");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<List<EstacionamientoDTO>> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });
        List<Estacionamiento> listEstacionamiento = new ArrayList<Estacionamiento>();

        for (EstacionamientoDTO estacionamientoDTO : list){

            listEstacionamiento.add(estacionamientoDTO.toEstacionamiento());

        }

        return listEstacionamiento;
    }
}
