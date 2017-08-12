package com.example.puchoo.mapmaterial.Modelo;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.puchoo.mapmaterial.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static android.content.ContentValues.TAG;

/**
 * Created by Puchoo on 11/08/2017.
 */

public class Versionado {

    private static final String STRING_SHARED_PREF = "versionado";
    private static long VERSION_BD_ESTACIONAMIENTOS = 0;
    private static long VERSION_BD_PARKIMETROS = 0;
    private static long VERSION_BD_ZONA_PARKING = 0;
    private static Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private Context context;

    public static long getVersionBdEstacionamientos() {
        return VERSION_BD_ESTACIONAMIENTOS;
    }

    public static void setVersionBdEstacionamientos(long versionBdEstacionamientos) {
        VERSION_BD_ESTACIONAMIENTOS = versionBdEstacionamientos;
    }

    public static long getVersionBdParkimetros() {
        return VERSION_BD_PARKIMETROS;
    }

    public static void setVersionBdParkimetros(long versionBdParkimetros) {
        VERSION_BD_PARKIMETROS = versionBdParkimetros;
    }

    public static long getVersionBdZonaParking() {
        return VERSION_BD_ZONA_PARKING;
    }

    public static void setVersionBdZonaParking(long versionBdZonaParking) {
        VERSION_BD_ZONA_PARKING = versionBdZonaParking;
    }

    public void initVersionado(Context context) {
        this.context = context;
        Versionado versionado;
        String versionadoJson = PreferenceManager.getDefaultSharedPreferences(this.context)
                .getString(STRING_SHARED_PREF, "");

        if (versionadoJson.equals("")) {
            versionado = new Versionado();
        } else {
            //obtener la lista de Resultados desde el Json
            Type type = new TypeToken<Versionado>() {
            }.getType();
            versionado = gson.fromJson(versionadoJson, type);

            this.setVersionBdEstacionamientos( versionado.getVersionBdEstacionamientos());
            this.setVersionBdParkimetros(versionado.getVersionBdParkimetros());
            this.setVersionBdZonaParking(versionado.getVersionBdZonaParking());
        }
    }

    public void guardarVersionado(Context context){
        this.context = context;

        Versionado versionadoNuevo = new Versionado();
        versionadoNuevo.setVersionBdZonaParking(this.getVersionBdZonaParking());
        versionadoNuevo.setVersionBdParkimetros(this.getVersionBdParkimetros());
        versionadoNuevo.setVersionBdEstacionamientos(this.getVersionBdEstacionamientos());

        String jsonStringVersionado = gson.toJson(versionadoNuevo);

        PreferenceManager.getDefaultSharedPreferences(this.context).edit()
                .putString(STRING_SHARED_PREF, jsonStringVersionado)
                .apply();

        String msg = context.getResources().getString(R.string.fileSaverAlmacenExitoso);
        Log.v(TAG,msg);
    }
}
