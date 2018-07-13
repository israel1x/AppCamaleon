package com.example.pasantias.appcamaleon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuPrincipal extends AppCompatActivity {

    private Button btIngresarPedido;
    private Button btVerPedidos;
    private Button btRutaDeClientes;
    private Button btListaClientes;
    private Button btPedidosOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);


        btIngresarPedido = (Button) findViewById(R.id.bt_ingresar_pedido);
        btVerPedidos = (Button) findViewById(R.id.bt_ver_pedidos);
        btRutaDeClientes = (Button) findViewById(R.id.bt_ruta_de_clientes);
        btListaClientes = (Button) findViewById(R.id.bt_lista_clientes);
        btPedidosOffline = (Button) findViewById(R.id.bt_pedidos_offline);


        btListaClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuPrincipal.this, ListaClientes.class);
                startActivity(i);
            }
        });

        btRutaDeClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuPrincipal.this, MapsActivity.class);
                startActivity(i);
            }
        });

    }
}
