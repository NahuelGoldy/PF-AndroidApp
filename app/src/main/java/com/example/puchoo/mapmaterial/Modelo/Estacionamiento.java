package com.example.puchoo.mapmaterial.Modelo;

/**
 * Created by Nahuel SG on 30/01/2017.
 */

import com.google.android.gms.maps.model.LatLng;

public class Estacionamiento {
    private String nombreEstacionamiento;
    private String direccionEstacionamiento;
    private LatLng posicionEstacionamiento;
    private String telefono;
    private String horarios;
    private String tarifaEstacionamiento;
    private boolean esTechado;
    private boolean aceptaTarjetas;
    private Boolean eliminado;
    private int capacidad;

    public Estacionamiento(){
        this.eliminado = false;
    }

    /**
     * Getters y Setters
     */
    public String getNombreEstacionamiento() {
        return nombreEstacionamiento;
    }

    public void setNombreEstacionamiento(String nombreEstacionamiento) {
        this.nombreEstacionamiento = nombreEstacionamiento;
    }

    /**
     * Devuelve la latitud y longitud del estacionamiento
     * @return
     */
    public LatLng getPosicionEstacionamiento() {
        return posicionEstacionamiento;
    }

    public void setPosicionEstacionamiento(LatLng posicionEstacionamiento) {
        this.posicionEstacionamiento = posicionEstacionamiento;
    }

    public String getDireccionEstacionamiento() {
        return direccionEstacionamiento;
    }

    public void setDireccionEstacionamiento(String direccionEstacionamiento) {
        this.direccionEstacionamiento = direccionEstacionamiento;
    }

    public String getTarifaEstacionamiento() {
        return tarifaEstacionamiento;
    }

    public void setTarifaEstacionamiento(String tarifaEstacionamiento) {
        this.tarifaEstacionamiento = tarifaEstacionamiento;
    }

    public Boolean getEliminado(){
        return this.eliminado;
    }

    public void setEliminado(Boolean eliminado)
    {
        this.eliminado = eliminado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public boolean isEsTechado() {
        return esTechado;
    }

    public void setEsTechado(boolean esTechado) {
        this.esTechado = esTechado;
    }

    public boolean isAceptaTarjetas() {
        return aceptaTarjetas;
    }

    public void setAceptaTarjetas(boolean aceptaTarjetas) {
        this.aceptaTarjetas = aceptaTarjetas;
    }

    public void setCapacidad(int cap){
        this.capacidad = cap;
    }

    public int getCapacidad(){
        return this.capacidad;
    }
}
