package com.example.puchoo.mapmaterial.Dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nahuel SG on 14/08/2017.
 */

public class LoginDTO {

    @SerializedName("token")
    private String token;
    private UsuarioDTO user;

    public UsuarioDTO getUser() {
        return user;
    }

    public void setUser(UsuarioDTO user) {
        this.user = user;
    }

    public LoginDTO(){}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
