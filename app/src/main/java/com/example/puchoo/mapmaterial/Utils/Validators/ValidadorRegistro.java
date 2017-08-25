package com.example.puchoo.mapmaterial.Utils.Validators;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.puchoo.mapmaterial.Dto.ResultadoRegistroDTO;
import com.example.puchoo.mapmaterial.Modelo.Usuario;
import com.example.puchoo.mapmaterial.Utils.Api.AuthenticationEndpointClient;
import com.example.puchoo.mapmaterial.VistasAndControllers.Activities.RegistroActivity;

import java.io.IOException;

/**
 * Created by Puchoo on 08/08/2017.
 */

public class ValidadorRegistro extends AsyncTask<Void,Void,Void> {

    private final RegistroActivity registroActivity;
    private ProgressDialog progress;
    private Usuario nuevoUsuario;
    private ResultadoRegistroDTO resultadoRegistroDTO = null;

    public ValidadorRegistro(ProgressDialog progress,Usuario nuevoUsuario, RegistroActivity registroActivity){
        this.progress = progress;
        this.nuevoUsuario = nuevoUsuario;
        this.registroActivity = registroActivity;
    }

    @Override
    protected void onPreExecute(){
        progress.show();

        //ejecuta antes de arrancar
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            resultadoRegistroDTO = new AuthenticationEndpointClient().registro(nuevoUsuario);
        } catch (IOException e) {
            registroActivity.onSignupFailed();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused){
        progress.dismiss();
        if(resultadoRegistroDTO == null){
            registroActivity.onSignupFailed();
        }else {
            registroActivity.onSignupSuccess();
        }
        //ejecuta cuando termina
    }


}
