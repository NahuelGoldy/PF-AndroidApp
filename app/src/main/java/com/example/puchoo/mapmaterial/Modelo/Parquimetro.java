package com.example.puchoo.mapmaterial.Modelo;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Nahuel SG on 25/10/2017.
 */

public class Parquimetro {
    private String nombre;
    private LatLng coords;

    public Parquimetro(String s, LatLng ll){
        this.nombre = s;
        this.coords = ll;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LatLng getCoords() {
        return coords;
    }

    public void setCoords(LatLng coords) {
        this.coords = coords;
    }
}
