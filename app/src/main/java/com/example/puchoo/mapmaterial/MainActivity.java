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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /** Booleano que sirve para identificar si pulsó dos veces para salir */
    boolean doubleBackToExitPressedOnce = false;

    private DrawerLayout mDrawerLayout;

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
        navigationView.setNavigationItemSelectedListener(this);
        //SetEnable el boton de estacionar Aqui del Nav
        navigationView.getMenu().getItem(ConstantsNavigatorView.INDICE_MENU_ESTACIONAR_AQUI).setEnabled(true);
        //SetEnable el boton de Alarma del Nav
        navigationView.getMenu().getItem(ConstantsNavigatorView.INDICE_MENU_ALARMA).setEnabled(true);
        //SetVisible el boton de Ver Estacionmiento
        navigationView.getMenu().getItem(ConstantsNavigatorView.INDICE_MENU_VER_ESTACIONAMIENTO).setVisible(false);
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
        adapter.addFragment(new ListContentFragment(), "Mapa");
        adapter.addFragment(new CardContentFragment(), "Estacionamientos");
        adapter.addFragment(new TileContentFragment(), "Donde estacionaste");
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
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            //SetEnable el boton de estacionar Aqui del Nav
            navigationView.getMenu().getItem(ConstantsNavigatorView.INDICE_MENU_ESTACIONAR_AQUI).setEnabled(ConstantsNavigatorView.ENABLE__INDICE_MENU_ESTACIONAR_AQUI);
            //SetEnable el boton de Alarma del Nav
            navigationView.getMenu().getItem(ConstantsNavigatorView.INDICE_MENU_ALARMA).setEnabled(ConstantsNavigatorView.ENABLE__INDICE_MENU_ALARMA);

            navigationView.getMenu().getItem(ConstantsNavigatorView.INDICE_MENU_VER_ESTACIONAMIENTO).setVisible(ConstantsNavigatorView.VIEW_INDICE_MENU_VER_ESTACIONAMIENTO);
            navigationView.getMenu().getItem(ConstantsNavigatorView.INDICE_MENU_ESTACIONAR_AQUI).setVisible(ConstantsNavigatorView.VIEW_INDICE_MENU_ESTACIONAR_AQUI);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Set item in checked state

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

}
