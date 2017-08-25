package com.example.puchoo.mapmaterial.Utils.Validators;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.puchoo.mapmaterial.Dto.CredencialesLoginDTO;
import com.example.puchoo.mapmaterial.Utils.Api.AuthenticationEndpointClient;
import com.example.puchoo.mapmaterial.VistasAndControllers.Activities.LoginActivity;
import com.example.puchoo.mapmaterial.VistasAndControllers.SesionManager;

import java.io.IOException;


/**
 * Created by Puchoo on 08/08/2017.
 */

public class ValidadorLogin extends AsyncTask<Void,Void,Void> {
    private final LoginActivity loginActivity;
    private ProgressDialog progress;
    private String email,pass;

    Boolean valido = false;

    public ValidadorLogin(ProgressDialog progress, String email, String pass, LoginActivity loginActivity){
        this.progress = progress;
        this.email = email;
        this.pass = pass;
        this.loginActivity = loginActivity;
    }

    @Override
    protected void onPreExecute(){
        progress.show();

        //ejecuta antes de arrancar
    }

    @Override
    protected Void doInBackground(Void... params) {
         valido = true;

        try {
            new AuthenticationEndpointClient().login(new CredencialesLoginDTO(email, pass));
            if(SesionManager.getInstance().getTokenUsuario() == null){
                valido = false;
            }
        } catch (IOException e) {
            valido = false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused){
        progress.dismiss();
        if(valido){
            loginActivity.onLoginSuccess();
        } else {
            loginActivity.onLoginFailed();
        }
        //ejecuta cuando termina
    }


}
