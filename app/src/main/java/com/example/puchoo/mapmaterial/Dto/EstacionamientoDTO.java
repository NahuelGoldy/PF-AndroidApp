package com.example.puchoo.mapmaterial.Dto;

import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nahuel SG on 09/08/2017.
 */

public class EstacionamientoDTO {

    @SerializedName("idEstacionamiento")
    private int idEstacionamiento;
    @SerializedName("nombreEstacionamiento")
    private String nombreEstacionamiento;
    @SerializedName("direccionEstacionamiento")
    private String direccionEstacionamiento;
    @SerializedName("posicionLatitud")
    private double posicionLatitud;
    @SerializedName("posicionLongitud")
    private double posicionLongitud;
    @SerializedName("telefono")
    private String telefono;
    @SerializedName("horarios")
    private String horarios;
    @SerializedName("cuadroTarifario")
    private String tarifaEstacionamiento;
    @SerializedName("esTechado")
    private boolean esTechado;
    @SerializedName("aceptaTarjetas")
    private boolean aceptaTarjetas;
    @SerializedName("eliminado")
    private Boolean eliminado;
    @SerializedName("capacidad")
    private int capacidad;
    @SerializedName("image")
    private String image;

    public EstacionamientoDTO() {}

    public int getIdEstacionamiento() {
        return idEstacionamiento;
    }

    public void setIdEstacionamiento(int idEstacionamiento) {
        this.idEstacionamiento = idEstacionamiento;
    }

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

    public double getPosicionLatitud() {
        return posicionLatitud;
    }

    public void setPosicionLatitud(double posicionLatitud) {
        this.posicionLatitud = posicionLatitud;
    }

    public double getPosicionLongitud() {
        return posicionLongitud;
    }

    public void setPosicionLongitud(double posicionLongitud) {
        this.posicionLongitud = posicionLongitud;
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

    public String getTarifaEstacionamiento() {
        return tarifaEstacionamiento;
    }

    public void setTarifaEstacionamiento(String tarifaEstacionamiento) {
        this.tarifaEstacionamiento = tarifaEstacionamiento;
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

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LatLng getCoords(){
        return new LatLng(this.posicionLatitud, this.posicionLongitud);
    }


    public Estacionamiento toEstacionamiento(){
        Estacionamiento est = new Estacionamiento();
        est.setIdEstacionamiento(this.idEstacionamiento);
        est.setNombreEstacionamiento(this.nombreEstacionamiento);
        est.setDireccionEstacionamiento(this.direccionEstacionamiento);
        est.setTarifaEstacionamiento(this.tarifaEstacionamiento);
        est.setAceptaTarjetas(this.aceptaTarjetas);
        est.setTelefono(this.telefono);
        est.setCapacidad(this.capacidad);
        est.setEsTechado(this.esTechado);
        est.setHorarios(this.horarios);
        LatLng latLng = new LatLng(this.posicionLatitud,this.posicionLongitud);
        est.setPosicionEstacionamiento(latLng);
        if(this.image != null){
            est.setImagen(this.image.getBytes());}
        est.setEliminado(this.eliminado);

        return est;
    }
}
