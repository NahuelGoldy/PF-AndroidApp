package com.example.puchoo.mapmaterial.VistasAndControllers.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.puchoo.mapmaterial.Dao.EstacionamientoDAO;
import com.example.puchoo.mapmaterial.Dao.FavoritosDAO;
import com.example.puchoo.mapmaterial.Exceptions.EstacionamientoException;
import com.example.puchoo.mapmaterial.Modelo.Estacionamiento;
import com.example.puchoo.mapmaterial.R;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Puchoo on 02/09/2017.
 */

public class FavoritosActivity extends AppCompatActivity {

    /** Lista de listaEstacionamientos */
    private ArrayList<Estacionamiento> listaEstacionamientos = new ArrayList<>();

    /** Adapter**/
    ContentAdapter adapter;

    /** Dao que almacena ubicacion de listaEstacionamientos */
    private static final FavoritosDAO estacionamientoDAO = FavoritosDAO.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_favoritos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        /** Cargo la lista de estacionamientos para luego hacer las Card**/
        //cargarEstacionamientos();
        listaEstacionamientos = FavoritosDAO.getInstance().getFavoritosSharedPref(getBaseContext());

        adapter = new ContentAdapter(recyclerView.getContext(),listaEstacionamientos);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        ((Toolbar) findViewById(R.id.toolbar_favoritos)).setTitle("Estacionamientos Favoritos");

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;
        public TextView description;
        public ImageButton btnFavorito;
        public final ArrayList<Estacionamiento> listaEstHolder;
        public ViewHolder(LayoutInflater inflater, final ViewGroup parent, ArrayList<Estacionamiento> listaEstacionamiento) {
            super(inflater.inflate(R.layout.item_card, parent, false));
            this.listaEstHolder = listaEstacionamiento;

            picture = (ImageView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_text);
            btnFavorito = (ImageButton) itemView.findViewById(R.id.favorite_button);
            btnFavorito.setImageDrawable(itemView.getContext().getDrawable(R.drawable.ic_favorite_red));

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
            Button buttonReserva = (Button)itemView.findViewById(R.id.reserva_button);
            buttonReserva.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Reservando.....",
                            Snackbar.LENGTH_LONG).show();

                    Context context = v.getContext();
                    Intent intent = new Intent(context, ReservarActivity.class);
                    intent.putExtra(ReservarActivity.EXTRA_POSITION, getAdapterPosition());
                    context.startActivity(intent);

                }
            });

            ImageButton favoriteImageButton =
                    (ImageButton) itemView.findViewById(R.id.favorite_button);
            favoriteImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(!listaEstHolder.get(getAdapterPosition()).isFavorito()) {
                        FavoritosDAO.getInstance().borrarFavoritoSharedPref(listaEstHolder.get(getAdapterPosition()), parent.getContext());
                        Snackbar.make(v, "Estacionamiento retirado de Favoritos",
                                Snackbar.LENGTH_LONG).show();

                        ((ImageButton)v.findViewById(R.id.favorite_button)).setImageResource(R.drawable.ic_favorite);
                        //lo seteo como favorito
                        listaEstHolder.get(getAdapterPosition()).setFavorito(false);
                        try {
                            EstacionamientoDAO.getInstance().actualizarEstacionamiento(listaEstHolder.get(getAdapterPosition()),itemView.getContext());
                            listaEstHolder.remove(getAdapterPosition());
                        } catch (EstacionamientoException e) {
                            String msg = itemView.getResources().getString(R.string.errorProducidoIntenteNuevamente);
                            Log.v("Favoritos Activity",msg);
                        }

                    } else {
                        Snackbar.make(v, "Estacionamiento retirado de  Favoritos",
                                Snackbar.LENGTH_LONG).show();
                    }
                }
            });

            ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_button);
            shareImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Compartir estacionamiento",
                            Snackbar.LENGTH_LONG).show();
                    Context context = v.getContext();
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    //Seteo el tipo de send Intent
                    sendIntent.setType("text/plain");

                    // Always use string resources for UI text.
                    String title = "Compartir con:";
                    // Create intent to show the chooser dialog
                    Intent chooser = Intent.createChooser(sendIntent, title);

                    sendIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                            //TODO CAMBIaR A UN TEXTO COHERENTE
                            "Descagar la App \"Donde estaciono?\" y podras reservar en: ");

                    // Verify the original intent will resolve to at least one activity
                    if (sendIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(chooser);
                    }
                }
            });
        }
    }

    /**********************************************************************************************
     *******************************Adapter to display recycler view.*******************************
     **********************************************************************************************/
    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private int LENGTH = 18;

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

            if(listaEstContentAdapter.isEmpty()){
                LENGTH = 0; //Si la lista es vacia seteo en 0 el la cant de items
            } else {
                LENGTH = listaEstContentAdapter.size();
            }

            //TODO Remplazarlos resources por peticiones al sv para no iterar tanto
            int i = 0;
            for(Estacionamiento e : listaEstContentAdapter){
                mPlaces[i] = e.getNombreEstacionamiento();
                mPlaceDesc[i] = e.getHorarios();

                if (e.getImagen() == null){
                    mPlacePictures[i] = getBaseContext().getDrawable(R.drawable.img_parque_default);
                }

                mPlacePictures[i] = getBaseContext().getDrawable(R.drawable.img_parque_default);

                i++;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent,listaEstContentAdapter);
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(mPlaceDesc.length != 0) {
                holder.picture.setImageDrawable(mPlacePictures[position % mPlacePictures.length]);
                holder.name.setText(mPlaces[position % mPlaces.length]);
                holder.description.setText(mPlaceDesc[position % mPlaceDesc.length]);
            }
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
