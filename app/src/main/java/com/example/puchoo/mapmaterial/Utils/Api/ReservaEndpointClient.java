package com.example.puchoo.mapmaterial.Utils.Api;

import com.example.puchoo.mapmaterial.Dto.DatosReservaDTO;
import com.example.puchoo.mapmaterial.Dto.ReservaDTO;
import com.example.puchoo.mapmaterial.VistasAndControllers.SesionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Nahuel SG on 11/09/2017.
 */

public class ReservaEndpointClient {

    ArrayList<ReservaDTO> list;

    public ReservaDTO crearReserva(DatosReservaDTO datosReserva) throws IOException{

        String token = "Bearer " + SesionManager.getInstance().getTokenUsuario();
        ApiEndpointInterface apiInterface = ApiClient.getClient().create(ApiEndpointInterface.class);
        Call<ReservaDTO> call = apiInterface.crearReserva(token, datosReserva.getIdParqueEstacionamiento(), datosReserva);

        ReservaDTO result = call.execute().body();

        //TODO hacer algo con el resultado?
        return result;
    }

    public ArrayList<ReservaDTO> getAllReservas(int id_parque) throws IOException {

        String token = "Bearer " + SesionManager.getInstance().getTokenUsuario();
        ApiEndpointInterface apiInterface = ApiClient.getClient().create(ApiEndpointInterface.class);
        Call<List<ReservaDTO>> call = apiInterface.getAllReservas(token, id_parque);

        return (ArrayList<ReservaDTO>) call.execute().body();
    }
}
