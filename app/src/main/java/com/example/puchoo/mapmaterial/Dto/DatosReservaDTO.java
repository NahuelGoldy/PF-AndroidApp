package com.example.puchoo.mapmaterial.Dto;

/**
 * Created by Nahuel SG on 11/09/2017.
 */

public class DatosReservaDTO {

    private int idParqueEstacionamiento;
    private int idUsuario;
    private String patente;

    public DatosReservaDTO(){}

    public int getIdParqueEstacionamiento() {
        return idParqueEstacionamiento;
    }

    public void setIdParqueEstacionamiento(int idParqueEstacionamiento) {
        this.idParqueEstacionamiento = idParqueEstacionamiento;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }
}
