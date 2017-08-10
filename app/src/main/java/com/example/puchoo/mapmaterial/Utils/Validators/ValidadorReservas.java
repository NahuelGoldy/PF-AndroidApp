package com.example.puchoo.mapmaterial.Utils.Validators;

import android.content.Context;
import android.util.Log;

import com.example.puchoo.mapmaterial.Dao.ReservaDAO;
import com.example.puchoo.mapmaterial.Modelo.ReservaMock;
import com.example.puchoo.mapmaterial.Utils.Constants.ConstantsEstacionamientoService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Puchoo on 26/07/2017.
 */

public class ValidadorReservas {

    private static ValidadorReservas ourInstance = new ValidadorReservas();

    public static ValidadorReservas getInstance(){
        return ourInstance;
    }

    /**
     * Valida que la reserva este activa
     * @return true si la reserva esta activa
     */
    public boolean validarReservaActiva() {

        Date horaActual = getHoraActual();
        Long horaReservaMasQuinceMinutos = (ConstantsEstacionamientoService.HORA_RESERVA + (900000));
        if (horaActual.getTime() < horaReservaMasQuinceMinutos) {
            return true;
        }
        return false;
    }

    /**
     * Me da la otra actual en el formato correcto para compararla
     * @return horaActual Formato correcto
     */
    private Date getHoraActual() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date horaActual = null;
        try {
            //formateo la hora y la vuelvo a parsear, porque si se la trato en la reserva
            horaActual = df.parse( df.format(Calendar.getInstance().getTime()) );
        } catch (ParseException e) {        }
        return horaActual;
    }

    /**
     * Iniciaiza la constante de HORA_RESERVA
     * @param context
     */
    public void initConstansHoras(Context context){
        if(ConstantsEstacionamientoService.HORA_RESERVA == null) {
            SimpleDateFormat df = new SimpleDateFormat("dd / MMMM / yyyy");
            try {
                ArrayList<ReservaMock> ultReservas = ReservaDAO.getInstance().getReservaSharedPref(context);
                if(!ultReservas.isEmpty()) {
                    ReservaMock ultReserva = ultReservas.get(0);
                    Date fechaUltimaReserva = df.parse(ultReserva.getFechaReservado());
                    if (fechaUltimaReserva.getDay() == new Date().getDay()) {
                        df = new SimpleDateFormat("HH:mm:ss");
                        Date horaUltimaReserva = df.parse(ultReserva.getHoraReservado());

                        ConstantsEstacionamientoService.HORA_RESERVA = horaUltimaReserva.getTime();
                    }
                }
            } catch (ParseException e) {
                String msg = "Error en parsear fecha/hora";
                Log.v("TileContentFragment", msg);
            }
        }
    }
}
