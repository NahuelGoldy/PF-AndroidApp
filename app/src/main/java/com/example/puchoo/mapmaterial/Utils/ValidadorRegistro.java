package com.example.puchoo.mapmaterial.Utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.puchoo.mapmaterial.Modelo.Usuario;
import com.example.puchoo.mapmaterial.VistasAndControllers.RegistroActivity;

/**
 * Created by Puchoo on 08/08/2017.
 */

public class ValidadorRegistro extends AsyncTask<Void,Void,Void> {

    private final RegistroActivity registroActivity;
    private ProgressDialog progress;
    private Usuario nuevoUsuario;

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
        Boolean valido = true;

        //TODO VALIDAR QUE SEA PEUDA CREAR EL USER

        //TODO EJECUAR EL singUpSucces o singUpFailed del registro

        return null;
    }

    @Override
    protected void onPostExecute(Void unused){
        progress.dismiss();

        //ejecuta cuando termina
    }


}
