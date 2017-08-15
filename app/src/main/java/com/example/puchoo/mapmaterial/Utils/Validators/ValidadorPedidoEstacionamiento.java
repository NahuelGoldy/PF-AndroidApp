package com.example.puchoo.mapmaterial.Utils.Validators;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.puchoo.mapmaterial.Dao.EstacionamientoDAO;
import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;
import com.example.puchoo.mapmaterial.Utils.Api.EstacionamientoEndpointClient;
import com.example.puchoo.mapmaterial.VistasAndControllers.CesionManager;
import com.example.puchoo.mapmaterial.VistasAndControllers.Fragments.ListContentFragment;

import java.util.ArrayList;

/**
 * Created by Puchoo on 10/08/2017.
 */

public class ValidadorPedidoEstacionamiento extends AsyncTask<Void,Void,Void> {

    private final ListContentFragment listContentFragment;
    private ProgressDialog progress;
    private static final String TAG = "ValidadorPedEst";
    private ArrayList<Estacionamiento> listaEstacionamiento;

    public ValidadorPedidoEstacionamiento(ProgressDialog progress, ListContentFragment listContentFragment){
        this.progress = progress;
        this.listContentFragment = listContentFragment;

    }

    @Override
    protected void onPreExecute(){
        progress.show();

        //ejecuta antes de arrancar
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {

            if (!CesionManager.getInstance().getActualiarBD()) {
                listaEstacionamiento = EstacionamientoDAO.getInstance().listarEstacionamientos(this.listContentFragment.getContext());
            } else{
                listaEstacionamiento = (ArrayList<Estacionamiento>) new EstacionamientoEndpointClient().getAllEstacionamientos();
            }
            /** Si no hay nada en local pidel a la api**/
            if(listaEstacionamiento.isEmpty()){
                listaEstacionamiento = (ArrayList<Estacionamiento>) new EstacionamientoEndpointClient().getAllEstacionamientos();
            }

        } catch (Exception e) {

            String msgLog = "Hubo un error al traer los estacionamientos.";
            Log.v(TAG,msgLog);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void unused){

        if (listaEstacionamiento != null){
            listContentFragment.setListaEstacionamientos(listaEstacionamiento);
            listContentFragment.marcarEstacionamientos();
        }
        progress.dismiss();
        //ejecuta cuando termina
    }
}
