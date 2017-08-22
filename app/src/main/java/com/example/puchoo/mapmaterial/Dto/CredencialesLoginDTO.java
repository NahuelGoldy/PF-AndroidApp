
package com.example.puchoo.mapmaterial.Dto;


import com.google.gson.annotations.SerializedName;

public class CredencialesLoginDTO {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public CredencialesLoginDTO(){}

    public CredencialesLoginDTO(String mail, String pass){
        email = mail;
        password = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
