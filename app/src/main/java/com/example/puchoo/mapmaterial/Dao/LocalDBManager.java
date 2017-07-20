package com.example.puchoo.mapmaterial.Dao;

import android.content.Context;
import android.util.Log;

import com.example.puchoo.mapmaterial.Exceptions.FileSaverException;
import com.example.puchoo.mapmaterial.R;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Agustin on 02/01/2017.
 */
class LocalDBManager {

    private ExecutorService tareaFileSaver = Executors.newSingleThreadExecutor();
    private Future<?> future;


    private static final String METODO_GET_ARCHIVO = "getArchivo";
    private static final String METODO_GUARDAR_O_ACTUALIZAR_ARCHIVO = "guardarOActualizarArchivo";
    private static final String METODO_GUARDAR_ARCHIVO = "guardarArchivo";

    private static final String TAG = "LocalDBManager";


    private static LocalDBManager ourInstance = new LocalDBManager();

    public static LocalDBManager getInstance() {
        return ourInstance;
    }

    private LocalDBManager() {
    }

    public String getArchivo(String fileName,Context context) throws FileSaverException {
        String resultado = null;
        String msg = context.getResources().getString(R.string.fileSaverErrorLecturaLocal);
        DAOCallable getArchivoCallable = new DAOCallable(fileName,context, METODO_GET_ARCHIVO);

        future = tareaFileSaver.submit(getArchivoCallable);
        try {
            while (!future.isDone()) {     }
            resultado = (String) future.get();
        }
        catch (InterruptedException e) {
            Log.v(TAG,msg);
            throw new FileSaverException(msg);
        }
        catch (ExecutionException e) {
            Log.v(TAG,msg);
            throw new FileSaverException(msg);
        }
        return resultado;
    }

    public void guardarArchivo(String objeto,String fileName,Context context) throws FileSaverException{
        DAOCallable getArchivoCallable = new DAOCallable(objeto,fileName,context, METODO_GUARDAR_ARCHIVO);
        String msg = context.getResources().getString(R.string.fileSaverErrorEscrituraLocal);
        getArchivoCallable.call();
        future = tareaFileSaver.submit(getArchivoCallable);
        try {
            future.get();
        }
        /** TODO - Si se produce una exception recuperable, hacer que vuelve a ejecutar la task */
        catch (InterruptedException e) {
            Log.v(TAG,msg);
            throw new FileSaverException(msg);
        }
        catch (ExecutionException e) {
            throw (FileSaverException) e.getCause();
        }
    }

    public void guardarOActualizarArchivo(String objeto,String fileName,Context context) throws FileSaverException{
        DAOCallable getArchivoCallable = new DAOCallable(objeto,fileName,context, METODO_GUARDAR_O_ACTUALIZAR_ARCHIVO);
        String msg = context.getResources().getString(R.string.fileSaverErrorEscrituraLocal);
        getArchivoCallable.call();
        future = tareaFileSaver.submit(getArchivoCallable);
        try {
            future.get();
        }
        /** TODO - Si se produce una exception recuperable, hacer que vuelve a ejecutar la task */
        catch (InterruptedException e) {
            Log.v(TAG,msg);
            throw new FileSaverException(msg);
        }
        catch (ExecutionException e) {
            throw (FileSaverException) e.getCause();
        }
    }

    public static class DAOCallable implements Callable<Object> {


        private String objeto,fileName;
        private FileSaverHelper fileSaver = FileSaverHelper.getInstance();
        private String accionARealizar;
        private Context context;

        public DAOCallable(String objeto,String fileName,Context context, String accionARealizar) {
            this.objeto=objeto;
            this.fileName=fileName;
            this.accionARealizar = accionARealizar;
            this.context = context;
        }

        public DAOCallable(String fileName,Context context, String accionARealizar) {
            this.fileName=fileName;
            this.accionARealizar = accionARealizar;
            this.context = context;
        }

        @Override
        public Object call() throws FileSaverException {
            switch (accionARealizar) {
                case METODO_GET_ARCHIVO: {
                    return fileSaver.getArchivo(fileName,context);
                }
                case METODO_GUARDAR_ARCHIVO: {
                    fileSaver.guardarArchivo(objeto,fileName,context);
                    break;
                }
                case METODO_GUARDAR_O_ACTUALIZAR_ARCHIVO: {
                    fileSaver.guardarOActualizarArchivo(objeto,fileName,context);
                    break;
                }
                default: {
                    break;
                }
            }
            return null;
        }

    }
}
