package com.example.puchoo.mapmaterial.Modelo;

import java.util.Objects;

/**
 * Created by Nahuel SG on 12/02/2017.
 */

public class ReservaMock {
    private String direccionEstacionamientoReservado;
    private String nombreEstacionamientoReservado;
    private String fechaReservado;
    private String horaReservado;

    public ReservaMock(){}

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

    @Override
    public boolean equals(Object obj){
        ReservaMock reservaComparacion = (ReservaMock) obj;
        if (this.getDireccionEstacionamientoReservado().equals(reservaComparacion.getDireccionEstacionamientoReservado())) {
            if (this.getNombreEstacionamientoReservado().equals(reservaComparacion.getNombreEstacionamientoReservado())){
                if (this.getFechaReservado().equals(reservaComparacion.getFechaReservado())){
                    if (this.getHoraReservado().equals(reservaComparacion.getHoraReservado())){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
