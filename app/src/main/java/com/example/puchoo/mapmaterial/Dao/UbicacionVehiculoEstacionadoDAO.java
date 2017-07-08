package com.example.puchoo.mapmaterial.Dao;


import android.content.Context;
import android.location.Address;
import android.util.Log;

import com.example.puchoo.mapmaterial.Exceptions.FileSaverException;
import com.example.puchoo.mapmaterial.Exceptions.UbicacionVehiculoException;
import com.example.puchoo.mapmaterial.Modelo.UbicacionVehiculoEstacionado;
import com.example.puchoo.mapmaterial.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Agustin on 01/25/2017.
 */

public class UbicacionVehiculoEstacionadoDAO {
    private static UbicacionVehiculoEstacionadoDAO ourInstance = new UbicacionVehiculoEstacionadoDAO();
    private static final int MODO_PERSISTENCIA_MIXTA = 2;  // Los datos se almacenan en la api rest y en local
    private static final int MODO_PERSISTENCIA_LOCAL = 1;  // Los datos se almacenan solamente en la bdd local
    private static final int MODO_PERSISTENCIA_REMOTA = 0; // Los datos se almacenan solamente en la nube
    private static int MODO_PERSISTENCIA_CONFIGURADA = MODO_PERSISTENCIA_MIXTA; // Como default es mixta
    private static boolean usarApiRest = false; // default false
    private static final LocalDBManager dbManagerLocal = LocalDBManager.getInstance(); // Clase que se encarga del almacenamiento local
    private static final String TAG = "UbicacionVehiculoDAO";
    private static final String UBICACION_VEHICULO_FILENAME = "ubicacionVehiculo.json";



    private Context context;

    private static Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    private UbicacionVehiculoEstacionadoDAO(){ }

    public static UbicacionVehiculoEstacionadoDAO getInstance(){
        MODO_PERSISTENCIA_CONFIGURADA = MODO_PERSISTENCIA_LOCAL;
        return ourInstance;
    }

    /**
     * Retorna el objeto ubicacion vehiculo estacionado con el usuario parametro y la fecha de ingreso (id de la ubicacion)
     * @param idUsuario
     * @param fechaIngreso
     * @return
     * @throws UbicacionVehiculoException
     */
    public UbicacionVehiculoEstacionado getUbicacionVehiculo(Integer idUsuario,Long fechaIngreso,Context context) throws UbicacionVehiculoException {
        this.context = context;
        switch(MODO_PERSISTENCIA_CONFIGURADA){
            case MODO_PERSISTENCIA_LOCAL:{
                return getUbicacionVehiculoLocal(idUsuario,fechaIngreso);
            }
            case MODO_PERSISTENCIA_REMOTA:{
                return getUbicacionVehiculoRemoto(idUsuario,fechaIngreso);
            }
            case MODO_PERSISTENCIA_MIXTA:{
                /* TODO - IMPLEMENTAR SI ES NECESARIO */
                break;
            }
        }
        return null;
    }
    
    private UbicacionVehiculoEstacionado getUbicacionVehiculoLocal(Integer idUsuario,Long fechaIngreso) throws UbicacionVehiculoException {
        ArrayList<UbicacionVehiculoEstacionado> ubicacionesUsuario;
        ubicacionesUsuario = listarUbicacionVehiculoEstacionadoLocal(idUsuario);
        /** TODO - Cambiar implementacion a futuro si es necesario, por una implementacion que obtenga
         *  el item desde la base de datos (como el listar por id de usuario), es mas eficiente, puesto que
         *  se evita crear un arraylist con todos los objetos ubicaciones, directamente se lee desde el JSONArray
         *  la fecha de ingreso, y si corresponde, se crea ese unico objeto y se devuelve
         */
        for(UbicacionVehiculoEstacionado iterador:ubicacionesUsuario){
            if(iterador.getHoraIngreso().compareTo(fechaIngreso) == 0){
                return iterador;
            }
        }
        return null;
    }

    /** TODO - Implementar */
    private UbicacionVehiculoEstacionado getUbicacionVehiculoRemoto(Integer idUsuario,Long fechaIngreso){ return null;}

    /**
     * Actualiza la informacion del objeto ubicacionVehiculoEstacionado pasado en la base de datos
     * @param ubicacionVehiculo
     * @throws UbicacionVehiculoException
     */
    public void actualizarUbicacionVehiculoEstacionado(UbicacionVehiculoEstacionado ubicacionVehiculo,Context context) throws UbicacionVehiculoException {
        this.context = context;
        switch(MODO_PERSISTENCIA_CONFIGURADA){
            case MODO_PERSISTENCIA_LOCAL:{
                actualizarUbicacionVehiculoLocal(ubicacionVehiculo);
                break;
            }
            case MODO_PERSISTENCIA_REMOTA:{
                actualizarUbicacionVehiculoRemoto(ubicacionVehiculo);
                break;
            }
            case MODO_PERSISTENCIA_MIXTA:{
                actualizarUbicacionVehiculoLocal(ubicacionVehiculo);
                actualizarUbicacionVehiculoRemoto(ubicacionVehiculo);
                break;
            }
        }
    }
    /**
     * Actualiza la ubicacion del objeto ubicacionVehiculoEstacionado en la base de datos local
     * @param ubicacionVehiculo
     * @throws UbicacionVehiculoException
     */
    private void actualizarUbicacionVehiculoLocal(UbicacionVehiculoEstacionado ubicacionVehiculo) throws UbicacionVehiculoException{
        JSONObject baseDeDatos;
        String jsonStringBaseDeDatos;
        String msg;
        try {
            jsonStringBaseDeDatos = dbManagerLocal.getArchivo(UBICACION_VEHICULO_FILENAME,context);
            baseDeDatos = new JSONObject(jsonStringBaseDeDatos);
            /** TODO Esto hay que cambiarlo a futuro en el caso de que hagamos herencia con los tipos de estacionamientos */
            JSONArray estacionamientos = baseDeDatos.getJSONArray("estacionamientosCalle");
            actualizarArrayEstacionamientos(estacionamientos,ubicacionVehiculo);
            jsonStringBaseDeDatos = baseDeDatos.toString();
            dbManagerLocal.guardarArchivo(jsonStringBaseDeDatos,UBICACION_VEHICULO_FILENAME,context);
        }
        catch (FileSaverException e) {
            Log.v(TAG,e.getMessage());
        }
        catch (JSONException e){
            msg = context.getResources().getString(R.string.fileSaverErrorEscrituraLocal);
            Log.v(TAG,msg);
            throw new UbicacionVehiculoException(msg);
        }
    }

    /* TODO - IMPLEMENTAR */
    private void actualizarUbicacionVehiculoRemoto(UbicacionVehiculoEstacionado ubicacionVehiculo) throws UbicacionVehiculoException {
    //    daoApiRest.actualizarTarea(t);
    }

    /**
     * Parsea el JSON ARRAY para buscar el objeto correspondiente y actualizarlo
     * @param estacionamientos
     * @param vehiculo
     */
    private void actualizarArrayEstacionamientos(JSONArray estacionamientos, UbicacionVehiculoEstacionado vehiculo) throws JSONException {
        JSONObject iterador = new JSONObject();
        JSONObject objetoDireccion;
        for(int i = 0; i<estacionamientos.length();i++){
            iterador = (JSONObject) estacionamientos.get(i);
            if(vehiculo.getHoraIngreso().compareTo((Long) iterador.get("horaIngreso")) == 0){ // Comprueba que el id del estacionamiento sea el mismo
                if(iterador.get("idUsuario") == vehiculo.getIdUsuario()){ // Comprueba que el id del usuario sea el mismo
                    if(((Boolean) iterador.get("eliminado") == false)) { // Comprueba que no es una ubicacion eliminada y por lo tanto se puede escribir
                        /* Estos ifs estan hechos asi, porque el metodo put del JSONObject, no agrega nada si el elemento a agregar es null
                            y borra el atributo que es nulo en el json, y por lo tanto, deja un campo vacio bugeado.
                            entonces uso el if para dejar el campo nulo pero sin borrar el atributo
                         */
                        if(vehiculo.getIdUsuario() != null) {
                            iterador.put("idUsuario", vehiculo.getIdUsuario());
                        }
                        if(vehiculo.getHoraEgreso() != null ){
                            iterador.put("horaEgreso",vehiculo.getHoraEgreso());
                        }
                        if(vehiculo.getHoraIngreso() != null){
                            iterador.put("horaIngreso",vehiculo.getHoraIngreso());
                        }
                        if(vehiculo.getDireccion() != null){
                            Address direccion = vehiculo.getDireccion();
                            objetoDireccion = new JSONObject(gson.toJson(direccion));
                            iterador.put("direccion",objetoDireccion);
                        }
                        iterador.put("eliminado",vehiculo.getEliminado());
                        return;
                    }
                }
            }
        }
    }

    /**
     * Persiste una ubicacion vehiculo nueva
     * @param ubicacionVehiculo
     * @throws UbicacionVehiculoException
     */
    public void guardarOActualizarUbicacionVehiculo(UbicacionVehiculoEstacionado ubicacionVehiculo, Context context) throws UbicacionVehiculoException {
        this.context = context;
        switch(MODO_PERSISTENCIA_CONFIGURADA){
            case MODO_PERSISTENCIA_LOCAL:{
                guardarUbicacionVehiculoLocal(ubicacionVehiculo);
                break;
            }
            case MODO_PERSISTENCIA_REMOTA:{
                guardarUbicacionVehiculoRemoto(ubicacionVehiculo);
                break;
            }
            case MODO_PERSISTENCIA_MIXTA:{
                guardarUbicacionVehiculoLocal(ubicacionVehiculo);
                guardarUbicacionVehiculoRemoto(ubicacionVehiculo);
                break;
            }
        }
    }

    private void guardarUbicacionVehiculoLocal(UbicacionVehiculoEstacionado ubicacionVehiculo) throws UbicacionVehiculoException {
        String jsonStringVehiculo = gson.toJson(ubicacionVehiculo);
        JSONObject vehiculo;
        JSONObject baseDeDatos;
        String jsonStringBaseDeDatos;
        String msg;
        try {
            vehiculo = new JSONObject(jsonStringVehiculo);
            jsonStringBaseDeDatos = dbManagerLocal.getArchivo(UBICACION_VEHICULO_FILENAME,context);
            baseDeDatos = new JSONObject(jsonStringBaseDeDatos);
            /** TODO Esto hay que cambiarlo a futuro en el caso de que hagamos herencia con los tipos de estacionamientos */
            baseDeDatos.getJSONArray("estacionamientosCalle").put(vehiculo);
            jsonStringBaseDeDatos = baseDeDatos.toString();
            dbManagerLocal.guardarArchivo(jsonStringBaseDeDatos,UBICACION_VEHICULO_FILENAME,context);
        }
        catch (FileSaverException e) {
            Log.v(TAG,e.getMessage());
        }
        catch (JSONException e){
            msg = context.getResources().getString(R.string.fileSaverErrorEscrituraLocal);
            Log.v(TAG,msg);
            throw new UbicacionVehiculoException(msg);
        }
    }

    /* TODO - Implementar */
    private void guardarUbicacionVehiculoRemoto(UbicacionVehiculoEstacionado ubicacionVehiculo) throws UbicacionVehiculoException {
       // daoApiRest.guardarTarea(t);
    }

    /**
     * Devuelve una lista con todas las ubicaciones de los vehiculos estacionados
     * del usuario id, de lo contrario retorna null
     * @param idUsuario id del usuario al que se desea listar sus ubicaciones
     * @return
     */
    public ArrayList<UbicacionVehiculoEstacionado> listarUbicacionVehiculoEstacionado(Integer idUsuario,Context context) throws UbicacionVehiculoException {
        this.context = context;
        switch(MODO_PERSISTENCIA_CONFIGURADA){
            case MODO_PERSISTENCIA_LOCAL:{
                return listarUbicacionVehiculoEstacionadoLocal(idUsuario);
            }
            case MODO_PERSISTENCIA_REMOTA:{
                return listarUbicacionVehiculoEstacionadoRemoto(idUsuario);
            }
            case MODO_PERSISTENCIA_MIXTA:{
                /** TODO - IMPLEMENTAR SI ES NECESARIO */
                break;
            }
        }
        return null;
    }

    private ArrayList<UbicacionVehiculoEstacionado> listarUbicacionVehiculoEstacionadoLocal(Integer idUsuario) throws UbicacionVehiculoException {
        String msg;
        JSONObject baseDeDatos;
        JSONArray listaEstacionamientos;
        ArrayList<UbicacionVehiculoEstacionado> listado = null;
        JSONObject iterador;
        UbicacionVehiculoEstacionado iteradorObject;
        try{
            baseDeDatos = new JSONObject(dbManagerLocal.getArchivo(UBICACION_VEHICULO_FILENAME,context));
            listaEstacionamientos = baseDeDatos.getJSONArray("estacionamientosCalle");
            listado = new ArrayList<UbicacionVehiculoEstacionado>();
            for(int i = 0; i<listaEstacionamientos.length();i++){
                iterador = ((JSONObject) listaEstacionamientos.get(i));
                if( iterador.get("idUsuario") == idUsuario ){ // Compruebo que se corresponda el usuario
                    if(iterador.getBoolean("eliminado") == false){ // Solo devuelvo los que no esten eliminados
                        iteradorObject = gson.fromJson(iterador.toString(),UbicacionVehiculoEstacionado.class);
                        listado.add(iteradorObject);
                    }
                }
            }
            msg = context.getResources().getString(R.string.listaUbicacionVehiculosEncontradaExitosamente);
            Log.v(TAG,msg);
        }
        catch(FileSaverException e){
            msg = context.getResources().getString(R.string.listaUbicacionVehiculosErrorLectura);
            Log.v(TAG,msg);
        }
        catch(JSONException e){
            msg = context.getResources().getString(R.string.listaUbicacionVehiculosNotFound);
            Log.v(TAG,msg);
        }
        return listado;
    }

    /** TODO - Implementar */
    private ArrayList<UbicacionVehiculoEstacionado> listarUbicacionVehiculoEstacionadoRemoto(Integer idUsuario) throws UbicacionVehiculoException{ return null;}

    /**
     * Devuelve la ultima ubicacion en donde el usuario idUsuario estaciono el vehiculo
     * Si no hay ubicaciones registradas del usuario retorna null
     * @param idUsuario
     * @param context
     * @return
     */
    /** TODO - Comprobar que devuelve realmente la ultima ubicacion */
    public UbicacionVehiculoEstacionado getUltimaUbicacionVehiculo(Integer idUsuario, Context context){
        this.context = context;
        switch(MODO_PERSISTENCIA_CONFIGURADA){
            case MODO_PERSISTENCIA_LOCAL:{
                return getUltimaUbicacionVehiculoLocal(idUsuario);
            }
            case MODO_PERSISTENCIA_REMOTA:{
                return getUltimaUbicacionVehiculoRemoto(idUsuario);
            }
            case MODO_PERSISTENCIA_MIXTA:{
                /** TODO - IMPLEMENTAR SI ES NECESARIO */
                break;
            }
        }
        return null;
    }

    private UbicacionVehiculoEstacionado getUltimaUbicacionVehiculoLocal(Integer idUsuario){
        String msg;
        JSONObject baseDeDatos;
        JSONArray listaEstacionamientos;
        JSONObject iterador;
        UbicacionVehiculoEstacionado iteradorObject = null;
        try {
            baseDeDatos = new JSONObject(dbManagerLocal.getArchivo(UBICACION_VEHICULO_FILENAME,context));
            listaEstacionamientos = baseDeDatos.getJSONArray("estacionamientosCalle");
            for(int i = listaEstacionamientos.length()-1; i>=0;i--){ // Como el file esta ordenado con los ultimos elementos al final, arranco por atras
                iterador = ((JSONObject) listaEstacionamientos.get(i));
                if( iterador.get("idUsuario") == idUsuario ){
                    if(iterador.getBoolean("eliminado") == false) { // Solo lo devuelvo si no esta eliminado
                        iteradorObject = gson.fromJson(iterador.toString(), UbicacionVehiculoEstacionado.class);
                        msg = context.getResources().getString(R.string.ubicacionVehiculoBuscadoFound);
                        Log.v(TAG, msg);
                        return iteradorObject;
                    }
                }
            }
            msg = context.getResources().getString(R.string.listaUbicacionVehiculosNotFound);
            Log.v(TAG,msg);
        }
        catch (FileSaverException e) {
            e.printStackTrace();
            msg = e.getMessage();
            Log.v(TAG,msg);
        }
        catch(JSONException e){
            e.printStackTrace();
            msg = context.getResources().getString(R.string.listaUbicacionVehiculosNotFound);
            Log.v(TAG,msg);
        }
        return iteradorObject;
    }

    /** TODO - Implementar */
    private UbicacionVehiculoEstacionado getUltimaUbicacionVehiculoRemoto(Integer idUsuario){ return null;}

    /**
     * Borra el objeto de la base de datos de manera LOGICA, no hay implementacion FISICA
     * @param ubicacionVehiculo
     * @throws UbicacionVehiculoException
     */
    public void borrarUbicacionVehiculo(UbicacionVehiculoEstacionado ubicacionVehiculo, Context context) throws UbicacionVehiculoException {
        this.context = context;
        switch(MODO_PERSISTENCIA_CONFIGURADA){
            case MODO_PERSISTENCIA_LOCAL:{
                borrarUbicacionVehiculoLocal(ubicacionVehiculo);
                break;
            }
            case MODO_PERSISTENCIA_REMOTA:{
                borrarUbicacionVehiculoRemoto(ubicacionVehiculo);
                break;
            }
            case MODO_PERSISTENCIA_MIXTA:{
                borrarUbicacionVehiculoLocal(ubicacionVehiculo);
                borrarUbicacionVehiculoRemoto(ubicacionVehiculo);
                break;
            }
        }
    }

    /**
     * Elimina el objeto ubicacionVehiculo de la base de datos local
     * @param ubicacionVehiculo
     */
    private void borrarUbicacionVehiculoLocal(UbicacionVehiculoEstacionado ubicacionVehiculo) throws UbicacionVehiculoException {
        ubicacionVehiculo.setEliminado(true);
        guardarOActualizarUbicacionVehiculo(ubicacionVehiculo,context);
    }

    /**
     * Elimina el objeto ubicacionVehiculo de la base de datos remota
     * @param ubicacionVehiculo
     * /** TODO - NOTA: una buena implementacion (cuando se realice), deberia de extraer solamente la hora de ingreso y el id de usuario y que el backend haga el resto
     */
    private void borrarUbicacionVehiculoRemoto(UbicacionVehiculoEstacionado ubicacionVehiculo){

    }


}
