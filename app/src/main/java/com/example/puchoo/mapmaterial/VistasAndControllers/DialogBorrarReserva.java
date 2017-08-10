package com.example.puchoo.mapmaterial.VistasAndControllers;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puchoo.mapmaterial.Dao.ReservaDAO;
import com.example.puchoo.mapmaterial.Modelo.ReservaMock;
import com.example.puchoo.mapmaterial.R;
import com.example.puchoo.mapmaterial.VistasAndControllers.Fragments.TileContentFragment;

/**
 * Created by Puchoo on 27/07/2017.
 */

public class DialogBorrarReserva implements View.OnClickListener {


    private String msgTituloDialog = "¿Desea borrar su reserva?";
    private String msgBorrar = "Si, borrar.";
    private String msgConservar = "No, conservar.";
    private final Dialog dialogTest;
    private Context context;
    private ReservaMock reserva;
    private TileContentFragment.ContentAdapter contentAdapter;

    public DialogBorrarReserva(Context context, ReservaMock reserva, TileContentFragment.ContentAdapter ContentAdapter) {
        this.reserva = reserva;
        this.context = context;
        this.contentAdapter = ContentAdapter;
        dialogTest = new Dialog(context); // Context, this, etc.
        dialogTest.setContentView(R.layout.custom_info_window_borrar_reserva);
        dialogTest.setTitle(msgTituloDialog);
        dialogTest.setCancelable(true);
        dialogTest.show();

        Button btnConservar = (Button) dialogTest.findViewById(R.id.btnConservarReserva);
        Button btnBorrar = (Button) dialogTest.findViewById(R.id.btnBorrarReserva);
        btnConservar.setText(msgConservar);
        btnBorrar.setText(msgBorrar);

        TextView tvBorrarReserva = (TextView) dialogTest.findViewById(R.id.tvBorrarReserva);
        tvBorrarReserva.setText("Borrar el registro de la reserva : "
                +   reserva.getNombreEstacionamientoReservado() + "\n"
                +   "a la hora: " + reserva.getHoraReservado());

        btnConservar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBorrarReserva){
            ReservaDAO.getInstance().borrarReservaSharedPref(reserva,context);
            dialogTest.dismiss();

            //Notifico al Adapter que le saque data
            this.contentAdapter.notifyDataSetChanged();
            Toast.makeText(context,"Se borro el registro de su reserva",Toast.LENGTH_LONG).show();

        }  else if(v.getId() == R.id.btnConservarReserva){
            dialogTest.dismiss();
        }
    }
}
