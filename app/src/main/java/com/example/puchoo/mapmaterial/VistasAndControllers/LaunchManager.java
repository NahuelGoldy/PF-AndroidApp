package com.example.puchoo.mapmaterial.VistasAndControllers;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.Toast;

import com.example.puchoo.mapmaterial.R;

/**
 * Created by Puchoo on 08/08/2017.
 */

public class LaunchManager extends Activity {

    /** Bandera para si hay datos para logear automatico o es nuevo **/
    private Boolean user_nuevo = true;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Activity con algun logito
        setContentView(R.layout.activity_launch);

        if(user_nuevo){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            return;
    }


}
