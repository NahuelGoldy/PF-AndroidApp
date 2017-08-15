package com.example.puchoo.mapmaterial.Dao;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.puchoo.mapmaterial.Modelo.Versionado;
import com.example.puchoo.mapmaterial.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static android.content.ContentValues.TAG;

/**
 * Created by Puchoo on 14/08/2017.
 */

public class VersionadoDAO {

    private static VersionadoDAO instance;
    private static final String STRING_SHARED_PREF = "versionado";
    private static Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private Context context;
    private Versionado versionado;


    private VersionadoDAO(){    }

    public static VersionadoDAO getInstance(){
        if (instance == null){
            instance = new VersionadoDAO();
        }
        return instance;
    }

    public Versionado getVersionado(Context context) {
        this.context = context;
        Versionado versionadoAux;
        String versionadoJson = PreferenceManager.getDefaultSharedPreferences(this.context)
                .getString(STRING_SHARED_PREF, "");

        if (versionadoJson.equals("")) {
            versionadoAux = new Versionado();
        } else {
            //obtener la lista de Resultados desde el Json
            Type type = new TypeToken<Versionado>() {
            }.getType();
            versionadoAux = gson.fromJson(versionadoJson, type);

            versionadoAux.getVersionBdEstacionamientos();
            versionadoAux.getVersionBdParkimetros();
            versionadoAux.getVersionBdZonaParking();
        }

        return versionadoAux;
    }

    public void guardarVersionado(Context context, Versionado versionado){
        this.context = context;

        Versionado versionadoNuevo = new Versionado();
        versionadoNuevo.setVersionBdZonaParking(versionado.getVersionBdZonaParking());
        versionadoNuevo.setVersionBdParkimetros(versionado.getVersionBdParkimetros());
        versionadoNuevo.setVersionBdEstacionamientos(versionado.getVersionBdEstacionamientos());

        String jsonStringVersionado = gson.toJson(versionadoNuevo);

        PreferenceManager.getDefaultSharedPreferences(this.context).edit()
                .putString(STRING_SHARED_PREF, jsonStringVersionado)
                .apply();

        String msg = context.getResources().getString(R.string.fileSaverAlmacenExitoso);
        Log.v(TAG,msg);
    }

}
