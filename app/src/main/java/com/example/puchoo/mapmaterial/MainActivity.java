package com.example.puchoo.mapmaterial;


import android.app.Dialog;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.puchoo.mapmaterial.Utils.ConstantsNavigatorView;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener{

    /** Booleano que sirve para identificar si pulsó dos veces para salir */
    boolean doubleBackToExitPressedOnce = false;

    private DrawerLayout mDrawerLayout;
    private ListContentFragment mapFragment;
    private CardContentFragment cardFragment;
    private  TileContentFragment tileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Genero el movimiento entre fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        //Genero las Tabs
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Mapa"));
        tabs.addTab(tabs.newTab().setText("Estacionamientos"));
        tabs.addTab(tabs.newTab().setText("Donde estacionaste"));
        //Le seteo el movimiento entre taps con los fragments
        tabs.setupWithViewPager(viewPager);

        // Create Navigation drawer and inflate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.setDrawerListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        //SetEnable el boton de estacionar Aqui del Nav
        navigationView.getMenu().getItem(ConstantsNavigatorView.INDICE_MENU_ESTACIONAR_AQUI).setEnabled(true);
        //SetEnable el boton de Alarma del Nav
        navigationView.getMenu().getItem(ConstantsNavigatorView.INDICE_MENU_ALARMA).setEnabled(true);
        //SetEnale el btn ver donde estacione
        navigationView.getMenu().getItem(ConstantsNavigatorView.INDICE_MENU_VER_ESTACIONAMIENTO).setEnabled(false);
        /*
            TODO Habilitar o desabilitar el boton estacionar aqui
         */

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        mapFragment = new ListContentFragment();//Creo el fragmento del mapa
        cardFragment = new CardContentFragment();//Creo el fragmento de la lista de estacionamientos
        tileFragment = new TileContentFragment();//Creo el 3er fragmento - Sin uso especifico

        adapter.addFragment(mapFragment, "Mapa");
        adapter.addFragment(cardFragment, "Estacionamientos");
        adapter.addFragment(tileFragment, "Donde estacionaste");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Set item in checked state
        Integer idItem = item.getItemId();
        System.out.println("***************************************");
        System.out.println(idItem);

            if(idItem == ConstantsNavigatorView.INDICE_MENU_ESTACIONAR_AQUI){

            }else if (idItem == ConstantsNavigatorView.INDICE_MENU_VER_ESTACIONAMIENTO){
                System.out.println("CLICKEO VER ESTACIONAMIENTO");
                mapFragment.verDondeEstaciono();
                item.setChecked(false);
            }else if(idItem == ConstantsNavigatorView.INDICE_MENU_FAVORITOS){

            }else if(idItem == ConstantsNavigatorView.INDICE_MENU_ALARMA){

            }else if(idItem == ConstantsNavigatorView.INDICE_MENU_PREFERENCIAS){

            }else if(idItem == ConstantsNavigatorView.INDICE_MENU_RESERVAS){

            }
        // TODO: hacer las acciones al clicker en el nav

        // Closing drawer on item click
        mDrawerLayout.closeDrawers();
        return true;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        String msg;
        DrawerLayout drawer = mDrawerLayout;
        if(drawer.isDrawerOpen(Gravity.LEFT)){
            drawer.closeDrawer(Gravity.LEFT);
        }
        else{
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            }
            else if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                msg = getResources().getString(R.string.presionarAtrasParaSalir);
                Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
            else {
                super.onBackPressed();
                return;
            }
        }
    }

    /********************************************************************************************
     * Listeners del Drawer - Para manejar el Nav, saber cuando abre y cierra, ect.
     *********************************************************************************************/

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {    }

    @Override
    public void onDrawerOpened(View drawerView) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //SetEnable el boton de Alarma del Nav
        navigationView.getMenu().getItem(ConstantsNavigatorView.INDICE_MENU_ALARMA).setEnabled(ConstantsNavigatorView.ENABLE__INDICE_MENU_ALARMA);
        //SetEnable el boton de estacionar Aqui del Nav
        navigationView.getMenu().getItem(ConstantsNavigatorView.INDICE_MENU_ESTACIONAR_AQUI).setEnabled(ConstantsNavigatorView.ENABLE__INDICE_MENU_ESTACIONAR_AQUI);
        //SetNebale el btn ver donde estacione
        navigationView.getMenu().getItem(ConstantsNavigatorView.INDICE_MENU_VER_ESTACIONAMIENTO).setEnabled(ConstantsNavigatorView.ENABLE__INDIACE_MENU_VER_ESTACIONAMIENTO);
    }

    @Override
    public void onDrawerClosed(View drawerView) {    }

    @Override
    public void onDrawerStateChanged(int newState) {    }
}
