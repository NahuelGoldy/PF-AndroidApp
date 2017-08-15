package com.example.puchoo.mapmaterial.VistasAndControllers;

/**
 * Created by Puchoo on 11/08/2017.
 */

public class CesionManager {

    private static CesionManager instance;
    private Long tokenUsuario;
    private Boolean actualiarBD = false;

    private  CesionManager(){

    }

    public static CesionManager getInstance(){
        if (instance == null){
            instance = new CesionManager();
        }
        return instance;
    }

    public void consultarVersionado(){

        /** TODO Usar VersionadoEndpointClient para consultar lass versiones **/
        setActualiarBD(false);
    }

    public Long getTokenUsuario() {
        return tokenUsuario;
    }

    public void setTokenUsuario(Long tokenUsuario) {
        this.tokenUsuario = tokenUsuario;
    }

    public Boolean getActualiarBD() {
        return actualiarBD;
    }

    public void setActualiarBD(Boolean actualiarBD) {
        this.actualiarBD = actualiarBD;
    }


}
