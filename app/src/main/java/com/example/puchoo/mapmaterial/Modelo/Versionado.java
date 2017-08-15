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

    private static long VERSION_BD_ESTACIONAMIENTOS = 0;
    private static long VERSION_BD_PARKIMETROS = 0;
    private static long VERSION_BD_ZONA_PARKING = 0;

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
}
