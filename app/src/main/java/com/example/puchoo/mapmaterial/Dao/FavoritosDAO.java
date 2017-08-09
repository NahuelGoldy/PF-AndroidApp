package com.example.puchoo.mapmaterial.Dao;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;
import com.example.puchoo.mapmaterial.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Puchoo on 21/07/2017.
 */

public class FavoritosDAO {

    private static final String STRING_SHARED_PREF = "listaFavoritos" ;
    private static FavoritosDAO ourInstance = new FavoritosDAO();
    private static final String TAG = "FavoritosDAO";

    private Context context;

    private static Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    private FavoritosDAO(){ }

    public static FavoritosDAO getInstance(){
        return ourInstance;
    }

    /**
     * Permite guardar el Estacionamiento SharedPref - Es temporal
     * @param estacionamiento
     */
    public void guardarFavoritoSharedPref(Estacionamiento estacionamiento, Context context) {
        this.context = context;

        ArrayList<Estacionamiento> listaFavoritosViejos = getFavoritosSharedPref(context);
        listaFavoritosViejos.add(estacionamiento);

        String jsonStringFavoritos = gson.toJson(listaFavoritosViejos);

        PreferenceManager.getDefaultSharedPreferences(this.context).edit()
                .putString(STRING_SHARED_PREF, jsonStringFavoritos)
                .apply();

        String msg = context.getResources().getString(R.string.fileSaverAlmacenExitoso);
        Log.v(TAG,msg);
    }

    public ArrayList<Estacionamiento> getFavoritosSharedPref(Context context) {
        this.context = context;
        ArrayList<Estacionamiento> favoritosViejosList;
        String listaReservasJson = PreferenceManager.getDefaultSharedPreferences(this.context)
                .getString(STRING_SHARED_PREF,"");

        if(listaReservasJson.equals("")){
            favoritosViejosList = new ArrayList<Estacionamiento>();
        }
        else{
            //obtener la lista de Resultados desde el Json
            Type type = new TypeToken<List<Estacionamiento>>() {}.getType();
            favoritosViejosList = gson.fromJson(listaReservasJson, type);
        }

        return favoritosViejosList;
    }

    public void actualiarFavoritosSharedPref(ArrayList<Estacionamiento> favoritosNuevos, Context context){
        this.context = context;
        String jsonStringFavoritos = gson.toJson(favoritosNuevos);

        PreferenceManager.getDefaultSharedPreferences(this.context).edit()
                .putString(STRING_SHARED_PREF, jsonStringFavoritos)
                .apply();

        String msg = context.getResources().getString(R.string.fileSaverAlmacenExitoso);
        Log.v(TAG,msg);
    }

    public void borrarEstacionamientoSharedPref(Estacionamiento estacionamiento, Context context){
        this.context = context;
        ArrayList<Estacionamiento> favoritosViejosList;

        favoritosViejosList = getFavoritosSharedPref(context);

        if (!favoritosViejosList.equals("")){
            //if(reservasViejasList.remove(reserva)){
            favoritosViejosList.remove(estacionamiento);
            actualiarFavoritosSharedPref(favoritosViejosList, context);
            //}
        }
    }
}
