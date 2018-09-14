package com.example.pasantias.appcamaleon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.pasantias.appcamaleon.Fragments.ListaDeClientes;
import com.example.pasantias.appcamaleon.Fragments.MapsActivity;
import com.example.pasantias.appcamaleon.Fragments.MenuPrin;
import com.example.pasantias.appcamaleon.Util.Util;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MenuPrin.OnFragmentInteractionListener {

    FrameLayout frameContentPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegation);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences("datosAplicacion", MODE_PRIVATE);
        int modoTrabajo = sharedPreferences.getInt("modoDeTrabajo", 0);
        Log.d("MODO TRABAJO" , String.valueOf(modoTrabajo));

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new MenuPrin();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fr_mainContent,fragment);
        fragmentTransaction.commit();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("fragment");
            if (value != null) {
                setFragment(value);
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navegation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, Configuraciones.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        MapsActivity fragmentMaps = null;
        Fragment fragment = null;
        int id = item.getItemId();
        if (id == R.id.nav_ing_pedido) {
           // Intent intent = new Intent(MainActivity.this, IngresarPedido.class);
          //  startActivity(intent);
            item.setChecked(true);
            fragment = new RutaClientesPedido();

        } else if (id == R.id.nav_offline) {
            item.setChecked(true);
            fragment = new Offline();

        } else if (id == R.id.nav_listado_pedidos) {
            Intent i = new Intent(MainActivity.this, ListaPedidos.class);
            startActivity(i);

        } else if (id == R.id.nav_ruta_clientes) {
            item.setChecked(true);
            fragment = new RutaDeClientes();

        } else if (id == R.id.nav_lista_clientes) {
            item.setChecked(true);
            fragment = new ListaClientes();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fr_mainContent,fragment);
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void callFragment(int containerViewId,Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId,fragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }

    public void setFragment(String mParam1){
        switch (mParam1){
            case Util.Constantes.FRAGRUTACLIENTE:
                callFragment(R.id.fr_mainContent,new RutaClientesPedido());

        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
