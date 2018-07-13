package com.example.pasantias.appcamaleon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MenuPrincipal extends AppCompatActivity {

    private ImageButton btVerPedidos;
    private ImageButton btRutaClientes;
    private ImageButton btListClientes;
    private ImageButton btOffline;
    private ImageButton btIngPedidos;
    private ImageButton btReportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        btVerPedidos = (ImageButton) findViewById(R.id.bt_ver_pedidos);
        btRutaClientes = (ImageButton) findViewById(R.id.bt_ruta_clientes);
        btListClientes = (ImageButton) findViewById(R.id.bt_list_clientes);
        btOffline = (ImageButton) findViewById(R.id.bt_offline);
        btIngPedidos = (ImageButton) findViewById(R.id.bt_ing_pedidos);
        //btReportes = (ImageButton) findViewById(R.id.bt_reportes);



        btListClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuPrincipal.this, ListaClientes.class);
                startActivity(i);
            }
        });

        btRutaClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuPrincipal.this, MapsActivity.class);
                startActivity(i);
            }
        });

    }
}
