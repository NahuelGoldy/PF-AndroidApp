package com.example.puchoo.mapmaterial.Utils;

import com.example.puchoo.mapmaterial.VistasAndControllers.DialogErrorReserva;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Puchoo on 26/07/2017.
 */

public class ValidadorHoras {

    private static ValidadorHoras ourInstance = new ValidadorHoras();

    public static ValidadorHoras getInstance(){
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
}
