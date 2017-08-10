package com.example.puchoo.mapmaterial.Utils.Constants;

/**
 * Created by Agustin on 02/04/2017.
 */

public final class ConstantsNotificaciones {
    /** Id que identifica la accion de ignorar la notificacion */
    public static String ACCION_NOTIFICACION_IGNORAR_ALARMA = String.valueOf(1);
    /** Id que identifica la accion de dar de baja el auto estacionado */
    public static String ACCION_NOTIFICACION_SALIR_ESTACIONAMIENTO = String.valueOf(2);
    /** Id que identifica la accion de generar una alarma */
    public static String ACCION_GENERAR_ALARMA = String.valueOf(3);
    /** Id que identifica la accion de generar una alarma */
    public static String ACCION_GENERAR_RESERVA = String.valueOf(4);
    /** Tiempo para que suene la alarma luego de configurada */
    //public static final Long TIEMPO_CONFIGURADO_ALARMA = Long.valueOf(1000*60*60); // Suena luego de una hora
    public static final Long TIEMPO_CONFIGURADO_ALARMA = Long.valueOf(1000*60*1); // Suena luego de un minuto
    /** Tiempo para que suene la alarma de la reserva luego de configurada */
    //public static final Long TIEMPO_CONFIGURADO_ALARMA_RESERVA = Long.valueOf(1000*60*15); // Suena luego de 15 minutos
    public static final Long TIEMPO_CONFIGURADO_ALARMA_RESERVA = Long.valueOf(1000*15); // Suena luego de 15 segundos
}
