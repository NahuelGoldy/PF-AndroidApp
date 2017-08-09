package com.example.puchoo.mapmaterial.VistasAndControllers;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.puchoo.mapmaterial.Dao.EstacionamientoDAO;
import com.example.puchoo.mapmaterial.Exceptions.EstacionamientoException;
import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;
import com.example.puchoo.mapmaterial.Utils.EstacionamientoAdapter;

import java.util.Arrays;

/**
 * Created by Nahuel SG on 31/01/2017.
 */

public class ListarLugaresActivity extends AppCompatActivity implements View.OnClickListener {
    private EstacionamientoAdapter adapterEst;
    private ListView listaEst;
    public static Estacionamiento[] Estacionamientos;
    private Location ubicacionActual;
    /** Dao que almacena ubicacion de Estacionamientos */
    private static final EstacionamientoDAO estacionamientoDAO = EstacionamientoDAO.getInstance();
    /** Tag usado por el LOG    */
    private static final String TAG = "ServicioUbicacion";
    /** Boton que permite switchear entre la lista de lugares y el mapa */
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO LA LISTA DE LUGARES TMB IRIA EN OTRA CLASE
        //setContentView(R.layout.activity_listar_lugares);

        //fab = (FloatingActionButton) findViewById(R.id.fabmap);
        fab.setOnClickListener((View.OnClickListener) this);

        //Aca hago el primer try para intentar leer el archivo que tiene la lista de estacionamientos
        //Si no existe, lo creo, lo lleno (harcodeado..!) y lo leo
        try {
            Estacionamientos = estacionamientoDAO.llenarEstacionamientos(this);
        } catch (EstacionamientoException e) {
            //TODO manejar la excepcion
            String msgLog = "No se pudo leer el archivo";
            Log.v(TAG,msgLog);
            try {
                estacionamientoDAO.inicializarListaEstacionamientos(this);
                Estacionamientos = estacionamientoDAO.llenarEstacionamientos(this);
            } catch (EstacionamientoException e1) {
                msgLog = "Hubo un error al crear el archivo con la lista de Estacionamientos.";
                Log.v(TAG,msgLog);
            }
        }

        ubicacionActual = getIntent().getParcelableExtra("ubicacionActual");

        //listaEst = (ListView) findViewById(R.id.listLugares);
        adapterEst = new EstacionamientoAdapter(this, Arrays.asList(Estacionamientos), ubicacionActual);
        listaEst.setAdapter(adapterEst);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == fab.getId()) {
            Intent intent = new Intent(this, ListContentFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("bandera", "FABMAPA");
            startActivity(intent);
        }
    }
}
