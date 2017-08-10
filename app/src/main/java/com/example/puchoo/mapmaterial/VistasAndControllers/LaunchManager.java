package com.example.puchoo.mapmaterial.VistasAndControllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.puchoo.mapmaterial.R;
import com.example.puchoo.mapmaterial.Utils.Validators.ValidadorLauncher;
import com.example.puchoo.mapmaterial.VistasAndControllers.Activities.LoginActivity;
import com.example.puchoo.mapmaterial.VistasAndControllers.Activities.MainActivity;

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

        new ValidadorLauncher(this).execute();

    }

    public void launch() {
        if(getUser_nuevo()){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            return;
    }

    public Boolean getUser_nuevo() {
        return user_nuevo;
    }

    public void setUser_nuevo(Boolean user_nuevo) {
        this.user_nuevo = user_nuevo;
    }
}
