package com.example.puchoo.mapmaterial.VistasAndControllers;

import com.example.puchoo.mapmaterial.Dto.UsuarioDTO;
import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;
import com.example.puchoo.mapmaterial.Modelo.Usuario;

import java.util.ArrayList;

/**
 * Created by Puchoo on 11/08/2017.
 */

public class SesionManager {

    private static SesionManager instance;
    private String tokenUsuario = null;
    private Boolean actualizarBD = false;
    private int idUsuario;
    private ArrayList<Estacionamiento> listaEstacionamientos;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    private SesionManager(){

    }

    public static SesionManager getInstance(){
        if (instance == null){
            instance = new SesionManager();
        }
        return instance;
    }

    public void consultarVersionado(){

        /** TODO Usar VersionadoEndpointClient para consultar lass versiones **/
        setActualizarBD(false);
    }

    public String getTokenUsuario() {
        return tokenUsuario;
    }

    public void setTokenUsuario(String tokenUsuario) {
        this.tokenUsuario = tokenUsuario;
    }

    public Boolean getActualizarBD() {
        return actualizarBD;
    }

    public void setActualizarBD(Boolean actualizarBD) {
        this.actualizarBD = actualizarBD;
    }


    public ArrayList<Estacionamiento> getListaEstacionamientos() {
        return listaEstacionamientos;
    }

    public void setListaEstacionamientos(ArrayList<Estacionamiento> listaEstacionamientos) {
        this.listaEstacionamientos = listaEstacionamientos;
    }
}
