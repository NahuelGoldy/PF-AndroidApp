package com.example.puchoo.mapmaterial.Utils.Api;

import com.example.puchoo.mapmaterial.Dto.CredencialesLoginDTO;
import com.example.puchoo.mapmaterial.Dto.LoginDTO;
import com.example.puchoo.mapmaterial.VistasAndControllers.SesionManager;

import java.io.IOException;
import retrofit2.Call;

public class LoginEndpointClient {

    public void login(CredencialesLoginDTO credenciales) throws IOException {
        ApiEndpointInterface apiInterface = ApiClient.getClient().create(ApiEndpointInterface.class);
        Call<LoginDTO> call = apiInterface.login(credenciales);

        //ejecuto llamada a la API
        LoginDTO login = call.execute().body();
        //seteo el token proveniente en la respuesta
        SesionManager.getInstance().setTokenUsuario(login.getToken());

        return;
    }

}
