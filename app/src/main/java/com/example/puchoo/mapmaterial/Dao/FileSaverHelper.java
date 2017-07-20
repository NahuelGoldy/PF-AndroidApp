package com.example.puchoo.mapmaterial.Dao;

/**
 * Created by Agustin on 01/25/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.puchoo.mapmaterial.Exceptions.FileSaverException;
import com.example.puchoo.mapmaterial.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_APPEND;

/**
 * Clase que permite guardar informacion en almacenamiento interno o externo aislando el comportamiento
 */
public class FileSaverHelper {
    private static FileSaverHelper ourInstance = new FileSaverHelper();
    private static final int MEMORIA_INTERNA = 0;
    private static final int MEMORIA_EXTERNA = 1;
    private static final String TAG = "FileSaverHelper";
    /** Indica el tipo de guardado por default */
    private static int TIPO_ESCRITURA = MEMORIA_INTERNA;
    private Context contexto;

    public static FileSaverHelper getInstance() {
        return ourInstance;
    }

    private FileSaverHelper() {
    }

    /**
     * Permite modificar el modo de almacenamiento
     * @param usarDiscoInterno
     */
    public void usarEscrituraInterna(Boolean usarDiscoInterno){
        if(usarDiscoInterno){
            TIPO_ESCRITURA = MEMORIA_INTERNA;
        }
        else {
            TIPO_ESCRITURA = MEMORIA_EXTERNA;
        }
    }

    /**
     * Recibe un objeto JSON y lo almacena en disco interno o externo segun lo configurado default
     * Si el objeto ya existia, lo remplaza
     * @param objeto objeto JSON a almacenar
     * @param nombre nombre que se le quiere asignar al archivo
     * @param context contexto desde donde se llama a guardar el archivo (necesario para guardar)
     * @throws FileSaverException
     */
    public void guardarArchivo(String objeto, String nombre,Context context) throws FileSaverException{
        String msg;
        switch(TIPO_ESCRITURA){
            case MEMORIA_EXTERNA:{
                contexto = context;
                msg = contexto.getResources().getString(R.string.fileSaverInicioEscrituraExterna);
                Log.v(TAG,msg);
                guardarArchivoMemoriaExterna(objeto,nombre,Activity.MODE_PRIVATE);
                break;
            }
            case MEMORIA_INTERNA:{
                contexto=context;
                msg = contexto.getResources().getString(R.string.fileSaverInicioEscrituraInterna);
                Log.v(TAG,msg);
                guardarArchivoMemoriaInterna(objeto,nombre,Activity.MODE_PRIVATE);
                break;
            }
            default:
                msg = context.getResources().getString(R.string.fileSaverErrorEscrituraLocal);
                throw new FileSaverException(msg);
        }
    }

    /**TODO implementar metodo */
    private void guardarArchivoMemoriaExterna(String objeto, String nombre,int modoGuardado) throws FileSaverException{

    }

    /**
     * Recibe un objeto JSON y lo almacena en memoria interna
     * @param objeto
     * @param fileName nombre del archivo
     * @param modoGuardado indica si el modo de escritura agrega al final o remplaza por un nuevo archivo
     * @throws FileSaverException
     */
    private void guardarArchivoMemoriaInterna(String objeto,String fileName,int modoGuardado) throws FileSaverException{
        String msg = contexto.getResources().getString(R.string.fileSaverErrorEscrituraLocal);
        FileOutputStream mOutput;
        if(objeto!=null) {
            try {
                Log.v(TAG,"Guardando el archivo "+fileName+" en memoriaInterna");
                mOutput = contexto.openFileOutput(fileName, modoGuardado);
                mOutput.write(objeto.toString().getBytes());
                mOutput.flush();
                mOutput.close();
                msg = contexto.getResources().getString(R.string.fileSaverAlmacenExitoso);
                Log.v(TAG,msg);
            }
            catch(FileNotFoundException e){
                throw new FileSaverException(msg);
            }
            catch(IOException e){
                throw new FileSaverException(msg);
            }
        }
        else{
            throw new FileSaverException(msg);
        }
    }

    /**
     * Recibe un objeto JSON y lo almacena en disco interno o externo segun lo configurado default
     * Si el objeto ya existia, lo actualiza
     * @param objeto objeto JSON a almacenar
     * @param nombre nombre que se le quiere asignar al archivo
     * @param context contexto desde donde se llama a guardar el archivo (necesario para guardar)
     * @throws FileSaverException
     */
    public void guardarOActualizarArchivo(String objeto, String nombre, Context context) throws FileSaverException{
        String msg;
        switch(TIPO_ESCRITURA){
            case MEMORIA_EXTERNA:{
                contexto = context;
                msg = contexto.getResources().getString(R.string.fileSaverInicioEscrituraExterna);
                Log.v(TAG,msg);
                guardarArchivoMemoriaExterna(objeto,nombre, MODE_APPEND);
                break;
            }
            case MEMORIA_INTERNA:{
                contexto=context;
                msg = contexto.getResources().getString(R.string.fileSaverInicioEscrituraInterna);
                Log.v(TAG,msg);
                guardarArchivoMemoriaInterna(objeto,nombre, MODE_APPEND);
                break;
            }
            default:
                msg = contexto.getResources().getString(R.string.fileSaverErrorEscrituraLocal);
                Log.v(TAG,msg);
                throw new FileSaverException(msg);
        }
    }

    /**
     * Recibe el nombre del objeto JSON a obtener y lo devuelve
     * Si no lo encuentra lanza una exception
     * @param nombre
     * @param context
     * @throws FileSaverException
     */
    public String getArchivo(String nombre,Context context) throws FileSaverException{
        contexto=context;
        JSONObject objeto = null;
        String objet = null;
        String msg;
        switch(TIPO_ESCRITURA){
            case MEMORIA_EXTERNA:{
                contexto = context;
                msg = contexto.getResources().getString(R.string.fileSaverInicioLecturaExterna);
                Log.v(TAG,msg);
                objeto = getArchivoMemoriaExterna(nombre);
                break;
            }
            case MEMORIA_INTERNA:{
                contexto=context;
                msg = contexto.getResources().getString(R.string.fileSaverInicioLecturaInterna);
                Log.v(TAG,msg);
                objet = getArchivoMemoriaInterna(nombre);

                break;
            }
            default:
                msg = contexto.getResources().getString(R.string.fileSaverErrorLecturaLocal);
                Log.v(TAG,msg);
                throw new FileSaverException(msg);
        }
        return objet;
    }
    /**TODO Completar metodo*/
    private JSONObject getArchivoMemoriaExterna(String fileName) throws FileSaverException{
        return null;
    }

    /**
     * Recibe el nombre de un archivo JSON y lo retorna.
     * Si el objeto no se encuentra lanza una exception
     * @param fileName
     * @return
     * @throws FileSaverException
     */
    private String getArchivoMemoriaInterna(String fileName) throws FileSaverException{
        int size;
        byte[] buffer;
        FileInputStream mInput;
        String jsonString=null,msg;
        JSONObject objeto;
        try{
            mInput = contexto.openFileInput(fileName);
            size = mInput.available();
            buffer = new byte[size];
            mInput.read(buffer);
            mInput.close();
            msg = contexto.getResources().getString(R.string.fileSaverLecturaExitosa);
            Log.v(TAG,msg);
            jsonString = new String(buffer,"UTF-8");
        }
        catch(FileNotFoundException e){
            msg = contexto.getResources().getString(R.string.fileSaverErrorLecturaFileNotFound);
            Log.v(TAG,msg);
            //e.printStackTrace();
            throw new FileSaverException(msg);
        }
        catch(IOException e){
            msg = contexto.getResources().getString(R.string.fileSaverErrorLecturaLocal);
            Log.v(TAG,msg);
            throw new FileSaverException(msg,"ArchivoInexistente");
        }
        return jsonString;
    }


}
