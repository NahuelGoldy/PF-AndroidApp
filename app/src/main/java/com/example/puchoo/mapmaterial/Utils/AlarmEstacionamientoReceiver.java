package com.example.puchoo.mapmaterial.Utils;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.puchoo.mapmaterial.Dao.UbicacionVehiculoEstacionadoDAO;
import com.example.puchoo.mapmaterial.Exceptions.UbicacionVehiculoException;
import com.example.puchoo.mapmaterial.MapsContentFragment;
import com.example.puchoo.mapmaterial.Modelo.UbicacionVehiculoEstacionado;
import com.example.puchoo.mapmaterial.R;
import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Agustin on 02/03/2017.
 */

public class AlarmEstacionamientoReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmEstReceiver";
    private static final Map<String,Marker> mapaMarcadores = MapsContentFragment.mapaMarcadores;
    private static UbicacionVehiculoEstacionado ubicacionEstacionamiento;
    private static Marker markerEstacionamiento;
    private static Context context;
    /** Id con el que se identifica una notificacion de otra */
    private static Integer idNotificacion = 1;
    private static String tiempoGeneraReserva;
    private static String nombreEst;

    /** Dao que almacena ubicacion de vehiculos estacionados */
    private static final UbicacionVehiculoEstacionadoDAO ubicacionVehiculoDAO = UbicacionVehiculoEstacionadoDAO.getInstance();

    private static NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String s = intent.getAction();
        if (s.equals(ConstantsNotificaciones.ACCION_GENERAR_ALARMA) && (ubicacionEstacionamiento != null)) {
            String msg,idMarcador;
            idMarcador = intent.getStringExtra("idMarcador");
            markerEstacionamiento = mapaMarcadores.get(idMarcador);
            ubicacionEstacionamiento = (UbicacionVehiculoEstacionado) markerEstacionamiento.getTag();
            idNotificacion = 1;
            generarNotificacion(context.getResources().getString(R.string.notificacionAlarmaEstTexto), context.getResources().getString(R.string.notificacionAlarmaEstTitulo));
            msg = context.getResources().getString(R.string.alarmEstacionamientoGenerada);
            Log.d(TAG, msg);
        }
        else if (s.equals(ConstantsNotificaciones.ACCION_GENERAR_RESERVA)) {
            //TODO configurar y generar notificacion
            String msg,idMarcador;
            idMarcador = intent.getStringExtra("idMarcador");
            tiempoGeneraReserva = intent.getStringExtra("horaReserva");
            nombreEst = intent.getStringExtra("nombreEstac");
            markerEstacionamiento = mapaMarcadores.get(idMarcador);
            idNotificacion = 2;
            generarNotificacion("Su reserva está por caducar", "Recordatorio reserva");
            msg = "Se produjo una alarma asociada a una reserva";
            Log.d(TAG, msg);
        }
        else if (s.equals(ConstantsNotificaciones.ACCION_NOTIFICACION_IGNORAR_ALARMA)) {
            posponerAlarma();
        }
        else if (s.equals(ConstantsNotificaciones.ACCION_NOTIFICACION_SALIR_ESTACIONAMIENTO)) {
            salirEstacionamiento();
        }

    }

    /**
     * Genera una notificacion
     */
    private void generarNotificacion(String textoDefault, String titulo){
        String tiempoDeIngreso;
        StringBuilder texto = new StringBuilder();
        if(ubicacionEstacionamiento != null){
            /* Creo el texto a mostrar en caso de que se pueda generar una notificacion big text */
            texto.append("Su vehiculo se encuentra estacionado desde las ");
            Date date = new Date(ubicacionEstacionamiento.getHoraIngreso());
            tiempoDeIngreso = new SimpleDateFormat("HH:mm").format(date);
            texto.append(tiempoDeIngreso);
        }
        else{
            texto.append("Su reserva generada a las ");
            texto.append(tiempoGeneraReserva);
            texto.append(" en el estacionamiento ");
            texto.append(nombreEst);
            texto.append(" está a punto de expirar.");
        }

        /* Creo los botones de la notificacion */
        Intent ignorarAlarmaIntent = new Intent(context,AlarmEstacionamientoReceiver.class);
        ignorarAlarmaIntent.setAction(String.valueOf(ConstantsNotificaciones.ACCION_NOTIFICACION_IGNORAR_ALARMA));
        PendingIntent piIgnorarAlarma = PendingIntent.getBroadcast(context, 0, ignorarAlarmaIntent, 0);

        Intent salirEstacionamientoIntent = new Intent(context,AlarmEstacionamientoReceiver.class);
        salirEstacionamientoIntent.setAction(String.valueOf(ConstantsNotificaciones.ACCION_NOTIFICACION_SALIR_ESTACIONAMIENTO));
        PendingIntent piSalirEstacionamiento = PendingIntent.getBroadcast(context, 0, salirEstacionamientoIntent, 0);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.icn_notif)
                        .setContentTitle(titulo)
                        .setContentText(textoDefault)
                        .setAutoCancel(true)
                        /* Genera la notifcacion en forma de big text, si el dispositivo es menor a 4.1, esto no se genera */
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(texto))
                        /* TODO - CONFIGURAR ICONOS (NO ESTAN SIENDO VISIBLES) */
                        .addAction (R.drawable.ic_ignorar_24dp,
                                getString(R.string.btnIgnorarAlarma), piIgnorarAlarma)
                        .addAction (R.drawable.ic_parking_24dp,
                                getString(R.string.btnSalirEstacionamiento), piSalirEstacionamiento);

        mNotificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(idNotificacion, mBuilder.build());

    }

    private String getString(Integer idString){
        return context.getResources().getString(idString);
    }

    /**
     * Programa una nueva alarma para el marcador
     */
    private void posponerAlarma(){
        String msg = context.getResources().getString(R.string.alarmEstacionamientoPospuesta);
        mNotificationManager.cancel(idNotificacion);
        idNotificacion = 2;
        UbicacionVehiculoEstacionado ubicacionVehiculo = (UbicacionVehiculoEstacionado) markerEstacionamiento.getTag();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmEstacionamientoReceiver.class);
        intent.putExtra("idMarcador",markerEstacionamiento.getId());
        intent.setAction(String.valueOf(ConstantsNotificaciones.ACCION_GENERAR_ALARMA));
        Integer idPendingIntent = ubicacionVehiculo.getId();
        PendingIntent pi = PendingIntent.getBroadcast(context,idPendingIntent,intent,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,ConstantsNotificaciones.TIEMPO_CONFIGURADO_ALARMA,pi);
        Log.v(TAG,msg);
    }

    /**
     * Marca la salida de la ubicacion del estacionamiento
     */
    private void salirEstacionamiento(){
        /*
        String msg = context.getResources().getString(R.string.parkLoggerInicioSalidaEstacionamiento);
        Log.v(TAG,msg);
        UbicacionVehiculoEstacionado ubicacionVehiculo = (UbicacionVehiculoEstacionado) markerEstacionamiento.getTag();
        ubicacionVehiculo.setHoraEgreso(System.currentTimeMillis());
        try{
            eliminarAlarma(ubicacionVehiculo);
            actualizarUbicacionPersistida(ubicacionVehiculo);
            markerEstacionamiento.remove();
            mNotificationManager.cancel(idNotificacion);
            Toast.makeText(context,msg,Toast.LENGTH_LONG);
        }
        catch (UbicacionVehiculoException e) {
            msg = context.getResources().getString(R.string.errorProducidoIntenteNuevamente);
            Toast.makeText(context, msg, Toast.LENGTH_LONG);
        }
        markerUltimoEstacionamiento = null;
        estCalle = null;
        this.lugarEstacionamientoGuardado = false;
        (menuLateral.getItem(ConstantsMenuNavegacion.INDICE_MENU_ESTACIONAR_AQUI)).setTitle(estacionarAqui);
        menuLateral.getItem(ConstantsMenuNavegacion.INDICE_MENU_LIMPIAR).setEnabled(false);
        */
        //TODO VER DE RESOLVER COMO LLAMAR A ESTE METODO QUE ESTA DENTRO DEL FRAGMENT
       // MapsContentFragment.marcarSalidaEstacionamiento(markerEstacionamiento);
    }
    /**
     * Actualiza la informacion en disco del objeto ubicacion vehiculo
     * @param estCalle
     */
    private void actualizarUbicacionPersistida(UbicacionVehiculoEstacionado estCalle) throws UbicacionVehiculoException {
        String msg = context.getResources().getString(R.string.parkLoggerInicioActualizacionUbicacion);
        Log.v(TAG,msg);
        ubicacionVehiculoDAO.actualizarUbicacionVehiculoEstacionado(estCalle,context);
    }

    private void eliminarAlarma(UbicacionVehiculoEstacionado ubicacionVehiculo){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmEstacionamientoReceiver.class);
        //  intent.putExtra("idMarcador",markerEstacionamiento.getId());
        //  intent.setAction(String.valueOf(ConstantsNotificaciones.ACCION_GENERAR_ALARMA));
        Integer idPendingIntent = ubicacionVehiculo.getId();
        PendingIntent pi = PendingIntent.getBroadcast(context,idPendingIntent,intent,0);
        alarmManager.cancel(pi);
    }
}
