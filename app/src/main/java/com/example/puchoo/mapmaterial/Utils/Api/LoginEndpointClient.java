package com.example.puchoo.mapmaterial.Utils.Api;

import com.example.puchoo.mapmaterial.Dto.LoginDTO;
import java.io.IOException;
import retrofit2.Call;

/**
 * Created by Nahuel SG on 14/08/2017.
 */

public class LoginEndpointClient {

    public LoginDTO login() throws IOException {
        ApiEndpointInterface apiInterface = ApiClient.getClient().create(ApiEndpointInterface.class);
        Call<LoginDTO> call = apiInterface.login();
        return call.execute().body();
    }

}
