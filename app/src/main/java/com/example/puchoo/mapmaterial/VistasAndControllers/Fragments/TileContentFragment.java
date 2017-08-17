package com.example.puchoo.mapmaterial.VistasAndControllers.Fragments;

/**
 * Created by Puchoo on 10/04/2017.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.puchoo.mapmaterial.Dao.ReservaDAO;
import com.example.puchoo.mapmaterial.Modelo.ReservaMock;
import com.example.puchoo.mapmaterial.R;
import com.example.puchoo.mapmaterial.Utils.Adapters.ReservaMockAdapter;
import com.example.puchoo.mapmaterial.VistasAndControllers.Modals.DialogBorrarReserva;

import java.util.ArrayList;

/**
 * Provides UI for the view with Cards.
 */
public class TileContentFragment extends Fragment {

    private TextView titulo;
    private ListView listView;
    private View viewReservas;
    private ReservaMockAdapter resAdapter;
    private ArrayList<ReservaMock> listaReservas;
    public ContentAdapter adapterReservas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);

        /** Cargo la lista de estacionamientos para luego hacer la lista**/
        initReservas();

        final ContentAdapter adapter = new ContentAdapter(recyclerView.getContext(),listaReservas);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView direcc;
        public TextView fecha;
        public TextView hora;
        public ViewHolder(LayoutInflater inflater, final ViewGroup parent,
                          final ArrayList<ReservaMock> listaReservaHolder, final ContentAdapter Adapter) {
            super(inflater.inflate(R.layout.row_lista_reserva, parent, false));
                name = (TextView) itemView.findViewById(R.id.tv_nombreEstac_reserva_lista);
                direcc = (TextView) itemView.findViewById(R.id.tv_direccEstac_reserva_lista);
                fecha = (TextView) itemView.findViewById(R.id.tv_fecha_reserva_lista);
                hora = (TextView) itemView.findViewById(R.id.tv_hora_reserva_lista);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       new DialogBorrarReserva(v.getContext(),listaReservaHolder.get(getAdapterPosition()),Adapter);
                    }
                });
            }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{
        // Set numbers of Card in RecyclerView.
        private static int LENGTH = 10;

        private final String[] names;
        private final String[] direccs;
        private final String[] fechas;
        private final String[] horas;
        private ArrayList<ReservaMock> listaReservasAdapter;

        public ContentAdapter(Context context, ArrayList<ReservaMock> listaReservas) {

            if(listaReservas.isEmpty()){
                LENGTH = 0; //Si la lista es vacia seteo en 0 el la cant de items
            } else {
                if(listaReservas.size() < 10){
                    LENGTH = listaReservas.size();
                } else {
                    LENGTH = 10; //Si la lista no es vacia seteo en 10 la cant de items
                }
            }
            /** Inicialiacion de variables**/
            listaReservasAdapter = listaReservas;
            names = new String[listaReservasAdapter.size()];
            direccs = new String[listaReservasAdapter.size()];
            fechas = new String[listaReservasAdapter.size()];
            horas = new String[listaReservasAdapter.size()];

            if(!listaReservas.isEmpty()){
                int i = 0;
                for(ReservaMock r : listaReservasAdapter){
                    names[i] = "NOMBRE: "+r.getNombreEstacionamientoReservado();
                    direccs[i] ="DIRECCION: "+ r.getDireccionEstacionamientoReservado();
                    fechas[i] = r.getFechaReservado();
                    horas[i] = r.getHoraReservado();
                    i++;
                }
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent, listaReservasAdapter, this);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(!listaReservasAdapter.isEmpty()) {
                holder.name.setText(names[position % names.length]);
                holder.direcc.setText(direccs[position % direccs.length]);
                holder.fecha.setText(fechas[position % fechas.length]);
                holder.hora.setText(horas[position % horas.length]);
            }
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }

    }

    private void initReservas(){
        listaReservas = ReservaDAO.getInstance().getReservaSharedPref(this.getContext());
    }
}