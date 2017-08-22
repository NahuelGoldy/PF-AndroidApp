package com.example.puchoo.mapmaterial.Utils.Validators;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.puchoo.mapmaterial.Dao.EstacionamientoDAO;
import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;
import com.example.puchoo.mapmaterial.Utils.Api.EstacionamientoEndpointClient;
import com.example.puchoo.mapmaterial.VistasAndControllers.Activities.LoginActivity;
import com.example.puchoo.mapmaterial.VistasAndControllers.Activities.MainActivity;
import com.example.puchoo.mapmaterial.VistasAndControllers.Fragments.CardContentFragment;
import com.example.puchoo.mapmaterial.VistasAndControllers.Fragments.ListContentFragment;
import com.example.puchoo.mapmaterial.VistasAndControllers.SesionManager;

import java.util.ArrayList;

/**
 * Created by Puchoo on 10/08/2017.
 */

public class ValidadorPedidoEstacionamiento extends AsyncTask<Void,Void,Void> {

    private Activity loginActivity;
    private ProgressDialog progress;
    private static final String TAG = "ValidadorPedEst";
    private ArrayList<Estacionamiento> listaEstacionamiento;

    public ValidadorPedidoEstacionamiento(Activity loginActivity, ProgressDialog progressDialog){
        this.loginActivity = loginActivity;
        this.progress= progressDialog;
    }

    @Override
    protected void onPreExecute(){
        progress.show();

        //ejecuta antes de arrancar
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {

            if (!SesionManager.getInstance().getActualizarBD()) {
                listaEstacionamiento = EstacionamientoDAO.getInstance().listarEstacionamientos(loginActivity);

            } else{
                /** Inicializa los estacionamientos desde la api**/
                EstacionamientoDAO.getInstance().inicializarListaEstacionamientos(loginActivity);
                listaEstacionamiento = EstacionamientoDAO.getInstance().listarEstacionamientos(loginActivity);
            }
            /** Si no hay nada en local pide a la api**/
            if(listaEstacionamiento.isEmpty()){
                EstacionamientoDAO.getInstance().inicializarListaEstacionamientos(loginActivity);
                listaEstacionamiento = EstacionamientoDAO.getInstance().listarEstacionamientos(loginActivity);
            }

            Intent intent = new Intent(loginActivity.getApplicationContext(), MainActivity.class);
            loginActivity.startActivity(intent);

        } catch (Exception e) {

            String msgLog = "Hubo un error al traer los estacionamientos.";
            Log.v(TAG,msgLog);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void unused){
        //ejecuta cuando termina
        if (listaEstacionamiento != null){

            SesionManager.getInstance().setListaEstacionamientos(listaEstacionamiento);

            loginActivity.finish();

        }
        progress.dismiss();

    }
}
