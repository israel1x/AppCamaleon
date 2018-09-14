package com.example.pasantias.appcamaleon;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pasantias.appcamaleon.Fragments.MenuPrin;
import com.example.pasantias.appcamaleon.HistorialFragment;
import com.example.pasantias.appcamaleon.IngresarPedidoFragment;
import com.example.pasantias.appcamaleon.Util.Cart;
import com.example.pasantias.appcamaleon.Util.Util;


public class IngresarPedido extends AppCompatActivity implements IngresarPedidoFragment.OnListenerIngresarPedido,HistorialFragment.OnListenerProductoItem{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    // private PedidoFragment pedidoFragment = new PedidoFragment();
    private IngresarPedidoFragment ingresarPedidoFragment=IngresarPedidoFragment.newInstance();
    private HistorialFragment historialFragment=HistorialFragment.newInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_pedido);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public void onBackPressed() {
        if (Cart.countList() != 0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Pedido");
            alertDialog.setMessage("Esta seguro que desea salir, se perderán los datos del pedido");

            alertDialog.setPositiveButton("SI",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Cart.removeListCart();

                            Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("fragment",Util.Constantes.FRAGRUTACLIENTE);
                            startActivity(intent);

                            finish();
                            //finish();
                           // IngresarPedido.this.finish();
                        }
                    });

            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = alertDialog.create();
            alert.show();
        } else {
            IngresarPedido.this.finish();
        }
    }



    @Override
    public void onBuscarHistorial(String idCliente) {
        historialFragment.buscarHistorialCliente(idCliente);
    }


    @Override
    public void actualizarListaItem() {
        ingresarPedidoFragment.actualizarLista();
    }

    public void actualizar(){
        ingresarPedidoFragment.actualizarLista();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    return ingresarPedidoFragment;
                case 1:
                    return historialFragment;

            }
            return null;

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
