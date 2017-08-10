package com.example.puchoo.mapmaterial.Utils.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.puchoo.mapmaterial.Modelo.ReservaMock;
import com.example.puchoo.mapmaterial.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nahuel SG on 12/02/2017.
 */

public class ReservaMockAdapter extends BaseAdapter{
    private List<ReservaMock> listaReservas;
    private LayoutInflater inflater;
    private View row;
    private TextView nombre, direccion, fecha, hora;
    private Context mCont;

    public ReservaMockAdapter(Context context, List<ReservaMock> listaReserv)
    {
        super();
        this.listaReservas = new ArrayList<ReservaMock>();
        this.listaReservas.addAll(listaReserv);
        inflater = LayoutInflater.from(context);
        mCont = context;
    }

    @Override
    public int getCount() {
        return listaReservas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaReservas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        row = convertView;
        if(row == null){
            row = inflater.inflate(R.layout.row_lista_reserva,parent,false);
        }
        cargarVariables();
        llenarAdapter(position);
        return(row);
    }

    private void cargarVariables(){
        nombre = (TextView) row.findViewById(R.id.tv_nombreEstac_reserva_lista);
        direccion = (TextView) row.findViewById(R.id.tv_direccEstac_reserva_lista);
        fecha = (TextView) row.findViewById(R.id.tv_fecha_reserva_lista);
        hora = (TextView) row.findViewById(R.id.tv_hora_reserva_lista);
    }

    private void llenarAdapter(final int position) {
        nombre.setText(((ReservaMock)this.getItem(position)).getNombreEstacionamientoReservado());
        direccion.setText(((ReservaMock)this.getItem(position)).getDireccionEstacionamientoReservado());
        fecha.setText(((ReservaMock)this.getItem(position)).getFechaReservado());
        hora.setText(((ReservaMock)this.getItem(position)).getHoraReservado());
    }
}
