package com.example.puchoo.mapmaterial;

/**
 * Created by Puchoo on 10/04/2017.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.puchoo.mapmaterial.Dao.ReservaDAO;
import com.example.puchoo.mapmaterial.Modelo.ReservaMock;
import com.example.puchoo.mapmaterial.Utils.ConstantsEstacionamientoService;
import com.example.puchoo.mapmaterial.Utils.ReservaMockAdapter;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
        initReservas();

        View view = inflater.inflate(R.layout.activity_listar_reservas, null);

        titulo = (TextView) view.findViewById(R.id.textViewTituloUltimasReservas);
        listView = (ListView) view.findViewById(R.id.listView_ultimas_reservas);

        poblarListaReservas(this.getContext());
        listView.setOnItemClickListener(this);

        return view;
    }

    private void poblarListaReservas(Context context) {

        if(listaReservas!=null && listaReservas.size()>0){
            resAdapter = new ReservaMockAdapter(this.getContext(), listaReservas);
            listView.setAdapter(resAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO hacer que se puede eliminar la reserva aqui mismos o en un LongClick

    }

    private void initReservas(){
        listaReservas = ReservaDAO.getInstance().getReservaSharedPref(this.getContext());
        if(!listaReservas.isEmpty()){
            //ordeno la lista por fecha
            if(ConstantsEstacionamientoService.HORA_RESERVA == null) {
                SimpleDateFormat df = new SimpleDateFormat("dd / MMMM / yyyy");
                try {
                    ReservaMock ultReserva = listaReservas.get(0);
                    Date fechaUltimaReserva = df.parse(ultReserva.getFechaReservado());
                    if (fechaUltimaReserva.getDay() == new Date().getDay()) {
                        df = new SimpleDateFormat("HH:mm:ss");
                        Date horaUltimaReserva = df.parse(ultReserva.getHoraReservado());

                        ConstantsEstacionamientoService.HORA_RESERVA = horaUltimaReserva.getTime();
                    }
                } catch (ParseException e) {
                    String msg = "Error en parsear fecha/hora";
                    Log.v("TileContentFragment", msg);
                }
            }
        }
    }

    public void refreshList(){
        if(listaReservas != null) {
            initReservas();
            poblarListaReservas(getContext());
        }
    }
}