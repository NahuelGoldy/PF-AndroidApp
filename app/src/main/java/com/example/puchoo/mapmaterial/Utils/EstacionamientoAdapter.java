package com.example.puchoo.mapmaterial.Utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nahuel SG on 31/01/2017.
 */

public class EstacionamientoAdapter extends BaseAdapter {
    private List<Estacionamiento> listaEstacionamiento;
    private LayoutInflater inflater;
    private View row;
    private Button reserva, verMapa;
    private TextView nombre, direccion, horarios, tarifa, distancia;
    private ImageView icnTechado, icnTarjeta, icn24hs;
    private Context mCont;
    private Location ubicacionActual;


    public EstacionamientoAdapter(Context context, List<Estacionamiento> listaEstac, Location ubicActual)
    {
        super();
        this.listaEstacionamiento = new ArrayList<Estacionamiento>();
        this.listaEstacionamiento.addAll(listaEstac);
        inflater = LayoutInflater.from(context);
        mCont = context;
        ubicacionActual = ubicActual;
    }
    @Override
    public int getCount() { return listaEstacionamiento.size() ;    }

    @Override
    public Object getItem(int position) {   return listaEstacionamiento.get(position);    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        row = convertView;
        if(row == null)
        {
            //row = inflater.inflate(R.layout.row_lista_estacionamiento,parent,false);

        }
        cargarVariables();
        llenarAdapter(position);
        return(row);
    }
    /**
     * Inicializa la fila de listview correspondiente con los componentes
     */
    private void cargarVariables()
    {
      //  reserva = (Button) row.findViewById(R.id.botonReservar);
       // verMapa = (Button) row.findViewById(R.id.botonVerEnMapa);
        //reserva.setFocusable(false);
        //reserva.setClickable(false);
        //verMapa.setFocusable(false);
        //verMapa.setClickable(false);

        //nombre = (TextView) row.findViewById(R.id.tvNombreEstacionamiento);
        //direccion = (TextView) row.findViewById(R.id.tvDireccionEstacionamiento);
        //horarios = (TextView) row.findViewById(R.id.tvHorarioEstacionamiento);
        //tarifa = (TextView) row.findViewById(R.id.textViewTarifa);
        //distancia = (TextView) row.findViewById(R.id.textViewDistancia);

        //icnTechado = (ImageView) row.findViewById(R.id.imageViewTechado);
        //icnTarjeta = (ImageView) row.findViewById(R.id.imageViewTarjeta);
        //icn24hs = (ImageView) row.findViewById(R.id.imageView24hs);

    }
    /**
     * Carga los componentes de la fila position del listview
     * @param position
     */
    private void llenarAdapter(final int position) {
        nombre.setText((((Estacionamiento)this.getItem(position)).getNombreEstacionamiento()).substring(8) );
        direccion.setText((CharSequence) ((Estacionamiento)this.getItem(position)).getDireccionEstacionamiento());
        horarios.setText(((Estacionamiento)this.getItem(position)).getHorarios());
        tarifa.setText(((Estacionamiento)this.getItem(position)).getTarifaEstacionamiento() );
        float[] res = new float[3];
        Location.distanceBetween( //almacena en res[0] la distancia en metros entre dos puntos (en línea recta)
                ubicacionActual.getLatitude(),
                ubicacionActual.getLongitude(),
                ((Estacionamiento)this.getItem(position)).getPosicionEstacionamiento().latitude,
                ((Estacionamiento)this.getItem(position)).getPosicionEstacionamiento().longitude,
                res);
        String distanciaRedondeada = String.format("%.2f", (res[0] / 1000));
        distancia.setText(distanciaRedondeada+ " KM");

        if(((Estacionamiento)this.getItem(position)).isEsTechado()){
            icnTechado.setVisibility(View.VISIBLE);
        }
        else icnTechado.setVisibility(View.INVISIBLE);

        if(((Estacionamiento)this.getItem(position)).isAceptaTarjetas()){
            icnTarjeta.setVisibility(View.VISIBLE);
        }
        else icnTarjeta.setVisibility(View.INVISIBLE);

        if(((Estacionamiento)this.getItem(position)).getHorarios().contains("24hs")){
            icn24hs.setVisibility(View.VISIBLE);
        }
        else icn24hs.setVisibility(View.INVISIBLE);
/*
        verMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idView = v.getId();
                if(idView == R.id.botonVerEnMapa){
                    //TODO borrar Sysout
                    System.out.println("(ADAPTER) Tocaste VER MAPA de la pos: " + position);

                    //Lanza un intent, mCont es el contexto que contiene el adapter
                    LatLng latLngToMap = listaEstacionamiento.get(position).getPosicionEstacionamiento();
                    Bundle args = new Bundle();
                    args.putParcelable("latlong", latLngToMap);
                    //Hago el intent y envio el dato
                    mCont.startActivity((new Intent(mCont, MapaActivity.class))
                            .putExtra("bundle", args)
                            .putExtra("bandera", "VER")
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        });

        reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idView = v.getId();
                if(idView == R.id.botonReservar){
                    Intent in = new Intent(mCont, ReservarActivity.class);
                    in.putExtra("indice", position);
                    mCont.startActivity(in);

                    // TODO Reserva (implementacion a futuro...)
                    //reservar(position);
                }
              }
        });
    */
    }

    /**
     * Carga un estacionamiento
     *
     */
    public void agregarEstacionamiento(Estacionamiento nuevoEstac,Context context)
    {
        listaEstacionamiento.add(nuevoEstac);
        notifyDataSetChanged();

    }

}