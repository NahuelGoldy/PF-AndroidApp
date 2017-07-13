package com.example.puchoo.mapmaterial.Modelo;

import android.location.Address;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

/**
 * Created by Agustin on 01/24/2017.
 */

public class UbicacionVehiculoEstacionado {
    protected Address direccion;
    protected Long horaIngreso; /** LA HORA INGRESO ES UN TIMESTAMP PRACTICAMENTE UNICO POR LO TANTO ES EL ID (USAR JUNTO CON EL ID USUARIO)*/
    protected Long horaEgreso;
    protected Integer idUsuario;
    protected Boolean eliminado;
    private Boolean enLaCalle;

    public UbicacionVehiculoEstacionado(Location ubicacion) {
        direccion = new Address(Locale.getDefault());
        direccion.setLongitude(ubicacion.getLongitude());
        direccion.setLatitude(ubicacion.getLatitude());
        this.eliminado = false;
        this.idUsuario = 0;
        this.enLaCalle = false; //Bool para saber si esta en la calle o en un "Estacionamiento"
    }
    public UbicacionVehiculoEstacionado(){}

    public Address getDireccion() {
        return direccion;
    }

    public void setDireccion(Address direccion) {
        this.direccion = direccion;
    }

    public Long getHoraEgreso() {
        return horaEgreso;
    }

    public void setHoraEgreso(Long horaEgreso) {
        this.horaEgreso = horaEgreso;
    }

    public Long getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(Long horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setEliminado(Boolean eliminado){
        this.eliminado = eliminado;
    }

    public Boolean getEliminado(){return this.eliminado;}

    /**
     * Devuelve latitud y longitud del estacionamiento
     * @return
     */
    public LatLng getCoordenadas(){
        LatLng latLng = new LatLng(direccion.getLatitude(),direccion.getLongitude());
        return latLng;
    }

    /** Devuelve el tiempo en minutos que el vehiculo estuvo en el estacionamiento
     * Si el vehiculo aun sigue estacionado devuelve -1
     */
    public long calcularTiempoEstacionado(){
        long minutos,diferencia;
        if(horaEgreso!=null){
            diferencia = horaIngreso-horaEgreso;
            minutos = diferencia/1000;
            minutos = minutos % 60;
        }
        else{
            minutos = -1;
        }
        return minutos;
    }

    /** Devuelve el nombre de la calle en donde se encuentra estacionado
     * en formato nombre calle - numero - nombre ciudad
     * @return
     */
    public String getTitulo(){
        StringBuilder titulo = new StringBuilder();
        if(direccion!=null) {
            titulo.append(direccion.getAddressLine(0));
            titulo.append(", ");
            titulo.append(direccion.getLocality());
            if (titulo.length()>10) { // Si es >10 entonces el titulo contiene informacion, de lo contrario tiene punteros nulos o info vacia
                return titulo.toString();
            }
            else{
                return direccion.getLatitude() + " " + direccion.getLongitude();
            }
        }
        return "Vehiculo";
    }
    /**
     * USO LA HORA DE INGRESO COMO ID IDENTIFICADOR DE ESE ESTACIONAMIENTO
     */
    public Integer getId(){
        return horaIngreso.intValue();
    }


    public Boolean getEnLaCalle() {
        return enLaCalle;
    }

    public void setEnLaCalle(Boolean enLaCalle) {
        this.enLaCalle = enLaCalle;
    }
}
