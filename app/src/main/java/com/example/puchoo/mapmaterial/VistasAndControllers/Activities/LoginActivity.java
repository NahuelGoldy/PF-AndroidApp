package com.example.puchoo.mapmaterial.VistasAndControllers.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puchoo.mapmaterial.Dto.LoginDTO;
import com.example.puchoo.mapmaterial.R;
import com.example.puchoo.mapmaterial.Utils.Api.LoginEndpointClient;
import com.example.puchoo.mapmaterial.Utils.Validators.ValidadorLogin;
import com.example.puchoo.mapmaterial.Utils.Validators.ValidadorPedidoEstacionamiento;
import com.example.puchoo.mapmaterial.VistasAndControllers.SesionManager;

import java.io.IOException;

/**
 * Created by Puchoo on 08/08/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private Button loginButton;
    private TextView signupLink;
    private EditText emailText, passwordText;

    /**Dialog**/
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarComponentes();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(v.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    private void inicializarComponentes() {
        loginButton = (Button) findViewById(R.id.btn_login_login);
        signupLink = (TextView) findViewById(R.id.link_signup_login);
        emailText = (EditText) findViewById(R.id.input_email_login);
        passwordText= (EditText) findViewById(R.id.input_password_login);
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autenticando...");
        progressDialog.setCanceledOnTouchOutside(false);



        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // TODO: Implemetar validacion dentro de ValidadorLogin
        new ValidadorLogin(progressDialog,email,password, this).execute();

        //TODO revisar: aca se setea el token??
        /*
        try {
            LoginDTO dto = new LoginEndpointClient().login();
            SesionManager.getInstance().setTokenUsuario(dto.getToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {

        progressDialog.setTitle("Cargando....");
        progressDialog.setMessage("Aguarde un instante mientras se cargan los estacionamientos...");

        new ValidadorPedidoEstacionamiento(this, progressDialog).execute();

        loginButton.setEnabled(true);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login fallido", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("ingrese un email valido");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("de 4 a 10 caracteres alfanumericos");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
