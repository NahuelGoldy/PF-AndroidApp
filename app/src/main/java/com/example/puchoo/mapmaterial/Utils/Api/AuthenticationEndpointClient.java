package com.example.puchoo.mapmaterial.Utils.Api;

import com.example.puchoo.mapmaterial.Dto.CredencialesLoginDTO;
import com.example.puchoo.mapmaterial.Dto.LoginDTO;
import com.example.puchoo.mapmaterial.Dto.ResultadoRegistroDTO;
import com.example.puchoo.mapmaterial.Modelo.Usuario;
import com.example.puchoo.mapmaterial.VistasAndControllers.SesionManager;

import java.io.IOException;
import retrofit2.Call;

public class AuthenticationEndpointClient {

    public void login(CredencialesLoginDTO credenciales) throws IOException {
        ApiEndpointInterface apiInterface = ApiClient.getClient().create(ApiEndpointInterface.class);
        Call<LoginDTO> call = apiInterface.login(credenciales);

        //ejecuto llamada a la API
        LoginDTO login = call.execute().body();
        //seteo el token proveniente en la respuesta
        if(login != null){
            SesionManager.getInstance().setTokenUsuario(login.getToken());}

        return;
    }

    public ResultadoRegistroDTO registro(Usuario user) throws IOException {
        ApiEndpointInterface apiInterface = ApiClient.getClient().create(ApiEndpointInterface.class);
        Call<ResultadoRegistroDTO> call = apiInterface.registro(user);

        //ejecuto llamada a la API
        ResultadoRegistroDTO result = call.execute().body();

        //TODO hacer algo con el resultado?
        return result;
    }

}
