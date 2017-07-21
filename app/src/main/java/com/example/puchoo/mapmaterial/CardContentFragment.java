package com.example.puchoo.mapmaterial;

/**
 * Created by Puchoo on 10/04/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puchoo.mapmaterial.Dao.EstacionamientoDAO;
import com.example.puchoo.mapmaterial.Exceptions.EstacionamientoException;
import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Provides UI for the view with Cards.
 */
public class CardContentFragment extends Fragment {

    /** Lista de listaEstacionamientos */
    private ArrayList<Estacionamiento> listaEstacionamientos;

    /** Dao que almacena ubicacion de listaEstacionamientos */
    private static final EstacionamientoDAO estacionamientoDAO = EstacionamientoDAO.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);

        /** Cargo la lista de estacionamientos para luego hacer las Card**/
        cargarEstacionamientos();

        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext(),listaEstacionamientos);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    private void cargarEstacionamientos() {
        try {
            /** Levanto la lista de estacionamientos de archivo/nube/db **/
            /** Cambiarlo en algun momento **/
            estacionamientoDAO.inicializarListaEstacionamientos(getActivity());
            listaEstacionamientos = estacionamientoDAO.listarEstacionamientos(getActivity());
        }
        catch (EstacionamientoException e1) {
            String msgLog = "Hubo un error al crear el archivo con la lista de listaEstacionamientos.";
            Log.v(TAG,msgLog);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;
        public TextView description;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_card, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_POSITION, getAdapterPosition());
                    context.startActivity(intent);
                }
            });

            // Adding Snackbar to Action Button inside card
            Button button = (Button)itemView.findViewById(R.id.reserva_button);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Action is pressed",
                            Snackbar.LENGTH_LONG).show();
                }
            });

            ImageButton favoriteImageButton =
                    (ImageButton) itemView.findViewById(R.id.favorite_button);
            favoriteImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Estacionamiento agreado a Favoritos",
                            Snackbar.LENGTH_LONG).show();
                }
            });

            ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_button);
            shareImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Compartir estacionamiento",
                            Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private static final int LENGTH = 18;

        private final String[] mPlaces;
        private final String[] mPlaceDesc;
        private final Drawable[] mPlacePictures;
        private ArrayList<Estacionamiento> listaEstContentAdapter;

        public ContentAdapter(Context context, ArrayList<Estacionamiento> listaEstacionamientos) {
            Resources resources = context.getResources();

            /** Inicialiacion de variables**/
            listaEstContentAdapter = listaEstacionamientos;
            mPlaces = new String[listaEstContentAdapter.size()];
            mPlaceDesc = new String[listaEstContentAdapter.size()];
            mPlacePictures = new Drawable[listaEstContentAdapter.size()];

            //TODO Remplazarlos resources por peticiones al sv para no iterar tanto
            int i = 0;
            for(Estacionamiento e : listaEstContentAdapter){
                mPlaces[i] = e.getNombreEstacionamiento().substring(8); //Substring para sacar "NOMBRE:"
                mPlaceDesc[i] = e.getHorarios().substring(10); //Substring para sacar el "HORARIOS:"
                i++;
            }
            TypedArray a = resources.obtainTypedArray(R.array.places_picture);

            /** METODO TOTOALMENTE CROTO PARA PONER IMAGENES REPETIDAS A TODOS LOS ESTACIONAMIENTOS **/
            for (int y=0,x = 0; x < mPlacePictures.length; x++,y++) {
                mPlacePictures[x] = a.getDrawable(y);
                if (x == (a.length()-1)){ y=0;}
            }

            a.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.picture.setImageDrawable(mPlacePictures[position % mPlacePictures.length]);
            holder.name.setText(mPlaces[position % mPlaces.length]);
            holder.description.setText(mPlaceDesc[position % mPlaceDesc.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
