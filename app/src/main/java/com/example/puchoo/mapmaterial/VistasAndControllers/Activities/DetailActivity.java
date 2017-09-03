/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.puchoo.mapmaterial.VistasAndControllers.Activities;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.puchoo.mapmaterial.Dao.EstacionamientoDAO;
import com.example.puchoo.mapmaterial.Exceptions.EstacionamientoException;
import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;
import com.example.puchoo.mapmaterial.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class DetailActivity extends AppCompatActivity {

    /** Lista de listaEstacionamientos */
    private ArrayList<Estacionamiento> listaEstacionamientos;

    public static final String EXTRA_POSITION = "position";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Buscamos la lista de estacionamientos
        try {
            listaEstacionamientos = EstacionamientoDAO.getInstance().listarEstacionamientos(getBaseContext());
        } catch (EstacionamientoException e1) {
            String msgLog = "Hubo un error al crear el archivo con la lista de listaEstacionamientos.";
            Log.v(TAG,msgLog);
        }

        setContentView(R.layout.activity_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        Estacionamiento estacionamiento = listaEstacionamientos.get(position);

        // Set title of Detail page
        collapsingToolbar.setTitle(estacionamiento.getNombreEstacionamiento());

        TextView placeDetail = (TextView) findViewById(R.id.place_detail);
        placeDetail.setText(estacionamiento.getTarifaEstacionamiento() + "\n"
                + estacionamiento.getHorarios() + "\n"
                + "CAPACIDAD MAXIMA: "+ estacionamiento.getCapacidad() + "\n"
                + "TELEFONO: " + estacionamiento.getTelefono());

        TextView placeLocation =  (TextView) findViewById(R.id.place_location);
        placeLocation.setText(estacionamiento.getDireccionEstacionamiento());

        //TypedArray placePictures = getResources().obtainTypedArray(R.array.places_picture);

        ImageView placePicutre = (ImageView) findViewById(R.id.image);
        placePicutre.setImageDrawable(getBaseContext().getDrawable(R.drawable.img_parque_default));

        //placePictures.recycle();
    }
}
