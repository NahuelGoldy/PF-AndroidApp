package com.example.puchoo.mapmaterial.Utils.Helpers;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapaHelper {

    public static PolylineOptions  dibujarCallesEstacionamientoMedido(){

        //TODO setear todas las coordenadas posta
        return (new PolylineOptions()
                .add(new LatLng(-31.645863, -60.705607), new LatLng(-31.644648, -60.705277))
                //.add(new LatLng(-31, -60), new LatLng(-31, -60))
                //.add(new LatLng(-31, -60), new LatLng(-31, -60))
                .width(5)
                .color(Color.RED));

        //TODO donde se llama a este método, poner: Polyline polyline = myMap.addPolyline(MapaHelper.dibujarCallesEstacionamientoMedido());
    }

}
