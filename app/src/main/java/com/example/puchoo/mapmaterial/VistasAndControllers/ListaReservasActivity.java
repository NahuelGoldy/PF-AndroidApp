package com.example.puchoo.mapmaterial.VistasAndControllers;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.puchoo.mapmaterial.Modelo.ReservaMock;
import com.example.puchoo.mapmaterial.R;
import com.example.puchoo.mapmaterial.Utils.ReservaMockAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Nahuel SG on 12/02/2017.
 */

public class ListaReservasActivity extends AppCompatActivity {
    private TextView titulo;
    private ListView listView;
    private ReservaMockAdapter resAdapter;
    private Gson gson;
    private ArrayList<ReservaMock> listaReservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            //TODO ESTO DEBERIA ESTAR EN OTRA CLASE POR EL MOMENTO LA DEJO

        //setContentView(R.layout.activity_listar_reservas);

        //titulo = (TextView) findViewById(R.id.textViewTituloUltimasReservas);
        //listView = (ListView) findViewById(R.id.listView_ultimas_reservas);

        poblarListaReservas(this);
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
            resAdapter = new ReservaMockAdapter(this, listaReservas);
            listView.setAdapter(resAdapter);
        }
    }


}
