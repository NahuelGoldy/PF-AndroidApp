package com.example.puchoo.mapmaterial.Modelo;

/**
 * Created by Nahuel SG on 12/02/2017.
 */

public class ReservaMock {
    private String direccionEstacionamientoReservado;
    private String nombreEstacionamientoReservado;
    private String fechaReservado;
    private String horaReservado;

    public ReservaMock(String dir, String nomb, String fecha, String hora){
        direccionEstacionamientoReservado = dir;
        nombreEstacionamientoReservado = nomb;
        fechaReservado = fecha;
        horaReservado = hora;
    }

    public String getDireccionEstacionamientoReservado() {
        return direccionEstacionamientoReservado;
    }

    public void setDireccionEstacionamientoReservado(String direccionEstacionamientoReservado) {
        this.direccionEstacionamientoReservado = direccionEstacionamientoReservado;
    }

    public String getNombreEstacionamientoReservado() {
        return nombreEstacionamientoReservado;
    }

    public void setNombreEstacionamientoReservado(String nombreEstacionamientoReservado) {
        this.nombreEstacionamientoReservado = nombreEstacionamientoReservado;
    }

    public String getFechaReservado() {
        return fechaReservado;
    }

    public void setFechaReservado(String fechaReservado) {
        this.fechaReservado = fechaReservado;
    }

    public String getHoraReservado() {
        return horaReservado;
    }

    public void setHoraReservado(String horaReservado) {
        this.horaReservado = horaReservado;
    }
}
