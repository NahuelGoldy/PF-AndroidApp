package com.example.puchoo.mapmaterial.Utils;

/**
 * Created by Puchoo on 03/05/2017.
 */

public final class ConstantsPermissionLocation {

    private static final int PERMISO_FINE_LOCATION_PARA_MAPA_READY = 1;
    private static final int PERMISO_FINE_LOCATION_PARA_LOCATION_SERVICE = 2;
    private static final int PERMISO_FINE_LOCATION_PARA_GEOFENCING = 3;

    /** Codigo de request de permiso para cargar el mapa*/
    public static int getPermisoFineLocationParaMapaReady() {
        return PERMISO_FINE_LOCATION_PARA_MAPA_READY;
    }

    /** Codigo de request de permiso para conectarse al servicio de localizacion */
    public static int getPermisoFineLocationParaLocationService() {
        return PERMISO_FINE_LOCATION_PARA_LOCATION_SERVICE;
    }

    /** Codigo de request de permiso para crear geofences */
    public static int getPermisoFineLocationParaGeofencing() {
        return PERMISO_FINE_LOCATION_PARA_GEOFENCING;
    }
}
