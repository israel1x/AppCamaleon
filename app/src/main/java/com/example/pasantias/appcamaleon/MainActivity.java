package com.example.pasantias.appcamaleon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.pasantias.appcamaleon.Fragments.ListaDeClientes;
import com.example.pasantias.appcamaleon.Fragments.MapsActivity;
import com.example.pasantias.appcamaleon.Fragments.MenuPrin;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MenuPrin.OnFragmentInteractionListener {

    FrameLayout frameContentPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

       // MenuPrin menuPrin = new MenuPrin();
        //frameContentPrincipal = findViewById(R.id.nav_content_principal);
        //getSupportFragmentManager().beginTransaction().add(R.id.,menuPrin).commit();

        //fragment = findViewById(R.id.nav_content_principal);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new MenuPrin();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_content_principal,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            Intent intent = new Intent(MainActivity.this, IngresarPedido.class);
            startActivity(intent);
        } else if (id == R.id.nav_offline) {
            Intent i = new Intent(MainActivity.this, Offline.class);
            startActivity(i);

        } else if (id == R.id.nav_listado_pedidos) {
            Intent i = new Intent(MainActivity.this, ListaPedidos.class);
            startActivity(i);
        } else if (id == R.id.nav_ruta_clientes) {
            item.setChecked(true);
            //fragment = new MapsActivity();
            //fragmentMaps = new MapsActivity();
            Intent i = new Intent(MainActivity.this, RutaDeClientes.class);
            startActivity(i);

        } else if (id == R.id.nav_lista_clientes) {
            item.setChecked(true);
            //fragment = new ListaDeClientes();
            Intent i = new Intent(MainActivity.this, ListaClientes.class);
            startActivity(i);
        }

       if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_content_principal,fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
