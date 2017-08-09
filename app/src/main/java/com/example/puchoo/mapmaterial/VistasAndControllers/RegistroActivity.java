package com.example.puchoo.mapmaterial.VistasAndControllers;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaCodec;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.PointerIcon;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puchoo.mapmaterial.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Puchoo on 08/08/2017.
 */

public class RegistroActivity extends AppCompatActivity {
    private static final String TAG = "RegistroActivity";

    private EditText nameText;
    private EditText apellidoText;
    private EditText telefonoText;
    private EditText patenteText;
    private EditText modeloText;
    private EditText anioText;
    private EditText colorText;
    private EditText emailText;
    private EditText passwordText;
    private Button signupButton;
    private TextView loginLink;
    private CheckedTextView datosPersonales, datosVehiculo;
    private String COLOR_DESPLEGABLE_ICONO = "#b7b7b7";
    private Boolean expandidoPersonales,expandidoVehiculo;
    private TextInputLayout name,apellido,telefono,modelo,anio,color;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        inicializarComponentes();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });

        setExpandibleListener();

    }

    private void inicializarComponentes() {

        datosPersonales = (CheckedTextView) findViewById(R.id.desplegable_datospersonales_registro);
        cambiarIconoExpandir(datosPersonales);

        nameText = (EditText)findViewById(R.id.input_name_registro);
        apellidoText = (EditText)findViewById(R.id.input_apellido_registro);
        telefonoText = (EditText)findViewById(R.id.input_tel_registro);


        emailText = (EditText)findViewById(R.id.input_email_registro);
        passwordText = (EditText) findViewById(R.id.input_password_registro);

        datosVehiculo = (CheckedTextView) findViewById(R.id.desplegable_datosvehiculo_registro);
        cambiarIconoExpandir(datosVehiculo);

        patenteText = (EditText)findViewById(R.id.input_patente_registro);

        modeloText = (EditText)findViewById(R.id.input_modelo_registro);
        anioText = (EditText)findViewById(R.id.input_año_registro);
        colorText = (EditText)findViewById(R.id.input_color_registro);

        name = (TextInputLayout) findViewById(R.id.name_registro);
        apellido = (TextInputLayout) findViewById(R.id.apellido_registro);
        telefono = (TextInputLayout) findViewById(R.id.tel_registro);
        modelo = (TextInputLayout) findViewById(R.id.modelo_registro);
        color = (TextInputLayout) findViewById(R.id.color_registro);
        anio = (TextInputLayout) findViewById(R.id.año_registro);

        goneCampos();


        signupButton = (Button) findViewById(R.id.btn_signup_registro);
        loginLink = (TextView) findViewById(R.id.link_login_registro);
    }


    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegistroActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creado Cuenta...");
        progressDialog.show();

        String name = nameText.getText().toString();
        String apellido = apellidoText.getText().toString();
        String telefono = telefonoText.getText().toString();

        String patente = patenteText.getText().toString();
        String modelo = modeloText.getText().toString();
        String anio = anioText.getText().toString();
        String color = colorText.getText().toString();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login fallido", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String telefono = telefonoText.getText().toString();
        String patente = patenteText.getText().toString();
        String password = passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 4) {
            nameText.setError("al menos 4 caracteres");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("ingrese un email valido");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("entre 4 y 10 caracteres alfanumericos");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        Pattern patentePattern = Pattern.compile("[A-Z]{3}\\d{3}");
        Pattern patenteNuevaPattern = Pattern.compile("[A-Z]{2}\\d{3}[A-Z]{2}");
        Matcher matchetPatente = patentePattern.matcher(patente);
        Matcher matchetPatenteNueva = patenteNuevaPattern.matcher(patente);
        if(patente.isEmpty() || (!matchetPatente.find() && !matchetPatenteNueva.find()) ){
            patenteText.setError("ingrese patente valida");
            valid = false;
        } else {
            //TODO VALIDAR TIPO DE PATENTE POR AÑO
                patenteText.setError(null);
        }

        if (!telefono.isEmpty()){
            Pattern telefonoPattern = Pattern.compile("\\+?\\d{1,3}?[- .]?\\(?(?:\\d{2,3})\\)?[- .]?\\d\\d\\d[- .]?\\d\\d\\d\\d");
            Matcher matchetTelefono = telefonoPattern.matcher(telefono);
            if(!matchetTelefono.find()){
                valid = false;
                telefonoText.setError("ingrese telefono valido");
            }
        }

        return valid;
    }

    private void cambiarColorIcono(CheckedTextView cTV){
        int color = Color.parseColor(COLOR_DESPLEGABLE_ICONO);
        Drawable icon = cTV.getCheckMarkDrawable();

        icon.mutate().setColorFilter(color,PorterDuff.Mode.SRC_ATOP);
    }

    private void cambiarIconoExpandir(CheckedTextView cTV){
        cTV.setCheckMarkDrawable(R.drawable.ic_collapse_00009);
        cambiarColorIcono(cTV);
        if (cTV == datosVehiculo){
            expandidoVehiculo = true;
        } else {
            expandidoPersonales = true;
        }
    }

    private void cambiarIconoColapse(CheckedTextView cTV){
        cTV.setCheckMarkDrawable(R.drawable.ic_expand_00009);
        cambiarColorIcono(cTV);
        if(cTV == datosVehiculo){
            expandidoVehiculo = false;
        } else{
            expandidoPersonales = false;
        }
    }

    private void goneCampos() {
        name.setVisibility(View.GONE);
        apellido.setVisibility(View.GONE);
        telefono.setVisibility(View.GONE);

        modelo.setVisibility(View.GONE);
        color.setVisibility(View.GONE);
        anio.setVisibility(View.GONE);
    }

    private void setExpandibleListener(){

        datosPersonales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datosPersonales.setChecked(true);
                if (expandidoPersonales){
                    cambiarIconoColapse(datosPersonales);
                    name.setVisibility(View.VISIBLE);
                    apellido.setVisibility(View.VISIBLE);
                    telefono.setVisibility(View.VISIBLE);
                } else {
                    cambiarIconoExpandir(datosPersonales);
                    name.setVisibility(View.GONE);
                    apellido.setVisibility(View.GONE);
                    telefono.setVisibility(View.GONE);
                }
            }
        });

        datosVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datosVehiculo.setChecked(true);
                if (expandidoVehiculo){
                    cambiarIconoColapse(datosVehiculo);
                    color.setVisibility(View.VISIBLE);
                    modelo.setVisibility(View.VISIBLE);
                    anio.setVisibility(View.VISIBLE);
                } else {
                    cambiarIconoExpandir(datosVehiculo);
                    color.setVisibility(View.GONE);
                    modelo.setVisibility(View.GONE);
                    anio.setVisibility(View.GONE);
                }
            }
        });
    }

}
