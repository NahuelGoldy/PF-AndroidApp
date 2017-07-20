package com.example.puchoo.mapmaterial.Dao;


import android.content.Context;
import android.util.Log;

import com.example.puchoo.mapmaterial.Exceptions.EstacionamientoException;
import com.example.puchoo.mapmaterial.Exceptions.FileSaverException;
import com.example.puchoo.mapmaterial.Exceptions.UbicacionVehiculoException;
import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;
import com.example.puchoo.mapmaterial.R;
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
    private static int MODO_PERSISTENCIA_CONFIGURADA = MODO_PERSISTENCIA_MIXTA; // Como default es mixta
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
            /** TODO en un futuro levantar la lista desde la nube */
            Estacionamiento[] estacionamientosList = this.listarEstacionamientosHarcodeados();
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

    private Estacionamiento[] listarEstacionamientosHarcodeados(){
        Estacionamiento[] Estacionamientos = new Estacionamiento[12];
        Estacionamientos[0] = new Estacionamiento();
        Estacionamientos[0].setDireccionEstacionamiento("DIRECCIÓN: Belgrano 2964");
        Estacionamientos[0].setNombreEstacionamiento("NOMBRE: El Estacionamiento de la Terminal");
        Estacionamientos[0].setHorarios("HORARIOS: Lun-Dom abierto las 24hs");
        Estacionamientos[0].setTarifaEstacionamiento("TARIFA: $30/hs");
        Estacionamientos[0].setPosicionEstacionamiento(new LatLng(-31.642296, -60.700448));
        Estacionamientos[0].setTelefono("456-7893");
        Estacionamientos[0].setEsTechado(true);
        Estacionamientos[0].setAceptaTarjetas(true);
        Estacionamientos[0].setCapacidad(80);

        Estacionamientos[1] = new Estacionamiento();
        Estacionamientos[1].setDireccionEstacionamiento("DIRECCIÓN: Rivadavia 3176");
        Estacionamientos[1].setNombreEstacionamiento("NOMBRE: Estacionamiento Rivadavia");
        Estacionamientos[1].setHorarios("HORARIOS: Lun-Dom de 7hs a 21hs");
        Estacionamientos[1].setTarifaEstacionamiento("TARIFA: $15/hs");
        Estacionamientos[1].setPosicionEstacionamiento(new LatLng(-31.639896, -60.702384));
        Estacionamientos[1].setTelefono("456-1234");
        Estacionamientos[1].setEsTechado(false);
        Estacionamientos[1].setAceptaTarjetas(false);
        Estacionamientos[1].setCapacidad(45);

        Estacionamientos[2] = new Estacionamiento();
        Estacionamientos[2].setDireccionEstacionamiento("DIRECCIÓN: La Rioja y 25 de Mayo");
        Estacionamientos[2].setNombreEstacionamiento("NOMBRE: GARAGE MONTeCoRLO");
        Estacionamientos[2].setHorarios("HORARIOS: Lun-Vie abierto las 24hs, Sáb de 9hs a 18hs");
        Estacionamientos[2].setTarifaEstacionamiento("TARIFA: $25/hs");
        Estacionamientos[2].setPosicionEstacionamiento(new LatLng(-31.646182, -60.705633));
        Estacionamientos[2].setTelefono("432-1987");
        Estacionamientos[2].setEsTechado(true);
        Estacionamientos[2].setAceptaTarjetas(false);
        Estacionamientos[2].setCapacidad(60);

        Estacionamientos[3] = new Estacionamiento();
        Estacionamientos[3].setDireccionEstacionamiento("DIRECCIÓN: 9 de Julio 2083");
        Estacionamientos[3].setNombreEstacionamiento("NOMBRE: Garage y Lavadero Sarsotti");
        Estacionamientos[3].setHorarios("HORARIOS: Lun-Vie de 8hs a 20hs, Sáb de 9hs a 18hs");
        Estacionamientos[3].setTarifaEstacionamiento("TARIFA: $20/hs");
        Estacionamientos[3].setPosicionEstacionamiento(new LatLng(-31.651290, -60.710682));
        Estacionamientos[3].setTelefono("458-2757");
        Estacionamientos[3].setEsTechado(true);
        Estacionamientos[3].setAceptaTarjetas(false);
        Estacionamientos[3].setCapacidad(50);

        Estacionamientos[4] = new Estacionamiento();
        Estacionamientos[4].setDireccionEstacionamiento("DIRECCIÓN: Lisandro de la Torre y 1 de Mayo");
        Estacionamientos[4].setNombreEstacionamiento("NOMBRE: Playón Lisandro de la Torre");
        Estacionamientos[4].setHorarios("HORARIOS: Lun-Vie de 8hs a 19hs, Sáb de 10hs a 17hs");
        Estacionamientos[4].setTarifaEstacionamiento("TARIFA: $20/hs");
        Estacionamientos[4].setPosicionEstacionamiento(new LatLng(-31.651290, -60.710671));
        Estacionamientos[4].setTelefono("456-2084");
        Estacionamientos[4].setEsTechado(false);
        Estacionamientos[4].setAceptaTarjetas(false);
        Estacionamientos[4].setCapacidad(35);

        Estacionamientos[5] = new Estacionamiento();
        Estacionamientos[5].setDireccionEstacionamiento("DIRECCIÓN: San Lorenzo 2565");
        Estacionamientos[5].setNombreEstacionamiento("NOMBRE: Cocheras San Lorenzo");
        Estacionamientos[5].setHorarios("HORARIOS: Lun-Vie de 7:30hs a 20hs");
        Estacionamientos[5].setTarifaEstacionamiento("TARIFA: $10/hs");
        Estacionamientos[5].setPosicionEstacionamiento(new LatLng(-31.643693, -60.716726));
        Estacionamientos[5].setTelefono("342 504-4287");
        Estacionamientos[5].setEsTechado(true);
        Estacionamientos[5].setAceptaTarjetas(false);
        Estacionamientos[5].setCapacidad(15);

        Estacionamientos[6] = new Estacionamiento();
        Estacionamientos[6].setDireccionEstacionamiento("DIRECCIÓN: 25 de Mayo 2657");
        Estacionamientos[6].setNombreEstacionamiento("NOMBRE: Estacionamiento 25 de Mayo");
        Estacionamientos[6].setHorarios("HORARIOS: Lun-Vie de 7:30hs a 21hs");
        Estacionamientos[6].setTarifaEstacionamiento("TARIFA: $20/hs");
        Estacionamientos[6].setPosicionEstacionamiento(new LatLng(-31.645223, -60.705428));
        Estacionamientos[6].setTelefono("491-0897");
        Estacionamientos[6].setEsTechado(true);
        Estacionamientos[6].setAceptaTarjetas(true);
        Estacionamientos[6].setCapacidad(40);

        Estacionamientos[7] = new Estacionamiento();
        Estacionamientos[7].setDireccionEstacionamiento("DIRECCIÓN: 25 de Mayo 3219");
        Estacionamientos[7].setNombreEstacionamiento("NOMBRE: Estacionamiento Boneo");
        Estacionamientos[7].setHorarios("HORARIOS: Lun-Vie de 8hs a 14hs y 16hs a 21hs");
        Estacionamientos[7].setTarifaEstacionamiento("TARIFA: $25/hs");
        Estacionamientos[7].setPosicionEstacionamiento(new LatLng(-31.638824, -60.703545));
        Estacionamientos[7].setTelefono("435-9789");
        Estacionamientos[7].setEsTechado(false);
        Estacionamientos[7].setAceptaTarjetas(false);
        Estacionamientos[7].setCapacidad(25);

        Estacionamientos[8] = new Estacionamiento();
        Estacionamientos[8].setDireccionEstacionamiento("DIRECCIÓN: Dique 1, Puerto");
        Estacionamientos[8].setNombreEstacionamiento("NOMBRE: Estacionamiento Casino Santa Fe");
        Estacionamientos[8].setHorarios("HORARIOS: Abierto las 24hs");
        Estacionamientos[8].setTarifaEstacionamiento("TARIFA: $25/hs");
        Estacionamientos[8].setPosicionEstacionamiento(new LatLng(-31.649029, -60.701377));
        Estacionamientos[8].setTelefono("450-1444");
        Estacionamientos[8].setEsTechado(false);
        Estacionamientos[8].setAceptaTarjetas(true);
        Estacionamientos[8].setCapacidad(100);

        Estacionamientos[9] = new Estacionamiento();
        Estacionamientos[9].setDireccionEstacionamiento("DIRECCIÓN: Francisco Miguens 180");
        Estacionamientos[9].setNombreEstacionamiento("NOMBRE: Estacionamiento del Puerto");
        Estacionamientos[9].setHorarios("HORARIOS: Abierto las 24hs");
        Estacionamientos[9].setTarifaEstacionamiento("TARIFA: $30/hs");
        Estacionamientos[9].setPosicionEstacionamiento(new LatLng(-31.647216, -60.700394));
        Estacionamientos[9].setTelefono("450-9513");
        Estacionamientos[9].setEsTechado(true);
        Estacionamientos[9].setAceptaTarjetas(true);
        Estacionamientos[9].setCapacidad(200);

        Estacionamientos[10] = new Estacionamiento();
        Estacionamientos[10].setDireccionEstacionamiento("DIRECCIÓN: Patricio Cullen 6150");
        Estacionamientos[10].setNombreEstacionamiento("NOMBRE: Lo de Luisito");
        Estacionamientos[10].setHorarios("HORARIOS: Lun-Vie de 14hs a 22hs");
        Estacionamientos[10].setTarifaEstacionamiento("TARIFA: a voluntad");
        Estacionamientos[10].setPosicionEstacionamiento(new LatLng(-31.616724, -60.674654));
        Estacionamientos[10].setTelefono("3424412838");
        Estacionamientos[10].setEsTechado(false);
        Estacionamientos[10].setAceptaTarjetas(false);
        Estacionamientos[10].setCapacidad(30);

        Estacionamientos[11] = new Estacionamiento();
        Estacionamientos[11].setDireccionEstacionamiento("DIRECCIÓN: Rivadavia 3154");
        Estacionamientos[11].setNombreEstacionamiento("NOMBRE: Metroparking");
        Estacionamientos[11].setHorarios("HORARIOS: Abierto las 24hs");
        Estacionamientos[11].setTarifaEstacionamiento("TARIFA: $20/hs");
        Estacionamientos[11].setPosicionEstacionamiento(new LatLng(-31.640081, -60.702387));
        Estacionamientos[11].setTelefono("456-9090");
        Estacionamientos[11].setEsTechado(true);
        Estacionamientos[11].setAceptaTarjetas(false);
        Estacionamientos[11].setCapacidad(75);

        return Estacionamientos;
    }


    private String generarJsonDesdeArray(Estacionamiento[] estacionamientosList) {
        //Generar JSON a partir de un ArrayList
        List<Estacionamiento> listEstac = new ArrayList<Estacionamiento>();
        for (int i=0; i<estacionamientosList.length; i++){
            listEstac.add(estacionamientosList[i]);
        }
        Gson gson = new Gson();
        String jsonEstac = gson.toJson(listEstac);
        return jsonEstac;
    }


}
