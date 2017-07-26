package com.example.puchoo.mapmaterial;

/**
 * Created by Puchoo on 10/04/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.puchoo.mapmaterial.Modelo.ReservaMock;
import com.example.puchoo.mapmaterial.Utils.ReservaMockAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Provides UI for the view with Cards.
 */
public class TileContentFragment extends Fragment implements AdapterView.OnItemClickListener {

    private TextView titulo;
    private ListView listView;
    private ReservaMockAdapter resAdapter;
    private Gson gson;
    private ArrayList<ReservaMock> listaReservas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_listar_reservas, null);

        titulo = (TextView) view.findViewById(R.id.textViewTituloUltimasReservas);
        listView = (ListView) view.findViewById(R.id.listView_ultimas_reservas);
        poblarListaReservas(this.getContext());
        listView.setOnItemClickListener(this);
        return view;
    }

    private void poblarListaReservas(Context context) {
        gson = new Gson();
        String jsonReservasViejas = PreferenceManager.getDefaultSharedPreferences(context).getString("listaReservas", "");
        listaReservas = new ArrayList<>();
        Type type = new TypeToken<List<ReservaMock>>() {}.getType();
        listaReservas = gson.fromJson(jsonReservasViejas, type);

        if(listaReservas!=null && listaReservas.size()>0){

            //ordeno la lista por fecha
            Collections.sort(listaReservas, new Comparator<ReservaMock>() {
                public int compare(ReservaMock res1, ReservaMock res2) {
                    int comp = res2.getFechaReservado().compareTo(res1.getFechaReservado());
                    if(comp == 0){
                        return res2.getHoraReservado().compareTo(res1.getHoraReservado());
                    }
                    else return comp;
                }
            });
            resAdapter = new ReservaMockAdapter(this.getContext(), listaReservas);
            listView.setAdapter(resAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO hacer que se puede eliminar la reserva aqui mismos o en un LongClick

    }
}