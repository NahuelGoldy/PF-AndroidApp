package com.example.puchoo.mapmaterial.Utils.Validators;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.puchoo.mapmaterial.Dto.DatosReservaDTO;
import com.example.puchoo.mapmaterial.Dto.ReservaDTO;
import com.example.puchoo.mapmaterial.Utils.Api.ReservaEndpointClient;
import com.example.puchoo.mapmaterial.VistasAndControllers.Activities.ReservarActivity;

import java.io.IOException;

/**
 * Created by 084 on 17/10/2017.
 */

public class ValidadorReservaAsync extends AsyncTask<Void,Void,Void> {
        private final ReservarActivity reservaActivity;
        private ProgressDialog progress;
        private DatosReservaDTO nuevaReserva;
        private ReservaDTO resultadoReservaDTO = null;

        public ValidadorReservaAsync(ProgressDialog progress,DatosReservaDTO nuevaReserva, ReservarActivity regeservarActivity){
            this.progress = progress;
            this.nuevaReserva = nuevaReserva;
            this.reservaActivity = regeservarActivity;
        }

        @Override
        protected void onPreExecute(){
            progress.show();

            //ejecuta antes de arrancar
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                resultadoReservaDTO = new ReservaEndpointClient().crearReserva(nuevaReserva);
            } catch (IOException e) {
                reservaActivity.fail();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused){
            progress.dismiss();
            if(resultadoReservaDTO == null){
                reservaActivity.fail();
            }else {
                reservaActivity.success();
            }
            //ejecuta cuando termina
        }


}