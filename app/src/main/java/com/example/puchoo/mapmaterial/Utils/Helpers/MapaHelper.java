package com.example.puchoo.mapmaterial.Utils.Helpers;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MapaHelper {

    public static ArrayList<PolylineOptions> dibujarCallesEstacionamientoMedidoProhibido(){

        ArrayList<PolylineOptions> polylineOptionsList = new ArrayList<>();

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.660354, -60.709826), new LatLng(-31.659477, -60.708946))
                .add(new LatLng(-31.659477, -60.708946), new LatLng(-31.659139, -60.708270))
                .add(new LatLng(-31.659139, -60.708270), new LatLng(-31.658436, -60.707830))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.657804, -60.711248), new LatLng(-31.657998, -60.709974))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.656989, -60.709669), new LatLng(-31.640103, -60.704619))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.639783, -60.706081), new LatLng(-31.652281, -60.709659))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.656593, -60.712256), new LatLng(-31.647358, -60.709490))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.646209, -60.709165), new LatLng(-31.639548, -60.707216))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.640286, -60.703937), new LatLng(-31.649307, -60.706459))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.655098, -60.721051), new LatLng(-31.633212, -60.714836))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.633214, -60.714981), new LatLng(-31.655061, -60.721330))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.643248, -60.700756), new LatLng(-31.641140, -60.700106))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.649327, -60.706230), new LatLng(-31.649288, -60.706174))
                .add(new LatLng(-31.649288, -60.706174), new LatLng(-31.648781, -60.705954))
                .add(new LatLng(-31.648781, -60.705954), new LatLng(-31.648201, -60.705815))
                .add(new LatLng(-31.648201, -60.705815), new LatLng(-31.647648, -60.705346))
                .add(new LatLng(-31.647648, -60.705346), new LatLng(-31.647321, -60.704984))
                .add(new LatLng(-31.647321, -60.704984), new LatLng(-31.646198, -60.704252))
                .add(new LatLng(-31.646198, -60.704252), new LatLng(-31.644932, -60.703804))
                .add(new LatLng(-31.644932, -60.703804), new LatLng(-31.636129, -60.701121))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.647232, -60.704890), new LatLng(-31.647318, -60.704808))
                .add(new LatLng(-31.647318, -60.704808), new LatLng(-31.647336, -60.704686))
                .add(new LatLng(-31.647336, -60.704686), new LatLng(-31.646816, -60.702133))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.647035, -60.701280), new LatLng(-31.647172, -60.702176))
                .add(new LatLng(-31.647172, -60.702176), new LatLng(-31.647355, -60.702761))
                .add(new LatLng(-31.647355, -60.702761), new LatLng(-31.647638, -60.703297))
                .add(new LatLng(-31.647638, -60.703297), new LatLng(-31.647830, -60.703554))
                .add(new LatLng(-31.647830, -60.703554), new LatLng(-31.648017, -60.703747))
                .add(new LatLng(-31.648017, -60.703747), new LatLng(-31.648147, -60.703813))
                .add(new LatLng(-31.648147, -60.703813), new LatLng(-31.650085, -60.704420))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.649621, -60.704875), new LatLng(-31.647398, -60.704200))
                .add(new LatLng(-31.647398, -60.704200), new LatLng(-31.647327, -60.704166))
                .add(new LatLng(-31.647327, -60.704166), new LatLng(-31.647258, -60.704098))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.633171, -60.714754), new LatLng(-31.633943, -60.711321))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.633782, -60.711280), new LatLng(-31.633069, -60.714655))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.632479, -60.710904), new LatLng(-31.632780, -60.709499))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.635656, -60.703218), new LatLng(-31.636620, -60.698691))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.636482, -60.698654), new LatLng(-31.635504, -60.703167))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.645370, -60.707714), new LatLng(-31.646194, -60.704227))
                .add(new LatLng(-31.646194, -60.704227), new LatLng(-31.646801, -60.702164))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.647307, -60.705009), new LatLng(-31.647244, -60.705128))
                .add(new LatLng(-31.647244, -60.705128), new LatLng(-31.646824, -60.706633))
                .add(new LatLng(-31.646824, -60.706633), new LatLng(-31.646493, -60.708058))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.647814, -60.712239), new LatLng(-31.649349, -60.706256))
                .width(5)
                .color(Color.RED)
        );

        polylineOptionsList.add(new PolylineOptions()
                .add(new LatLng(-31.644736, -60.699660), new LatLng(-31.644213, -60.699457))
                .width(5)
                .color(Color.RED)
        );

        return polylineOptionsList;
    }

    private static ArrayList<String[]> readCVSFromAssetFolder(Context context){
        ArrayList<String[]> csvLine = new ArrayList<>();
        String[] content = null;
        try {
            InputStream inputStream = context.getAssets().open("estacionamientoAutos-coords.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while((line = br.readLine()) != null){
                content = line.split(";");
                csvLine.add(content);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvLine;
    }

    public static ArrayList<PolylineOptions> dibujarCallesEstacionamientoMedidoPermitido(Context context){

        ArrayList<PolylineOptions> polylineOptionsList = new ArrayList<>();
        ArrayList<String[]> csvLines = readCVSFromAssetFolder(context);

        for (String[] line : csvLines) {
            double latInicio, longInicio, latFin, longFin;
            latInicio = Double.parseDouble(line[2]);
            longInicio = Double.parseDouble(line[3]);
            latFin = Double.parseDouble(line[4]);
            longFin = Double.parseDouble(line[5]);
            polylineOptionsList.add(new PolylineOptions()
                    .add(new LatLng(latInicio, longInicio), new LatLng(latFin, longFin))
                    .width(5)
                    .color(Color.GREEN)
            );
        }

        return polylineOptionsList;

    }

}
