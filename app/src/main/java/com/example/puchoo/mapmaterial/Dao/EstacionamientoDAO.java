package com.example.puchoo.mapmaterial.Dao;


import android.content.Context;
import android.util.Log;

import com.example.puchoo.mapmaterial.Exceptions.EstacionamientoException;
import com.example.puchoo.mapmaterial.Exceptions.FileSaverException;
import com.example.puchoo.mapmaterial.Exceptions.UbicacionVehiculoException;
import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;
import com.example.puchoo.mapmaterial.R;
import com.example.puchoo.mapmaterial.Utils.Api.EstacionamientoEndpointClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agustin on 01/25/2017.
 */

public class EstacionamientoDAO {
    private static EstacionamientoDAO ourInstance = new EstacionamientoDAO();
    private static final int MODO_PERSISTENCIA_MIXTA = 2;  // Los datos se almacenan en la api rest y en local
    private static final int MODO_PERSISTENCIA_LOCAL = 1;  // Los datos se almacenan solamente en la bdd local
    private static final int MODO_PERSISTENCIA_REMOTA = 0; // Los datos se almacenan solamente en la nube
    private static int MODO_PERSISTENCIA_CONFIGURADA = MODO_PERSISTENCIA_LOCAL; // Como default es mixta
    private static boolean usarApiRest = false; // default true
    private static final FileSaverHelper fileSaver = FileSaverHelper.getInstance(); // Clase que se encarga del almacenamiento local
    private static final String TAG = "EstacionamientoDAO";
    private static final String UBICACION_VEHICULO_FILENAME = "estacionamientos.json";
    private static final String LISTA_ESTACIONAMIENTOS_FILENAME = "ParkingLots.json";

    private Context context;

    private static Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    private EstacionamientoDAO(){ }

    public static EstacionamientoDAO getInstance(){
        MODO_PERSISTENCIA_CONFIGURADA = MODO_PERSISTENCIA_LOCAL;
        return ourInstance;
    }

    /**
     * Retorna el objeto estacionamiento
     * @param idUsuario
     * @param fechaIngreso
     * @return
     * @throws EstacionamientoException
     */
    /** TODO - Cambiar la firma por algo que sea util para buscar, por ejemplo un "id estacionamiento" */
    public Estacionamiento getUbicacionVehiculo(Integer idUsuario, Long fechaIngreso, Context context) throws EstacionamientoException {
        this.context = context;
        switch(MODO_PERSISTENCIA_CONFIGURADA){
            case MODO_PERSISTENCIA_LOCAL:{
                return getEstacionamientoLocal(idUsuario,fechaIngreso);
            }
            case MODO_PERSISTENCIA_REMOTA:{
                return getEstacionamientoRemoto(idUsuario,fechaIngreso);
            }
            case MODO_PERSISTENCIA_MIXTA:{
                /* TODO - IMPLEMENTAR SI ES NECESARIO */
                break;
            }
        }
        return null;
    }
    /** TODO - Cambiar la firma por algun id */
    private Estacionamiento getEstacionamientoLocal(Integer idUsuario,Long fechaIngreso) throws EstacionamientoException {

        return null;
    }

    /** TODO - Implementar y cambiar firma */
    private Estacionamiento getEstacionamientoRemoto(Integer idUsuario,Long fechaIngreso){ return null;}

    /**
     * Actualiza la informacion del objeto ubicacionVehiculoEstacionado pasado en la base de datos
     * @param estacionamiento
     * @throws UbicacionVehiculoException
     */
    public void actualizarEstacionamiento(Estacionamiento estacionamiento,Context context) throws EstacionamientoException {
        this.context = context;
        switch(MODO_PERSISTENCIA_CONFIGURADA){
            case MODO_PERSISTENCIA_LOCAL:{
                actualizarEstacionamientoLocal(estacionamiento);
                break;
            }
            case MODO_PERSISTENCIA_REMOTA:{
                actualizarEstacionamientoRemoto(estacionamiento);
                break;
            }
            case MODO_PERSISTENCIA_MIXTA:{
                actualizarEstacionamientoLocal(estacionamiento);
                actualizarEstacionamientoRemoto(estacionamiento);
                break;
            }
        }
    }

    public void inicializarListaEstacionamientos(Context context) throws EstacionamientoException{
        String jsonString;
        String msg;
        try{
            ArrayList<Estacionamiento> estacionamientosList =
                    (ArrayList<Estacionamiento>) new EstacionamientoEndpointClient().getAllEstacionamientos();
            jsonString = this.generarJsonDesdeArray(estacionamientosList);
            fileSaver.usarEscrituraInterna(true);
            fileSaver.guardarArchivo(jsonString,LISTA_ESTACIONAMIENTOS_FILENAME,context);
        }
        catch (Exception e){
            msg = "Error al crear/guardar la lista de Estacionamientos.";
            Log.v(TAG,msg);
            throw new EstacionamientoException(msg);
        }
    }


    /**
     * Actualiza la ubicacion del objeto ubicacionVehiculoEstacionado en la base de datos local
     * @param estacionamiento
     * @throws EstacionamientoException
     */
    private void actualizarEstacionamientoLocal(Estacionamiento estacionamiento) throws EstacionamientoException{
        JSONObject baseDeDatos;
        String jsonStringBaseDeDatos;
        String msg;
        try {
            jsonStringBaseDeDatos = fileSaver.getArchivo(UBICACION_VEHICULO_FILENAME,context);
            baseDeDatos = new JSONObject(jsonStringBaseDeDatos);
            /** TODO Esto hay que cambiarlo a futuro en el caso de que hagamos herencia con los tipos de estacionamientos */
            JSONArray estacionamientos = baseDeDatos.getJSONArray("estacionamientos");
            actualizarArrayEstacionamientos(estacionamientos,estacionamiento);
            jsonStringBaseDeDatos = baseDeDatos.toString();
            fileSaver.usarEscrituraInterna(true);
            fileSaver.guardarArchivo(jsonStringBaseDeDatos,UBICACION_VEHICULO_FILENAME,context);
        }
        catch (FileSaverException e) {
            Log.v(TAG,e.getMessage());
        }
        catch (JSONException e){
            msg = context.getResources().getString(R.string.fileSaverErrorEscrituraLocal);
            Log.v(TAG,msg);
            throw new EstacionamientoException(msg);
        }
    }

    /* TODO - IMPLEMENTAR */
    private void actualizarEstacionamientoRemoto(Estacionamiento estacionamiento) throws EstacionamientoException {
    //    daoApiRest.actualizarTarea(t);
    }

    /**
     * Parsea el JSON ARRAY para buscar el objeto correspondiente y actualizarlo
     * @param estacionamientos
     * @param estacionamiento
     */
    /** TODO IMPLEMENTAR */
    private void actualizarArrayEstacionamientos(JSONArray estacionamientos, Estacionamiento estacionamiento) throws JSONException {

    }

    /**
     * Persiste una ubicacion vehiculo nueva
     * @param estacionamiento
     * @throws UbicacionVehiculoException
     */
    public void guardarOActualizarEstacionamiento(Estacionamiento estacionamiento, Context context) throws EstacionamientoException {
        this.context = context;
        switch(MODO_PERSISTENCIA_CONFIGURADA){
            case MODO_PERSISTENCIA_LOCAL:{
                guardarEstacionamientoLocal(estacionamiento);
                break;
            }
            case MODO_PERSISTENCIA_REMOTA:{
                guardarEstacionamientoRemoto(estacionamiento);
                break;
            }
            case MODO_PERSISTENCIA_MIXTA:{
                guardarEstacionamientoLocal(estacionamiento);
                guardarEstacionamientoRemoto(estacionamiento);
                break;
            }
        }
    }

    private void guardarEstacionamientoLocal(Estacionamiento estacionamiento) throws EstacionamientoException {
        /** TODO - IMPLEMENTAR */
    }

    /* TODO - Implementar */
    private void guardarEstacionamientoRemoto(Estacionamiento estacionamiento) throws EstacionamientoException {
       // daoApiRest.guardarTarea(t);
    }

    /**
     * Devuelve una lista con todas los Estacionamientos de lo contrario retorna null
     * @param idUsuario id del usuario al que se desea listar sus ubicaciones
     * @return
     */
    /** TODO CAMBIAR FIRMA */
    public ArrayList<Estacionamiento> listarEstacionamientos(/*Integer idUsuario,*/Context context) throws EstacionamientoException {
        this.context = context;
        switch(MODO_PERSISTENCIA_CONFIGURADA){
            case MODO_PERSISTENCIA_LOCAL:{
                return listarEstacionamientosLocal(/*Integer idUsuario*/);
            }
            case MODO_PERSISTENCIA_REMOTA:{
                return listarEstacionamientosRemoto(/*Integer idUsuario*/);
            }
            case MODO_PERSISTENCIA_MIXTA:{
                /** TODO - IMPLEMENTAR SI ES NECESARIO */
                break;
            }
        }
        return null;
    }
    /** TODO IMPLEMENTAR */
    private ArrayList<Estacionamiento> listarEstacionamientosLocal(/*Integer idUsuario*/) throws EstacionamientoException {
        ArrayList<Estacionamiento> resultado = new ArrayList<Estacionamiento>();
        String jsonStringBaseDeDatos;
        String msg;
        try {
            Gson myGson = new Gson();
            jsonStringBaseDeDatos = fileSaver.getArchivo(LISTA_ESTACIONAMIENTOS_FILENAME,context);
            Type type = new TypeToken<List<Estacionamiento>>() {}.getType();

            List<Estacionamiento> estacionamientosList = myGson.fromJson(jsonStringBaseDeDatos, type);

            for (Estacionamiento e : estacionamientosList) {
                resultado.add(e);
            }
        }
        catch (FileSaverException e) {
            msg="Error de lectura desde el JSON de Estacionamientos.";
            Log.v(TAG,msg);
        }
        return resultado;
    }

    /** TODO - Implementar */
    private ArrayList<Estacionamiento> listarEstacionamientosRemoto(/*Integer idUsuario*/) throws EstacionamientoException{
        return null;
    }

    /**
     * Borra el objeto de la base de datos de manera LOGICA, no hay implementacion FISICA
     * @param estacionamiento
     * @throws EstacionamientoException
     */
    public void borrarEstacionamiento(Estacionamiento estacionamiento, Context context) throws EstacionamientoException {
        this.context = context;
        switch(MODO_PERSISTENCIA_CONFIGURADA){
            case MODO_PERSISTENCIA_LOCAL:{
                borrarEstacionamientoLocal(estacionamiento);
                break;
            }
            case MODO_PERSISTENCIA_REMOTA:{
                borrarEstacionamientoRemoto(estacionamiento);
                break;
            }
            case MODO_PERSISTENCIA_MIXTA:{
                borrarEstacionamientoLocal(estacionamiento);
                borrarEstacionamientoRemoto(estacionamiento);
                break;
            }
        }
    }

    /**
     * Elimina el objeto Estacionamiento de la base de datos local
     * @param estacionamiento
     */
    private void borrarEstacionamientoLocal(Estacionamiento estacionamiento) throws EstacionamientoException {
        estacionamiento.setEliminado(true);
        guardarOActualizarEstacionamiento(estacionamiento,context);
    }

    /**
     * Elimina el objeto ubicacionVehiculo de la base de datos remota
     * @param estacionamiento
     * /** TODO - NOTA: una buena implementacion (cuando se realice), deberia de extraer solamente la hora de ingreso y el id de usuario y que el backend haga el resto
     */
    private void borrarEstacionamientoRemoto(Estacionamiento estacionamiento){

    }

    public Estacionamiento[] llenarEstacionamientos(Context context) throws EstacionamientoException {
        //TODO ATENCION!!!! OJO al índice con el que se inicializó el array, totalmente harcodeado!! (ver estacionamientoDAO.listarEstacionamientosHarcodeados())
        Estacionamiento[] Estacionamientos = new Estacionamiento[12];
        ArrayList<Estacionamiento> estacionamientoList = new ArrayList<Estacionamiento>();
        estacionamientoList = listarEstacionamientos(context);
        int i=0;
        for(Estacionamiento e : estacionamientoList){
            Estacionamientos[i] = e;
            i++;
        }
        return Estacionamientos;
    }



    private String generarJsonDesdeArray(ArrayList<Estacionamiento> estacionamientosList) {
        //Generar JSON a partir de un ArrayList
        List<Estacionamiento> listEstac = new ArrayList<Estacionamiento>();
        for (int i=0; i<estacionamientosList.size(); i++){
            listEstac.add(estacionamientosList.get(i));
        }
        Gson gson = new Gson();
        String jsonEstac = gson.toJson(listEstac);
        return jsonEstac;
    }
}
