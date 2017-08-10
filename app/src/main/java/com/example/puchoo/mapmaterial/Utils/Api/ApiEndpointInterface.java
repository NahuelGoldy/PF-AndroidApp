package com.example.puchoo.mapmaterial.Utils.Api;

import com.example.puchoo.mapmaterial.Dto.EstacionamientoDTO;
import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by Nahuel SG on 09/08/2017.
 */

public interface ApiEndpointInterface {

    @Headers({ "Accept: application/json" })
    @GET("parques/{id}")
    Call<EstacionamientoDTO> getEstacionamientoById(@Path("id") int parkId);

    @Headers({ "Accept: application/json" })
    @GET("parques/all")
    Call<List<EstacionamientoDTO>> getAllEstacionamientos();

//    Ejemplo de como seria un POST para crear una nueva entidad (Reserva, Usuario, lo que sea)
//    @POST("usuarios")
//    Call<User> createUser(@Body Usuario usuario);


}
