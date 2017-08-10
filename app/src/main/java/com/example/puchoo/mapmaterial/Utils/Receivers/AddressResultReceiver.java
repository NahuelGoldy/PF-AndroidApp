package com.example.puchoo.mapmaterial.Utils.Receivers;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;


/**
 * Created by Agustin on 01/24/2017.
 */

/**
 * Receiver que permite recibir los resultados del servicio de busqueda de direcciones y enviarlos denuevo
 * a la actividad
 */
public class AddressResultReceiver extends ResultReceiver {
    private Receiver mReceiver;
    public AddressResultReceiver(Handler handler) {
        super(handler);
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    public void setReceiver(Receiver receiver){
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(mReceiver!=null){
            mReceiver.onReceiveResult(resultCode,resultData);
        }
    }
}
