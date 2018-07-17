package com.example.pasantias.appcamaleon;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pasantias.appcamaleon.DataBase.AppDatabase;
import com.example.pasantias.appcamaleon.DataBase.ClienteMin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipal extends AppCompatActivity {

    private ImageButton btVerPedidos;
    private ImageButton btRutaClientes;
    private ImageButton btListClientes;
    private ImageButton btOffline;
    private ImageButton btIngPedidos;
    private ImageButton btReportes;

    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";

    public static AppDatabase appDatabase;

    List<ClienteMin> clienteMins = new ArrayList<>();
    List<ClienteMin> rutas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Toolbar miActionBar = (Toolbar)  findViewById(R.id.miActionBar);
        setSupportActionBar(miActionBar);

        btVerPedidos = (ImageButton) findViewById(R.id.bt_ver_pedidos);
        btRutaClientes = (ImageButton) findViewById(R.id.bt_ruta_clientes);
        btListClientes = (ImageButton) findViewById(R.id.bt_list_clientes);
        btOffline = (ImageButton) findViewById(R.id.bt_offline);
        btIngPedidos = (ImageButton) findViewById(R.id.bt_ing_pedidos);
        btReportes = (ImageButton) findViewById(R.id.bt_reportes);

        //traerRutaDeClientes(clienteMins,tokenEjemplo);

        // la base debe ser instanciada una sola vez en la aplicacion
        //appDatabase = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"clientesdb").allowMainThreadQueries().build();

        //appDatabase.clienteMinDao().insertAll(clienteMins);


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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
       if (menuItemThatWasSelected == R.id.action_test) {
           Log.d("Menu", "item xxxx");
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("LongLogTag")
    public void traerRutaDeClientes(final List<ClienteMin> clienteMins, String token) {
        //public void traerRutaDeClientes(String token) {
        final ClienteMin clienteMin = new ClienteMin();
        final String[] id = new String[1];
        final String[] ruc = new String[1];
        final String[] nombre = new String[1];
        final String[] direccion = new String[1];
        final String[] telefono = new String[1];
        final String[] latitud = new String[1];
        final String[] longitud = new String[1];

        JSONObject jsonObjectCliente = new JSONObject();
        try {
            jsonObjectCliente.put("metodo","listaClientes");
            jsonObjectCliente.put("token",token);
        }catch (JSONException e){
            e.printStackTrace();
        }
        //final ArrayList<Cliente> data_Clientes = new ArrayList<Cliente>();

        final JSONObject[] jsonObjectRutas = {new JSONObject()};
        final JSONArray[] jsonArrayDetalleRutas = {new JSONArray()};
        final JSONObject[] jsonObjectUno = {new JSONObject()};

        String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";

        Log.d("Web service rutaClientes:", url);
        ////Uso del web service para traer la ruta de clientes
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequestRequestRutaDeClientes = new JsonObjectRequest(
                url,
                jsonObjectCliente,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Data del WS", response.toString());
                        try {
                            jsonObjectRutas[0] = response.getJSONObject("lista");
                            jsonArrayDetalleRutas[0] = jsonObjectRutas[0].getJSONArray("listaClientes");
                            Log.d("RUTAS :", jsonArrayDetalleRutas[0].toString());
                            Log.d("RUTAS :", String.valueOf(jsonArrayDetalleRutas[0].length()));
                            for (int i = 0; i < jsonArrayDetalleRutas[0].length(); i++) {
                                jsonObjectUno[0] = jsonArrayDetalleRutas[0].getJSONObject(i);
                                id[0] = jsonObjectUno[0].getString("id_cliente");
                                ruc[0] = jsonObjectUno[0].getString("ruc");
                                nombre[0] = jsonObjectUno[0].getString("nombre");
                                direccion[0] = jsonObjectUno[0].getString("direccion");
                                telefono[0] = jsonObjectUno[0].getString("telefono");
                                latitud[0] = jsonObjectUno[0].getString("latitud");
                                longitud[0] = jsonObjectUno[0].getString("longitud");

                                clienteMin.setIdCliente(id[0]);
                                clienteMin.setRucCliente(ruc[0]);
                                clienteMin.setNameCliente(nombre[0]);
                                clienteMin.setDirCliente(direccion[0]);
                                clienteMin.setTelfCliente(telefono[0]);
                                clienteMin.setLattCliente(latitud[0]);
                                clienteMin.setLongCliente(longitud[0]);
                                clienteMins.add(clienteMin);

                                //Log.d("detalle ruta :",id[0] );
                            }
                            Log.d("detalle ruta :",clienteMins.toString() );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
            }
        }
        );
        requestQueue.add(jsonObjectRequestRequestRutaDeClientes);
    }


    //Metodo que trae la lista de clientes del Web Service
    @SuppressLint("StaticFieldLeak")
    public void traerListaDeClientes(final List<ClienteMin> clienteMins, String token) {
        new AsyncTask<String, Void, List<ClienteMin>>() {
            @Override
            protected List doInBackground(String... strings) {
                return null;
            }
        }.execute(token);

    }

    //Metodo que iserta la lista de clientes en la base, en segundo plano
    public void insertarListaDeClientes(List<ClienteMin> clienteMins) {


    }

}
