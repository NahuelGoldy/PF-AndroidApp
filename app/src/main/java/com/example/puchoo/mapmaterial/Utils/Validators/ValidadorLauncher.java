package com.example.puchoo.mapmaterial.Utils.Validators;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.puchoo.mapmaterial.Modelo.Usuario;
import com.example.puchoo.mapmaterial.VistasAndControllers.Activities.RegistroActivity;
import com.example.puchoo.mapmaterial.VistasAndControllers.LaunchManager;

/**
 * Created by Puchoo on 10/08/2017.
 */

public class ValidadorLauncher extends AsyncTask<Void,Void,Void> {

    private final LaunchManager launchActivity;
    private ProgressDialog progress;
    private Boolean nuevoUser;

    public ValidadorLauncher( LaunchManager launchActivity){
        this.launchActivity = launchActivity;

    }

    @Override
    protected void onPreExecute(){
        //progress.show();

        //ejecuta antes de arrancar
    }

    @Override
    protected Void doInBackground(Void... params) {

        //TODO BUSCAR SI EXISTEN PARAMETROS GUARDADOS PARA LOGEO
        nuevoUser = true;
        //TODO EJECUAR EL loginSucces o loginFailed

        return null;
    }

    @Override
    protected void onPostExecute(Void unused){
        //progress.dismiss();

        launchActivity.setUser_nuevo(nuevoUser);
        launchActivity.launch();
        //ejecuta cuando termina
    }


}
