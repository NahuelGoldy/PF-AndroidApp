package com.example.puchoo.mapmaterial.Utils;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by Agustin on 01/30/2017.
 */

public final class ConstantsEstacionamientoService {
    /* Indica el icono a usar del marcador de la calle, TODO - Cambiarlo por algo mejor */
    public static final float MARCADOR_ESTACIONAMIENTO_CALLE = BitmapDescriptorFactory.HUE_YELLOW;
    /* Indica el radio de las geofences */
    public static final Integer GEOFENCE_RADIUS_IN_METERS = 100;
    /* Indica el tiempo de permanencia en milisegundos que la persona tiene que estar en el geofence para activar la notificacion */
    /* TODO - PONER UN TIEMPO BIEN */
    public static final Integer GEOFENCE_STAY_TO_NOTIFICATION_TIME = 5000;
    /* Indica el tiempo de duracion de las geofences, - TODO - revisar la documentacion para saber como se usa y poner un numero correcto*/
    public static final Integer GEOFENCE_EXPIRATION_DURATION_TIME = 5000;
    /* Indica el icono a usar de la notificacion , TODO - Cambiarlo por algo mejor */
    public static final Integer ICONO_NOTIFICACION_GEOFENCE = com.google.android.gms.R.drawable.common_google_signin_btn_icon_dark;

}
