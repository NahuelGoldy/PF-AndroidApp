package com.example.puchoo.mapmaterial.Dao;

import android.content.Context;
import android.util.Log;

import com.example.puchoo.mapmaterial.Exceptions.FileSaverException;
import com.example.puchoo.mapmaterial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Agustin on 01/27/2017.
 */
public class JsonDBHelper {
    private static JsonDBHelper ourInstance = new JsonDBHelper();
    private static FileSaverHelper fileSaverHelper = FileSaverHelper.getInstance();
    private static Context contexto;
    private static final String TAG = "JsonDBHelper";
    private static final String UBICACION_VEHICULO_FILENAME = "ubicacionVehiculo.json";
    public static JsonDBHelper getInstance() {
        return ourInstance;
    }

    private JsonDBHelper() {
    }

    public void setContext(Context context){
        contexto = context;
        instanciarBDLocal();
    }
    /**
     * Crea la base de datos JSON si no existe
     */

    /** TODO Aca agregar cada una de las instanciaciones de los archivos */
    private void instanciarBDLocal(){
        String msg = contexto.getResources().getString(R.string.jsonDbHelperIniciandoInstanciacion);
        Log.v(TAG,msg);
        /** Primero creo el archivo ubicacion vehiculo */
        String objeto;
        String fileName = UBICACION_VEHICULO_FILENAME;
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("estacionamientosCalle", jsonArray);
            createJSONDB(jsonObject.toString(),fileName);
            msg = contexto.getResources().getString(R.string.jsonDbHelperBDInstanciadaExitosamente);
            Log.v(TAG,msg);
        }
        catch(JSONException e){
            msg = contexto.getResources().getString(R.string.jsonDbHelperBDNoInstanciada);
            Log.v(TAG,msg);
            e.printStackTrace();
        }
        catch(FileSaverException e){
            msg = contexto.getResources().getString(R.string.jsonDbHelperBDNoInstanciada);
            Log.v(TAG,msg);
            e.printStackTrace();
        }
    }
    /**
     * Permite crear por unica vez la estructura del archivo JSON, una vez creado, no se vuelve a ejecutar
     * @param fileName
     */
    private void createJSONDB(String objeto, String fileName) throws FileSaverException {
        String msg;
        FileOutputStream mOutput;
        File f = new File(contexto.getFilesDir(), fileName);
        if(!f.exists()) {
            fileSaverHelper.guardarArchivo(objeto,fileName,contexto);
        }
        else{
            msg = contexto.getResources().getString(R.string.jsonDbHelperBDExistente);
            Log.v(TAG,msg);
        }

    }
}
