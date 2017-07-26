package com.example.puchoo.mapmaterial.Dao;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.puchoo.mapmaterial.Modelo.ReservaMock;
import com.example.puchoo.mapmaterial.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Puchoo on 26/07/2017.
 */

public class ReservaDAO {
    private static final String STRING_SHARED_PREF = "listaReservas" ;
    private static ReservaDAO ourInstance = new ReservaDAO();
    private static final int MODO_PERSISTENCIA_MIXTA = 2;  // Los datos se almacenan en la api rest y en local
    private static final int MODO_PERSISTENCIA_LOCAL = 1;  // Los datos se almacenan solamente en la bdd local
    private static final int MODO_PERSISTENCIA_REMOTA = 0; // Los datos se almacenan solamente en la nube
    private static int MODO_PERSISTENCIA_CONFIGURADA = MODO_PERSISTENCIA_MIXTA; // Como default es mixta
    private static boolean usarApiRest = false; // default true
    private static final FileSaverHelper fileSaver = FileSaverHelper.getInstance(); // Clase que se encarga del almacenamiento local
    private static final String TAG = "ReservaDAO";

    private Context context;

    private static Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    private ReservaDAO(){ }

    public static ReservaDAO getInstance(){
        return ourInstance;
    }

    /**
     * Permite guardar la reserva SharedPref - Es temporal
     * @param reserva
     */
    public void guardarReservasSharedPref(ReservaMock reserva, Context context) {
        this.context = context;

        ArrayList<ReservaMock> listaReservasViejas = getReservaSharedPref(context);
        listaReservasViejas.add(reserva);

        String jsonStringReservas = gson.toJson(listaReservasViejas);

        PreferenceManager.getDefaultSharedPreferences(this.context).edit()
                .putString(STRING_SHARED_PREF, jsonStringReservas)
                .apply();

        String msg = context.getResources().getString(R.string.fileSaverAlmacenExitoso);
        Log.v(TAG,msg);
    }

    public ArrayList<ReservaMock> getReservaSharedPref(Context context) {
        this.context = context;
        ArrayList<ReservaMock> reservasViejasList;
        String listaReservasJson = PreferenceManager.getDefaultSharedPreferences(this.context)
                .getString("listaReservas","");

        if(listaReservasJson.equals("")){
            reservasViejasList = new ArrayList<ReservaMock>();
        }
        else{
            //obtener la lista de Resultados desde el Json
            Type type = new TypeToken<List<ReservaMock>>() {}.getType();
            reservasViejasList = gson.fromJson(listaReservasJson, type);
        }

        return reservasViejasList;
    }

    public void actualiarReservasSharedPref(ArrayList<ReservaMock> reservasNuevas, Context context){
        this.context = context;
        String jsonStringReservas = gson.toJson(reservasNuevas);

        PreferenceManager.getDefaultSharedPreferences(this.context).edit()
                .putString(STRING_SHARED_PREF, jsonStringReservas)
                .apply();

        String msg = context.getResources().getString(R.string.fileSaverAlmacenExitoso);
        Log.v(TAG,msg);
    }

    public void borrarReservaSharedPref(ReservaMock reserva, Context context){
        this.context = context;
        ArrayList<ReservaMock> reservasViejasList;

        reservasViejasList = getReservaSharedPref(context);

        if (!reservasViejasList.equals("")){
            reservasViejasList.remove(reserva);
            actualiarReservasSharedPref(reservasViejasList, context);
        }
    }



}
