package com.example.puchoo.mapmaterial;

/**
 * Created by Puchoo on 10/04/2017.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.puchoo.mapmaterial.Dao.EstacionamientoDAO;
import com.example.puchoo.mapmaterial.Dao.JsonDBHelper;
import com.example.puchoo.mapmaterial.Dao.UbicacionVehiculoEstacionadoDAO;
import com.example.puchoo.mapmaterial.Exceptions.EstacionamientoException;
import com.example.puchoo.mapmaterial.Exceptions.UbicacionVehiculoException;
import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;
import com.example.puchoo.mapmaterial.Modelo.UbicacionVehiculoEstacionado;
import com.example.puchoo.mapmaterial.Utils.AddressResultReceiver;
import com.example.puchoo.mapmaterial.Utils.AlarmEstacionamientoReceiver;
import com.example.puchoo.mapmaterial.Utils.ConstantsAddresses;
import com.example.puchoo.mapmaterial.Utils.ConstantsEstacionamientoService;
import com.example.puchoo.mapmaterial.Utils.ConstantsNavigatorView;
import com.example.puchoo.mapmaterial.Utils.ConstantsNotificaciones;
import com.example.puchoo.mapmaterial.Utils.ConstantsPermissionLocation;
import com.example.puchoo.mapmaterial.Utils.FetchAddressIntentService;
import com.example.puchoo.mapmaterial.Utils.GeofenceTransitionsIntentService;
import com.example.puchoo.mapmaterial.VistasAndControllers.InfoWindowsAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides UI for the view with List.
 */
public class ListContentFragment extends Fragment implements TimePicker.OnTimeChangedListener, NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnInfoWindowClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, AddressResultReceiver.Receiver,ResultCallback,
        View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback, OnMapReadyCallback, GoogleMap.OnMapLongClickListener{

    MapView mMapView;

    /** Mapa de google a mostrar */
    private GoogleMap googleMap;

    /** Cliente de api de google para utilizar el servicio de localizacion */
    private GoogleApiClient mGoogleApiClient;

    /** Ultima ubicacion en donde se encuentra la persona */
    private Location ubicacionActual;

    /** Adaptador que permite cargar con datos la ventana de informacion de los marcadores */
    private InfoWindowsAdapter ventanaInfo;

    /** Ubicacion del vehiculo cuando estaciona en la calle */
    private UbicacionVehiculoEstacionado estCalle;

    /** Marcador que indica el ultimo lugar donde la persona realizo un estacionamiento */
    private Marker markerUltimoEstacionamiento;

    /** Marcador que indica el ultimo marcador que fue seleccionado por el usuario */
    private Marker marcadorSelected;

    /** Clase que recibe de manera asincronica resultados del servicio de calles y envia los mismos a esta actividad */
    private static AddressResultReceiver mResultReceiver;

    /** Indica si se solicito obtener una direccion o no */
    private Boolean mAddressRequested = false;

    /** Servicio que permite obtener la direccion acorde a una ubicacion pasada */
    private FetchAddressIntentService buscarCallesService;

    /** Tag usado por el LOG    */
    private static final String TAG = "ServicioUbicacion";

    /** Tag usado por el LOG  representando al menu   */
    private static final String TAG_MENU = "Menu_Navigation";

    /** Dao que almacena ubicacion de vehiculos estacionados */
    private static final UbicacionVehiculoEstacionadoDAO ubicacionVehiculoDAO = UbicacionVehiculoEstacionadoDAO.getInstance();

    /** Dao que almacena ubicacion de listaEstacionamientos */
    private static final EstacionamientoDAO estacionamientoDAO = EstacionamientoDAO.getInstance();

    /** Helper que administra la base de datos JSON LOCAL */
    private final JsonDBHelper jsonDbHelper = JsonDBHelper.getInstance();

    /** Representa el id del usuario que esta utilizando la aplicacion actualmente, TODO - hacer que la app obtenga el id en onCreate() */
    private static Integer ID_USUARIO_ACTUAL = 0;

    /** Pending intent que representa la notificacion que se genera debido a los marcadores */
    private PendingIntent geofencePendingIntent;

    /** Lista de todas las geofences creadas */
    private List mGeofenceList;

    /** Booleano que indica si se guardó la ubicación donde se estacionó */
    private boolean lugarEstacionamientoGuardado = false;

    /** Boton que permite switchear entre la lista de lugares y el mapa */
    FloatingActionButton fab;

    /** Lista de listaEstacionamientos para marcar en el mapa */
    //private Estacionamiento[] listaEstacionamientos;
    private ArrayList<Estacionamiento> listaEstacionamientos;

    /** Bandera que determina si se pidieron o no permisos */
    private Boolean flagPermisoPedido = false;

    /** Intent auxiliar que se utiliza al cargar el mapa */
    private Intent intentAuxMapa;

    /** Lista de marcadores en el mapa */
    public static Map<String,Marker> mapaMarcadores = new HashMap<>();

    /** Instancia actual usada por el alarm receiver */
    //public static MapaActivity mapaActivityInstance;

    /** Objeto que representa el cuadro de dialogo que brinda informacion en caso de que el vehiculo se encuentre estacionado X Tiempo */
    private static AlertDialog dialogInfoVehiculoEstacionado;
    private int pickerHour = 0;
    private int pickerMin = 0;

    /** Codigo de request de permiso para cargar el mapa*/
    private ConstantsPermissionLocation ConstantePermisos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.maps_content_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        String msgLog;

        //Aca hago el primer try para intentar leer el archivo que tiene la lista de estacionamientos
        //Si no existe, lo creo, lo lleno (harcodeado..!) y lo leo

        try {
            /** Levanto la lista de estacionamientos de archivo/nube/db **/
            /** Cambiarlo en algun momento **/
            estacionamientoDAO.inicializarListaEstacionamientos(getActivity());
            listaEstacionamientos = estacionamientoDAO.listarEstacionamientos(getActivity());
            //listaEstacionamientos = estacionamientoDAO.inicializarListaEstacionamientos(getActivity());
            //listaEstacionamientos = estacionamientoDAO.llenarEstacionamientos(getActivity());
        }
        catch (EstacionamientoException e1) {
            msgLog = "Hubo un error al crear el archivo con la lista de listaEstacionamientos.";
            Log.v(TAG,msgLog);
        }

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);

        return rootView;
    }
    /**
     * Accion que se ejecuta luego de que el mapa de google esta listo, se extrae en este metodo
     * debido a que el codigo se repite si es necesario pedir permisos
     */
    public void cargarMapa(GoogleMap googleMap){

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnInfoWindowClickListener(this);
            googleMap.setInfoWindowAdapter(ventanaInfo);
            marcarEstacionamientos();
            estCalle = cargarUltimoEstacionamiento(ID_USUARIO_ACTUAL);
            enfocarMapaEnUbicacion(ubicacionActual, 16f);
        }
        else enfocarMapaEnUbicacion(ubicacionActual, 16f);
    }

    /**-------------------------------------------------------------------*/
    /**                      Metodos de Markes                            */
    /**-------------------------------------------------------------------*/

    /**
     *  Busca un Marker por su posicion dentro del Map-mapaMarcadores
     * @param posicion
     * @return retorna el Marker o Null, si no lo encuentra
     */
    private Marker buscarMarker(LatLng posicion){
        for(Marker aux : mapaMarcadores.values()) {
            if (aux.getPosition().equals(posicion)) {
                if(aux.isVisible()) {
                    return aux;
                }
            }
        }
        return null;
    }

    /**
     * Aunque ya existe el addMarker, esto me parece lo hace mas rapido mas legible y lo agrega a una lista
     *
     * @param latLng Posicion
     * @param title  Titulo
     * @param idIcon id del icono que va a tener
     */
    private void addMarker(LatLng latLng, String title, int idIcon) {
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(latLng) //Pongo el lugar
                .title(title));//Le meto titulo
        marker.setIcon(BitmapDescriptorFactory.fromResource(idIcon));
        marker.setVisible(true);
        mapaMarcadores.put(marker.getId(),marker);
    }

    /**
     * Agrega un marcador asociado al estacionamiento de una persona (la ubicacion donde estaciono)
     * A su vez agrega una geofence asociada al marcador
     * @param estacionamiento objeto que almacena la informacion acerca de donde realizo el estacionamiento el vehiculo
     * @return marcador que se agrego
     */
    public Marker agregarMarcadorEstacionamiento(UbicacionVehiculoEstacionado estacionamiento) {
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(estacionamiento.getCoordenadas())
                .title(estacionamiento.getTitulo()));

        marker.setIcon(BitmapDescriptorFactory.defaultMarker(ConstantsEstacionamientoService.MARCADOR_ESTACIONAMIENTO_CALLE));
        /* Agrego el objeto estacionamiento al marcador */
        marker.setTag(estacionamiento);

        if(!estacionamiento.getEnLaCalle()){
            marker.setVisible(false);
        } else {
            marker.setTitle("Su vehiculo esta aquí.");
        }
         /* Objeto que permite generar eventos de aproximacion al radio del marcador */
        Geofence.Builder geof = new Geofence.Builder();
        geof.setRequestId(marker.getId())
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(ConstantsEstacionamientoService.GEOFENCE_STAY_TO_NOTIFICATION_TIME)
                .setExpirationDuration(ConstantsEstacionamientoService.GEOFENCE_EXPIRATION_DURATION_TIME)
                .setCircularRegion(marker.getPosition().latitude, marker.getPosition().longitude, ConstantsEstacionamientoService.GEOFENCE_RADIUS_IN_METERS)
        ;
        //mGeofenceList.add(geof.build());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            pedirPermisosUbicacion(ConstantePermisos.getPermisoFineLocationParaGeofencing());
        }
        else
        {
            //agregarGeofence();
        }
        //mapaMarcadores.put(marker.getId(),marker);
        return marker;
    }


    /**-------------------------------------------------------------------*/
    /**                  Metodos de Estacionamietos                       */
    /**-------------------------------------------------------------------*/

    /**
     * Permite estacionar en la posicion actual desde el Nav
     **/
    public void estacionarAqui(){
        LatLng latLngActual = new LatLng(ubicacionActual.getLatitude(),ubicacionActual.getLongitude());
        estacionarEnParque(latLngActual);

        //Seteo true la bandera que indica que estaciono - se utiliza para los botones
        lugarEstacionamientoGuardado = true;
        //Configuro los botones del nav
        ConstantsNavigatorView.ENABLE_INDIACE_MENU_VER_ESTACIONAMIENTO = true;
        ConstantsNavigatorView.ENABLE_INDICE_MENU_ESTACIONAR_AQUI = false;
        ConstantsNavigatorView.ENABLE_INDICE_MENU_ALARMA = true;
    }
    /**
     * Permite mover la camara a donde esta estacionado el vehiculo
     */
    public void verDondeEstaciono(){
        Location auxLocation = new Location(ubicacionActual);
        auxLocation.setLatitude(markerUltimoEstacionamiento.getPosition().latitude);
        auxLocation.setLongitude(markerUltimoEstacionamiento.getPosition().longitude);
        enfocarMapaEnUbicacion(auxLocation,18f);
    }

    /**
     * Marca la salida del estacionamiento y elimina el marcador
     * @param marcadorSalida
     */
    public void marcarSalidaEstacionamiento(Marker marcadorSalida) throws UbicacionVehiculoException {
        String msg = getResources().getString(R.string.parkLoggerInicioSalidaEstacionamiento);
        Log.v(TAG,msg);
        String estacionarAqui = getResources().getString(R.string.menuOptEstacionarAqui);
        String dondeEstacione = getResources().getString(R.string.menuOptDondeEstacione);
        UbicacionVehiculoEstacionado ubicacionVehiculo = (UbicacionVehiculoEstacionado) markerUltimoEstacionamiento.getTag();
        ubicacionVehiculo.setHoraEgreso(System.currentTimeMillis());

        /* Elimino la alarma que se asocia al estacionamiento del usuario */
        eliminarAlarma(markerUltimoEstacionamiento);
        actualizarUbicacionPersistida(ubicacionVehiculo);

        //Cambia el icono al comun de todos lso estacionamientos
        if(marcadorSelected.getPosition().equals(markerUltimoEstacionamiento.getPosition()) && !estCalle.getEnLaCalle()) {
            marcadorSelected.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_estacionamiento));
        }
        //Si estaciono en la calle remuevo el marker cuando se va
        if(estCalle.getEnLaCalle()){
            markerUltimoEstacionamiento.remove();
        }

        //Si estaciono en la calle remuevo el marker cuando se va
        if(estCalle.getEnLaCalle()){
            markerUltimoEstacionamiento.remove();
        }
        markerUltimoEstacionamiento = null;
        estCalle = null;
        this.lugarEstacionamientoGuardado = false;
        ConstantsNavigatorView.ENABLE_INDICE_MENU_ESTACIONAR_AQUI = true;
        ConstantsNavigatorView.ENABLE_INDICE_MENU_ALARMA = false;
    }

    private void marcarEstacionamientos(){
        Estacionamiento estIterador;
        for(int i = 0; i< listaEstacionamientos.size(); i++){
            estIterador = listaEstacionamientos.get(i);
            addMarker(
                    estIterador.getPosicionEstacionamiento(),
                    (estIterador.getNombreEstacionamiento()).substring(8),
                    R.drawable.marker_estacionamiento
            );
        }
    }

    private void estacionarEnParque(LatLng position){
        String msg;
        //Se usa ubicacionActual solo para poder crear otra location
        Location locationParque = new Location(ubicacionActual);
        locationParque.setLatitude(position.latitude);
        locationParque.setLongitude(position.longitude);
        if (mGoogleApiClient.isConnected() && ubicacionActual != null) {
            estCalle = new UbicacionVehiculoEstacionado(locationParque);
            estCalle.setHoraIngreso(System.currentTimeMillis());

            //Busca si existe un markador en esta posicion, si existe entonces estaciono en un "Estacionamiento
            //Si no existe, es porque estaciono en la calle.
            Marker markerEstacionamiento = buscarMarker(position);
            if(markerEstacionamiento == null){
                estCalle.setEnLaCalle(true);
            }else {
                estCalle.setEnLaCalle(false);
                //Seteo un icono distinto al marker del estacionamiento donde el tipo estaciono
                markerEstacionamiento.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_estacionamiento_dondeestaciono));
            }

            startAddressFetchService();
            mAddressRequested = false;
            msg = getResources().getString(R.string.parkLoggerEstacionamientoExitoso);
            Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG);
            markerUltimoEstacionamiento = agregarMarcadorEstacionamiento(estCalle);

            /* Agrego el marcador a la lista de marcadores */
            //mapaMarcadores.put(markerUltimoEstacionamiento.getId(),markerUltimoEstacionamiento);
            /* Agrego la alarma que se asocia al estacionamiento del usuario */
            agregarAlarma(markerUltimoEstacionamiento);
            Log.v(TAG,msg);
            persistirUbicacion(estCalle);
        }
        mAddressRequested = true;
    }

    /** Permite cargar el ultimo estacionamiento en el mapa del usuario */
    public UbicacionVehiculoEstacionado cargarUltimoEstacionamiento(int idUsuario){
        UbicacionVehiculoEstacionado ultimoEst = ubicacionVehiculoDAO.getUltimaUbicacionVehiculo(idUsuario,getContext());
        String msg;

        if(ultimoEst !=null) {

            /* Si no hay hora de egreso es porque no se produjo, y por lo tanto lo agrego como ubicacion del vehiculo */
            if(ultimoEst.getHoraEgreso() == null ){
                /* TODO - una implementacion mejor tendria en cuenta otra condicion para el if: que el tiempo actual vs el tiempo
                de ingreso sea chico, si paso mucho tiempo significa que se olvido de marcar el egreso y por lo tanto habria
                que marcar el egreso y no poner el marcador
                */
                Marker marcadorAux = buscarMarker(ultimoEst.getCoordenadas());
                if(marcadorAux != null) {
                    marcadorAux.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_estacionamiento_dondeestaciono));
                }
                markerUltimoEstacionamiento = agregarMarcadorEstacionamiento(ultimoEst);
                //agregarAlarma(markerUltimoEstacionamiento);
                generarVentanaRecordatorioEstacionamiento(markerUltimoEstacionamiento);
                this.lugarEstacionamientoGuardado = true;
                msg = getResources().getString(R.string.menuOptDondeEstacione);

                //TODO Setear eneable o disable los botones del menu lateral segun corresponda
                ConstantsNavigatorView.ENABLE_INDICE_MENU_ESTACIONAR_AQUI = false;
                ConstantsNavigatorView.ENABLE_INDIACE_MENU_VER_ESTACIONAMIENTO = true;
                ConstantsNavigatorView.ENABLE_INDICE_MENU_ALARMA = true;

            }
        }
        return ultimoEst;
    }


    /**-------------------------------------------------------------------*/
    /**                      Metodos de Alarma                            */
    /**-------------------------------------------------------------------*/

    /**
     * Genera una ventana de informacion alertando de que existia un vehiculo previamente estacionado cuando se abrio la aplicacion
     * @param markerUltimoEstacionamiento
     */
    private void generarVentanaRecordatorioEstacionamiento(final Marker markerUltimoEstacionamiento){
        final UbicacionVehiculoEstacionado ubicacionEstacionamiento = (UbicacionVehiculoEstacionado) markerUltimoEstacionamiento.getTag();
        String titulo = this.getResources().getString(R.string.notificacionAlarmaEstTitulo);
        String tiempoDeIngreso;
        StringBuilder texto = new StringBuilder();
        texto.append("Su vehiculo se encuentra estacionado desde las ");
        Date date = new Date(ubicacionEstacionamiento.getHoraIngreso());
        tiempoDeIngreso = new SimpleDateFormat("HH:mm").format(date);
        texto.append(tiempoDeIngreso);
        texto.append(" , desea marcar la salida del estacionamiento?");

        final Dialog dialogTest = new Dialog(getContext());
        dialogTest.setContentView(R.layout.custom_info_windows_alert_recodatorio_estacionamiento);
        dialogTest.setTitle(titulo);
        dialogTest.setCancelable(true);
        dialogTest.show();
        Button btnAceptar = (Button) dialogTest.findViewById(R.id.btnAceptar);
        Button btnIrAlEstacionamiento = (Button) dialogTest.findViewById(R.id.btnIrAlEstacionamiento);
        Button btnCancelar = (Button) dialogTest.findViewById(R.id.btnCancelar);
        TextView msgDialog = (TextView) dialogTest.findViewById(R.id.tvMsgRecordatorio);
        msgDialog.setText(texto);
        /** Listener de la opcion de ir a la ubicacion del estacionamiento */
        btnIrAlEstacionamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location lugarEstacionado = new Location(ubicacionActual);
                lugarEstacionado.setLatitude(ubicacionEstacionamiento.getCoordenadas().latitude);
                lugarEstacionado.setLongitude(ubicacionEstacionamiento.getCoordenadas().longitude);
                enfocarMapaEnUbicacion(lugarEstacionado,18f);
                dialogTest.dismiss();
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String estacionarAqui = getResources().getString(R.string.menuOptEstacionarAqui);
                try {
                    marcarSalidaEstacionamiento(markerUltimoEstacionamiento);
                } catch (UbicacionVehiculoException e) {
                    String msg = getResources().getString(R.string.errorProducidoIntenteNuevamente);
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
                } finally {
                    dialogTest.dismiss();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarAlarma(markerUltimoEstacionamiento);
                dialogTest.dismiss();
            }
        });
    }

    /**
     * Asocia una alarma al objeto estacionamiento y la inicializa
     * Permite al usuario recordar que dejo el auto estacionado mediante una notificacion cada X Tiempo
     * @param markerEstacionamiento
     */
    private void eliminarAlarma(Marker markerEstacionamiento){
        UbicacionVehiculoEstacionado ubicacionVehiculo = (UbicacionVehiculoEstacionado) markerEstacionamiento.getTag();
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmEstacionamientoReceiver.class);
        //  intent.putExtra("idMarcador",markerEstacionamiento.getId());
        //  intent.setAction(String.valueOf(ConstantsNotificaciones.ACCION_GENERAR_ALARMA));
        Integer idPendingIntent = ubicacionVehiculo.getId();
        PendingIntent pi = PendingIntent.getBroadcast(getContext(),idPendingIntent,intent,0);
        alarmManager.cancel(pi);
    }

    /**
     * Elimina una alarma al objeto estacionamiento
     * @param markerEstacionamiento
     */
    private void agregarAlarma(Marker markerEstacionamiento){
        UbicacionVehiculoEstacionado ubicacionVehiculo = (UbicacionVehiculoEstacionado) markerEstacionamiento.getTag();
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmEstacionamientoReceiver.class);
        intent.putExtra("idMarcador",markerEstacionamiento.getId());
        intent.setAction(String.valueOf(ConstantsNotificaciones.ACCION_GENERAR_ALARMA));
        Integer idPendingIntent = ubicacionVehiculo.getId();
        PendingIntent pi = PendingIntent.getBroadcast(getContext(),idPendingIntent,intent,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+ConstantsNotificaciones.TIEMPO_CONFIGURADO_ALARMA,pi);
    }

    private void setearTimer(final Context context) {

        String msgCancelar = getResources().getString(R.string.btnCancelar);
        String msgGuardar = getResources().getString(R.string.btnDialogSetearAlarma);
        String msgTituloDialog = "Temporizador para alarma";

        final Dialog dialogTest = new Dialog(context); // Context, this, etc.
        dialogTest.setContentView(R.layout.custom_alarm_timer_window);
        dialogTest.setTitle(msgTituloDialog);
        dialogTest.setCancelable(true);
        dialogTest.show();

        Button btnSetearTimer = (Button) dialogTest.findViewById(R.id.btnSetearTimer);
        Button btnCancelar = (Button) dialogTest.findViewById(R.id.btnDialogCancel);
        btnSetearTimer.setText(msgGuardar);
        btnCancelar.setText(msgCancelar);

        TimePicker timePicker = (TimePicker) dialogTest.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener((TimePicker.OnTimeChangedListener) context);
        //TODO implementar todos los métodos TimePicker! (validaciones, etc)

        /** Listener de la opcion de setear el timer de la alarma */
        btnSetearTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO setear alarma/timer
                dialogTest.dismiss();
            }
        });

        /** Listener de la opcion de cancelar */
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTest.dismiss();
            }
        });
    }

    /**-------------------------------------------------------------------*/
    /**         Metodos de Ubicacion / Servicios de Ubicacion             */
    /**-------------------------------------------------------------------*/

    /**
     * Agrega la Api de Google
     */
    private void addGoogleApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Guarda el ultimo lugar en donde se realizo el estacionamiento
     */
    private void persistirUbicacion(UbicacionVehiculoEstacionado ubicacionEstacionado){
        String msg = getResources().getString(R.string.parkLoggerInicioPersistiendoUbicacion);
        Log.v(TAG,msg);
        try {
            ubicacionVehiculoDAO.guardarOActualizarUbicacionVehiculo(ubicacionEstacionado,getContext());
        }
        catch (UbicacionVehiculoException e) {
            msg = getResources().getString(R.string.errorProducidoIntenteNuevamente);
            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT);
            // e.printStackTrace();
        }
    }

    /**
     * Actualiza la informacion en disco del objeto ubicacion vehiculo
     * @param estCalle
     */
    private void actualizarUbicacionPersistida(UbicacionVehiculoEstacionado estCalle) throws UbicacionVehiculoException {
        String msg = getResources().getString(R.string.parkLoggerInicioActualizacionUbicacion);
        Log.v(TAG,msg);
        ubicacionVehiculoDAO.actualizarUbicacionVehiculoEstacionado(estCalle,getContext());
    }

    /**
     * Recibe una ubicacion y enfoca el mapa en ese punto
     */
    private void enfocarMapaEnUbicacion(Location location, float zoom){
        String msg;
        if(location!=null){
            msg = getResources().getString(R.string.ubicacionActualEncontrada);
            Log.v(TAG,msg);
            String msgToast = location.getLatitude() + ", " + location.getLongitude();

            //Muevo la camara
            //Le doy zoom
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),zoom),200,null);
        }
        else{
            msg = getResources().getString(R.string.ubicacionActualInexistente);
            Log.v(TAG,msg);
        }
    }

    /**
     *  Se conecta con location services de google luego de haber solicitado permisos
     */
    private void conectarALocationServices(){
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            ubicacionActual = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (ubicacionActual != null) {
                // Determine whether a Geocoder is available.
                if (!Geocoder.isPresent()) {
                    Log.v(TAG, getResources().getString(R.string.no_geocoder_available));
                }
                if (mAddressRequested) {
                    startAddressFetchService();
                }
            }
        }
    }

    /**
     * Inicializa el servicio que se encarga de buscar direcciones dada una ubicacion
     */
    private void startAddressFetchService(){
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(ConstantsAddresses.RECEIVER, mResultReceiver);
        intent.putExtra(ConstantsAddresses.LOCATION_DATA_EXTRA, ubicacionActual);
        getActivity().startService(intent);
    }


    /** Una vez recibido permisos de ubicacion, agrega las geofences creadas al mapa */
    private void agregarGeofence(){
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this);
        }
    }

    /**
     * Devuelve un pending intent relacionado a los geofences de los marcadores que permite realizar una notificacion
     * @return
     */
    private PendingIntent getGeofencePendingIntent(){
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(getActivity(), GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(getContext(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    /**
     * Genera una geofence la cual se debe asociar a un marcador de estacionamiento
     * @return
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }



    /**-------------------------------------------------------------------*/
    /**                     Metodos de Permisos                           */
    /**-------------------------------------------------------------------*/

    /**
     * Pide permisos para acceder a la ubicacion mediante un mensaje de usuario
     */
    private void pedirPermisosUbicacion(final Integer idSolicitantePermiso){
        String msg;
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            msg = getResources().getString(R.string.tituloSolicitudPermisos);
            builder.setTitle(msg);
            builder.setPositiveButton(android.R.string.ok, null);
            msg = getResources().getString(R.string.mensajeSolicitudPermisos);
            builder.setMessage(msg);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onDismiss(DialogInterface dialog) {
                    flagPermisoPedido=true;
                    requestPermissions(
                            new String[]
                                    {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}
                            , idSolicitantePermiso);
                }
            });
            builder.show();
        }
        else {
            flagPermisoPedido=true;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]
                            {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}
                    , idSolicitantePermiso);
        }
    }


    /**-------------------------------------------------------------------*/
    /**                     Metodos de Navigator                          */
    /**-------------------------------------------------------------------*/

    /**
     * Abre el navigator hacia las coordenadas del navegador destino
     * @param marcadorDestino
     */
    private void abrirNavigatorEnDestino(Marker marcadorDestino){
        String msg = getResources().getString(R.string.parkLoggerInicioNavegacion);
        Log.v(TAG,msg);
        Double latitude = marcadorDestino.getPosition().latitude;
        Double longitude = marcadorDestino.getPosition().longitude;
        Intent navigation = new Intent(Intent.ACTION_VIEW, Uri
                .parse("http://maps.google.com/maps?saddr="
                        + "&daddr="
                        + latitude + "," + longitude));
        startActivity(navigation);
    }


    /**-------------------------------------------------------------------*/
    /**                     Metodos de los Implements                     */
    /**-------------------------------------------------------------------*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        /** Pido permisos de ubicacion */
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            pedirPermisosUbicacion(ConstantePermisos.getPermisoFineLocationParaLocationService());
        }
        /** Tengo permisos */
        else{
            conectarALocationServices();
        }

        cargarMapa(this.googleMap);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Result result) {

    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        marcadorSelected = marker;
        final String msgSalidaEstacionamiento = getResources().getString(R.string.btnMarcarSalida);
        final String msgEstacionarAqui = getResources().getString(R.string.menuOptEstacionarAqui);
        final String msgNavegar = getResources().getString(R.string.btnAbrirEnNavigator);
        final String msgVerEstacionamiento = getResources().getString(R.string.btnIrAlEstacionamiento);
        String msgCancelar = getResources().getString(R.string.btnCancelar);
        String msgTituloDialog = getResources().getString(R.string.menuDialogTitulo);

        Location loc = new Location(ubicacionActual);
        loc.setLatitude(marker.getPosition().latitude); //Actualizo la posicion a la del estacionamiento
        loc.setLongitude(marker.getPosition().longitude);
        enfocarMapaEnUbicacion(loc,18f); //Enfoco el mapa al estacionamiento para que se vea mejor

        final Dialog dialogTest = new Dialog(getContext()); // Context, this, etc.
        dialogTest.setContentView(R.layout.custom_info_window_estacionamiento);
        dialogTest.setTitle(msgTituloDialog);
        dialogTest.setCancelable(true);
        dialogTest.show();

        Button btnAbrirNavigator = (Button) dialogTest.findViewById(R.id.btnNavegar);
        final Button btnSalidaEntrada = (Button) dialogTest.findViewById(R.id.btnSalir_EntrarEstacionamiento);
        Button btnCancelar = (Button) dialogTest.findViewById(R.id.btnCancelar);
        btnAbrirNavigator.setText(msgNavegar);


        if(lugarEstacionamientoGuardado == false){
            btnSalidaEntrada.setText(msgEstacionarAqui);
            btnSalidaEntrada.setEnabled(true);

        }
        else{
            btnSalidaEntrada.setText(msgSalidaEstacionamiento);
            btnSalidaEntrada.setEnabled(false);

        }
        btnCancelar.setText(msgCancelar);
        /** Listener de la opcion de navegar */
        btnAbrirNavigator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNavigatorEnDestino(marcadorSelected);
                dialogTest.dismiss();
            }
        });

        if(lugarEstacionamientoGuardado == true && marker.getPosition().equals(markerUltimoEstacionamiento.getPosition())){
            btnSalidaEntrada.setEnabled(true);
        }
        else{
            //btnSalidaEntrada.setEnabled(false);
        }

        /** Listener de la opcion de marcar salida del estacionamiento */
        btnSalidaEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Se ejecuta si el vehiculo no esta estacionado */
                if(btnSalidaEntrada.getText().equals(msgEstacionarAqui) && lugarEstacionamientoGuardado == false){
                    Log.v(TAG_MENU,"Estacionando en ubicacion actual");
                    estacionarEnParque(marker.getPosition());
                    lugarEstacionamientoGuardado = true;
                    btnSalidaEntrada.setText(msgSalidaEstacionamiento);

                    //TODO Setear enable o disable segun corresponda los botones del menu desplegable
                    ConstantsNavigatorView.ENABLE_INDIACE_MENU_VER_ESTACIONAMIENTO = true;
                    ConstantsNavigatorView.ENABLE_INDICE_MENU_ESTACIONAR_AQUI = false;
                    ConstantsNavigatorView.ENABLE_INDICE_MENU_ALARMA = true;
                    dialogTest.dismiss();
                }
                else {
                    /** Se ejecuta si el vehiculo se encuentra actualmente estacionado */
                    if(btnSalidaEntrada.getText().equals(msgSalidaEstacionamiento) && lugarEstacionamientoGuardado == true) {
                        Log.v(TAG_MENU, "Borrando ubicacion guardada");
                        //Creo una Location auxiliar con las coordenadas de la ubicacion guardada y enfoco el mapa ahi
                        Location lugarEstacionado = new Location(ubicacionActual);

                        lugarEstacionado.setLatitude(estCalle.getCoordenadas().latitude);
                        lugarEstacionado.setLongitude(estCalle.getCoordenadas().longitude);
                        enfocarMapaEnUbicacion(lugarEstacionado, 18f);

                        try{
                            marcarSalidaEstacionamiento(marcadorSelected);

                            btnSalidaEntrada.setText(msgEstacionarAqui);

                            ConstantsNavigatorView.ENABLE_INDICE_MENU_ALARMA = false;
                            ConstantsNavigatorView.ENABLE_INDICE_MENU_ESTACIONAR_AQUI = true;
                            ConstantsNavigatorView.ENABLE_INDIACE_MENU_VER_ESTACIONAMIENTO = false;
                            dialogTest.dismiss();
                        }
                        catch (UbicacionVehiculoException e) {
                            String msg = getResources().getString(R.string.errorProducidoIntenteNuevamente);
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
                        }


                    }
                }
            }
        });
        /** Listener de la opcion de cancelar */
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTest.dismiss();
            }
        });

        //AlertDialog dialog= builder.create();
        //Mostrarlo
        //dialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
       // this.googleMap.setMyLocationEnabled(true);
        //this.googleMap.setOnInfoWindowClickListener(this);
        addGoogleApi();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {    }

}
