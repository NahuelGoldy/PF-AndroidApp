package com.example.puchoo.mapmaterial.Utils;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import com.example.puchoo.mapmaterial.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
    Servicio que permite asociar ubicaciones a direcciones de manera asincronica
 */
public class FetchAddressIntentService extends IntentService {
    private Location ubicacionAObtenerAddress;
    private Double latitude,longitude;
    private String errorMessage;
    private StringBuilder direccion;
    protected ResultReceiver mReceiver;
    private final String TAG = "AddressService";
    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
        direccion = new StringBuilder();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            List<Address> addresses = null;
            ubicacionAObtenerAddress = intent.getParcelableExtra(ConstantsAddresses.LOCATION_DATA_EXTRA);
            mReceiver = intent.getParcelableExtra(ConstantsAddresses.RECEIVER);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            longitude = ubicacionAObtenerAddress.getLongitude();
            latitude = ubicacionAObtenerAddress.getLatitude();
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, ConstantsAddresses.CANT_MAXIMA_RESULTADOS);
                if(addresses == null){
                    errorMessage = getString(R.string.no_address_found);
                    Log.e(TAG, errorMessage);
                    deliverResultToReceiver(ConstantsAddresses.FAILURE_RESULT, errorMessage);
                }
            }
            catch (IOException ioException) {
                // Catch network or other I/O problems.
                String msg = this.getResources().getString(R.string.no_geocoder_avaiable_msg);
                Toast.makeText(this,msg,Toast.LENGTH_LONG);
                errorMessage = getString(R.string.service_not_available);
                Log.v(TAG, errorMessage, ioException);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // Catch invalid latitude or longitude values.
                errorMessage = getString(R.string.invalid_lat_long_used);
                Log.v(TAG, errorMessage + ". " +
                        "Latitude = " + latitude +
                        ", Longitude = " +
                        longitude, illegalArgumentException);
            }
            if (addresses == null || addresses.size()  == 0) {
                errorMessage = getString(R.string.no_address_found);
                Log.v(TAG, errorMessage);
                deliverResultToReceiver(ConstantsAddresses.FAILURE_RESULT, errorMessage);
            }
            else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread.
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                Log.i(TAG, getString(R.string.address_found));
                deliverResultToReceiver(ConstantsAddresses.SUCCESS_RESULT,address);
            }
        }
    }
    /** Envia la direccion si no se produjeron errores */
    private void deliverResultToReceiver(int resultCode, Address message) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ConstantsAddresses.RESULT_DATA_KEY,message);
        mReceiver.send(resultCode, bundle);
    }

    /** Envia la direccion si no se produjieron errores */
    /*
    private void deliverResultToReceiver(int resultCode, ArrayList<String> message) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ConstantsAddresses.RESULT_DATA_KEY,message);
        mReceiver.send(resultCode, bundle);
    }
    */
    /** Si se produjieron errores, envia un mensaje */
    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantsAddresses.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }

}
