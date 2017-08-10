package com.example.puchoo.mapmaterial.Utils.Api;

import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Nahuel SG on 09/08/2017.
 */

public class EstacionamientoEndpointClient {

    //TODO aca habria que ver si devolver la entidad Estacionamiento o EstacionamientoDTO !!!
    // ver como se maneja eso con la deserializacion del json y la info

    public List<Estacionamiento> getAllEstacionamientos() throws Exception {
        ApiEndpointInterface apiInterface = ApiClient.getClient().create(ApiEndpointInterface.class);
        Call<List<Estacionamiento>> estacionamientos = apiInterface.getAllEstacionamientos();
        return estacionamientos.execute().body();
    }

    public Estacionamiento getEstacionamientoById(int id) throws Exception {
        ApiEndpointInterface apiInterface = ApiClient.getClient().create(ApiEndpointInterface.class);
        Call<Estacionamiento> estacionamiento = apiInterface.getEstacionamientoById(id);
        return estacionamiento.execute().body();
    }
}
