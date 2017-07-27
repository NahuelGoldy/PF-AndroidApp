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
import com.example.puchoo.mapmaterial.TileContentFragment;
import com.example.puchoo.mapmaterial.Utils.ConstantsEstacionamientoService;

/**
 * Created by Puchoo on 26/07/2017.
 */

public class DialogErrorReserva implements View.OnClickListener {

    private String msgTituloDialog = "¿Desea cancelar su reserva?";
    private String msgCancelar = "Si, cacelar.";
    private String msgContinuar = "No, continuar.";
    private final Dialog dialogTest;
    private Context context;
    private ReservaMock reserva;

    public DialogErrorReserva(Context context) {
        this.reserva = ReservaDAO.getInstance().getReservaSharedPref(context).get(0);
        this.context = context;
        dialogTest = new Dialog(context); // Context, this, etc.
        dialogTest.setContentView(R.layout.custom_info_window_error_reserva);
        dialogTest.setTitle(msgTituloDialog);
        dialogTest.setCancelable(true);
        dialogTest.show();

        Button btnContinuarReserva = (Button) dialogTest.findViewById(R.id.btnContinuarReserva);
        Button btnCancelar = (Button) dialogTest.findViewById(R.id.btnCancelarReserva);
        btnContinuarReserva.setText(msgContinuar);
        btnCancelar.setText(msgCancelar);

        TextView tvErrorReserva = (TextView) dialogTest.findViewById(R.id.tvErrorReserva);
        tvErrorReserva.setText("Su reserva anterior es en: "
                +   reserva.getNombreEstacionamientoReservado() + "\n"
                +   "desde la hora: " + reserva.getHoraReservado());

        btnContinuarReserva.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnContinuarReserva){
            dialogTest.dismiss();
        }
        else if(v.getId() == R.id.btnCancelarReserva) {
            Toast.makeText(context,"Se cancelo su reserva en: "+ "\n"+
                    reserva.getNombreEstacionamientoReservado() ,Toast.LENGTH_LONG);
            ReservaDAO.getInstance().borrarReservaSharedPref(reserva,context);
            ConstantsEstacionamientoService.HORA_RESERVA = null;
            dialogTest.dismiss();
        }
    }
}
