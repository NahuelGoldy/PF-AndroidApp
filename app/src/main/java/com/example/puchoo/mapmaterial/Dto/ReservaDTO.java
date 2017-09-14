package com.example.puchoo.mapmaterial.Dto;

/**
 * Created by Nahuel SG on 11/09/2017.
 */

public class ReservaDTO {

    private String nombreEstacionamiento;
    private String direccionEstacionamiento;
    private String telefonoEstacionamiento;
    private String horaCreacionReserva;
    private String horaExpiracionReserva;
    private int idParqueEstacionamiento;
    private int idReservaGenerada;
    private double latitudEstacionamiento;
    private double longitudEstacionamiento;
    private String msgResultado;

    public ReservaDTO(){}

    public String getNombreEstacionamiento() {
        return nombreEstacionamiento;
    }

    public void setNombreEstacionamiento(String nombreEstacionamiento) {
        this.nombreEstacionamiento = nombreEstacionamiento;
    }

    public String getDireccionEstacionamiento() {
        return direccionEstacionamiento;
    }

    public void setDireccionEstacionamiento(String direccionEstacionamiento) {
        this.direccionEstacionamiento = direccionEstacionamiento;
    }

    public String getTelefonoEstacionamiento() {
        return telefonoEstacionamiento;
    }

    public void setTelefonoEstacionamiento(String telefonoEstacionamiento) {
        this.telefonoEstacionamiento = telefonoEstacionamiento;
    }

    public String getHoraCreacionReserva() {
        return horaCreacionReserva;
    }

    public void setHoraCreacionReserva(String horaCreacionReserva) {
        this.horaCreacionReserva = horaCreacionReserva;
    }

    public String getHoraExpiracionReserva() {
        return horaExpiracionReserva;
    }

    public void setHoraExpiracionReserva(String horaExpiracionReserva) {
        this.horaExpiracionReserva = horaExpiracionReserva;
    }

    public int getIdParqueEstacionamiento() {
        return idParqueEstacionamiento;
    }

    public void setIdParqueEstacionamiento(int idParqueEstacionamiento) {
        this.idParqueEstacionamiento = idParqueEstacionamiento;
    }

    public int getIdReservaGenerada() {
        return idReservaGenerada;
    }

    public void setIdReservaGenerada(int idReservaGenerada) {
        this.idReservaGenerada = idReservaGenerada;
    }

    public double getLatitudEstacionamiento() {
        return latitudEstacionamiento;
    }

    public void setLatitudEstacionamiento(double latitudEstacionamiento) {
        this.latitudEstacionamiento = latitudEstacionamiento;
    }

    public double getLongitudEstacionamiento() {
        return longitudEstacionamiento;
    }

    public void setLongitudEstacionamiento(double longitudEstacionamiento) {
        this.longitudEstacionamiento = longitudEstacionamiento;
    }

    public String getMsgResultado() {
        return msgResultado;
    }

    public void setMsgResultado(String msgResultado) {
        this.msgResultado = msgResultado;
    }
}
