package com.example.puchoo.mapmaterial.Utils.Api;

import com.example.puchoo.mapmaterial.Dto.CredencialesLoginDTO;
import com.example.puchoo.mapmaterial.Dto.EstacionamientoDTO;
import com.example.puchoo.mapmaterial.Dto.LoginDTO;
import com.example.puchoo.mapmaterial.Modelo.Usuario;

import java.util.List;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
    Call<List<EstacionamientoDTO>> getAllEstacionamientos(@Header("Authorization") String token);

    @Headers({ "Accept: application/json", "Content-Type: application/json" })
    @POST("login")
    Call<LoginDTO> login(@Body CredencialesLoginDTO credenciales);

    @Headers({ "Accept: application/json", "Content-Type: application/json" })
    @POST("registro")
    Call<Response> registro(@Body Usuario user);

//    @Headers({ "Accept: application/json" })
//    @POST("login")
//    Call<LoginDTO> logout(@Header("Authorization") String token);

}
