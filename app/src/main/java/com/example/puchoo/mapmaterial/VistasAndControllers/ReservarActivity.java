package com.example.puchoo.mapmaterial.VistasAndControllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.puchoo.mapmaterial.Dao.EstacionamientoDAO;
import com.example.puchoo.mapmaterial.Exceptions.EstacionamientoException;
import com.example.puchoo.mapmaterial.ListContentFragment;
import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;
import com.example.puchoo.mapmaterial.Modelo.ReservaMock;
import com.example.puchoo.mapmaterial.R;
import com.example.puchoo.mapmaterial.Utils.AlarmEstacionamientoReceiver;
import com.example.puchoo.mapmaterial.Utils.ConstantsNotificaciones;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Nahuel SG on 12/02/2017.
 */

public class ReservarActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_POSITION = "position";
    private TextView nombre, direccion;
    private Button botonConfirmar;
    private int position;
    private Estacionamiento estacionamientoReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);

        //TODO refactorizar: mejorar el manejo para obtener el objeto Estacionamiento (quizás pasarlo en el Intent)
        position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        try {
            estacionamientoReserva = ( EstacionamientoDAO.getInstance().listarEstacionamientos(getBaseContext()) ).get(position);
        } catch (EstacionamientoException e) {
            String msgLog = "Hubo un error al crear el archivo con la lista de listaEstacionamientos.";
            Log.v(TAG,msgLog);
        }

        nombre = (TextView) findViewById(R.id.tv_nombreEstac_reservar);
        nombre.setText("Nombre: "+ estacionamientoReserva.getNombreEstacionamiento());
        direccion = (TextView) findViewById(R.id.tv_direccionEstac_reservar);
        direccion.setText("Dirección: "+ estacionamientoReserva.getDireccionEstacionamiento());
        botonConfirmar = (Button) findViewById(R.id.button_confirmar_reserva);
        botonConfirmar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == botonConfirmar.getId()) {
            agregarAlarma(estacionamientoReserva, position);
            agregarAListaReservas(estacionamientoReserva);
            //generar reserva
            //intent a la lista o OnBackPressed?
            super.onBackPressed();
        }
    }

    private void agregarAListaReservas(Estacionamiento estacionamientoReserva) {
        String nombreEstacionamiento = estacionamientoReserva.getNombreEstacionamiento();
        String direccionEstacionamiento = estacionamientoReserva.getDireccionEstacionamiento();
        Gson gson = new Gson();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd / MMMM / yyyy");
        String fecha= df.format(c.getTime());
        df = new SimpleDateFormat("HH:mm:ss");
        String hora= df.format(c.getTime());
        ReservaMock res = new ReservaMock(direccionEstacionamiento, nombreEstacionamiento, fecha, hora);
        List<ReservaMock> reservasViejasList;

        //leer desde SharedPreferences -> el string guardado es un json
        String listaReservasJson = PreferenceManager.getDefaultSharedPreferences(this).getString("listaReservas", "");
        if(listaReservasJson.equals("")){
            reservasViejasList = new ArrayList<ReservaMock>();
        }
        else{
            //obtener la lista de Resultados desde el Json
            Type type = new TypeToken<List<ReservaMock>>() {}.getType();
            reservasViejasList = gson.fromJson(listaReservasJson, type);
        }
        reservasViejasList.add(res);
        listaReservasJson = gson.toJson(reservasViejasList);
        //volver a persistir en SharedPreferences la lista actualizada
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("listaReservas", listaReservasJson).apply();
    }


    private void agregarAlarma(Estacionamiento estacionamiento, int position){
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmEstacionamientoReceiver.class);
        Marker markerEstacionamiento = buscarMarker(estacionamiento.getPosicionEstacionamiento());
        intent.putExtra("idMarcador",markerEstacionamiento.getId());
        Date date = new Date();
        intent.putExtra("horaReserva", new SimpleDateFormat("HH:mm").format(date));
        intent.putExtra("nombreEstac", estacionamiento.getNombreEstacionamiento());
        intent.setAction(String.valueOf(ConstantsNotificaciones.ACCION_GENERAR_RESERVA));
        Integer idPendingIntent = position;
        PendingIntent pi = PendingIntent.getBroadcast(this,idPendingIntent,intent,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+ConstantsNotificaciones.TIEMPO_CONFIGURADO_ALARMA_RESERVA,pi);
    }

    /**
     *  Busca un Marker por su posicion dentro del Map-mapaMarcadores
     * @param posicion
     * @return retorna el Marker o Null, si no lo encuentra
     */
    private Marker buscarMarker(LatLng posicion){
        for(Marker aux : ListContentFragment.mapaMarcadores.values()) {
            if (aux.getPosition().equals(posicion)) {
                return aux;
            }
        }
        return null;
    }
}
