package com.example.pasantias.appcamaleon;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pasantias.appcamaleon.DataBase.AppDatabase;
import com.example.pasantias.appcamaleon.DataBase.ClienteMin;
import com.example.pasantias.appcamaleon.Pojos.Cliente;
import com.example.pasantias.appcamaleon.Util.Cart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import iammert.com.expandablelib.ExpandCollapseListener;
import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

public class ListaClientes extends AppCompatActivity {

    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";
    List<ClienteMin> clienteMins = new ArrayList<>();
    ArrayList<Cliente> clientes = new ArrayList<>();
    public static AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);
        appDatabase = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"clientesdb").allowMainThreadQueries().build();

        //appDatabase = AppDatabase.getAppDatabase(ListaClientes.this);

        ExpandableLayout sectionLinearLayout = (ExpandableLayout) findViewById(R.id.el_listaClientes);

        sectionLinearLayout.setRenderer(new ExpandableLayout.Renderer<Cliente, Cliente>() {
            @Override
            public void renderParent(View view, Cliente model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.tv_parent_nameCliente)).setText(model.nombre);
                 view.findViewById(R.id.arrow).setBackgroundResource(isExpanded ? R.drawable.arrow_up : R.drawable.arrow_down);

            }
            @Override
            public void renderChild(View view, Cliente model, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.tv_child_ci)).setText(model.ruc);
                ((TextView) view.findViewById(R.id.tv_child_dir)).setText(model.direccion);
                ((TextView) view.findViewById(R.id.tv_child_telf)).setText(model.telefono);
            }

        });

        if ( comprobarSalidaInternet()) {
            traerRutaDeClientes(clienteMins,tokenEjemplo,sectionLinearLayout);
        } else {
            Toast.makeText(getApplicationContext(), "Cargando clientes de hoy" , Toast.LENGTH_SHORT).show();

            //clienteMins = appDatabase.clienteMinDao().getAll();

            getClienteMinsLocales(clienteMins,sectionLinearLayout);

            Log.d("Clientes leidos DB",clienteMins.toString());
        }

        //traerRutaDeClientes(clienteMins,tokenEjemplo,sectionLinearLayout);

        //Log.d("datos de los clientes", clienteMins.toString());
        //Log.d("datos de los clientes :", clientes.toString() );

        sectionLinearLayout.setExpandListener(new ExpandCollapseListener.ExpandListener<Cliente>() {
            @Override
            public void onExpanded(int parentIndex, final Cliente parent, View view) {
                //Layout expanded

                Button nuevoPedido = findViewById(R.id.bt_child_nuevo_pedido);
                Button verUbicacion = findViewById(R.id.bt_child_ver_ubicacion);

                nuevoPedido.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ListaClientes.this, IngresarPedido.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Cart.setCliente(parent);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "Nuevo Pedido de: " +parent.getNombre(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                verUbicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ListaClientes.this, UbicacionCliente.class);
                        i.putExtra("LatCliente", parent.getLatC());
                        i.putExtra("LngCliente", parent.getLngC());
                        startActivity(i);
                    }
                });
            }
        });
        sectionLinearLayout.setCollapseListener(new ExpandCollapseListener.CollapseListener<Cliente>() {
            @Override
            public void onCollapsed(int parentIndex, Cliente parent, View view) {
                //Layout collapsed
            }
        });
    }

    public boolean comprobarSalidaInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Si hay conexión a Internet en este momento
            Log.d("Camaleon APP", " Estado actual: " + networkInfo.getState());
            return true;
        } else {
            // No hay conexión a Internet en este momento
            Log.d("Camaleon APP", "Estás offline");
            return false;
        }
    }



    public void consultarWSListaClientes(String token){


        JSONObject jsonObjectCliente = new JSONObject();
        try {
            jsonObjectCliente.put("metodo","listaClientes");
            jsonObjectCliente.put("token",token);
        }catch (JSONException e){
            e.printStackTrace();
        }

        final ArrayList<Cliente> data_Clientes = new ArrayList<Cliente>();

        //Log.d("ZZZZZZZZZ: ", productoBuscado);
        //productoBuscado = productoBuscado.toString();
        //Consulta al api de productos
        String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";


        Log.d("Ruta al web service: ", url);
        ////Uso del web service para traer los productos

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequestDatosDelproducto = new JsonArrayRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseProductos) {
                        //Response OK!! :)
                        Log.d("DATOS DEL PRODUCTO",responseProductos.toString());


                        try {
                            JSONArray dataProductos = responseProductos;
                            Log.d("ISRAEL", dataProductos.toString());

                            if ((dataProductos).length()== 0) {
                                Log.d("No DATOS DEL PRODUCTO: ", "No tiene DATOS");
                                Toast.makeText(getApplicationContext(),"No tiene DATOS",Toast.LENGTH_LONG).show();
                                //Si no existe ningun elemento muestro un mensahj que no hay datos del  PRODUCTO

                            } else {
                                Log.d("ISRAEL", dataProductos.toString());
                                Toast.makeText(getApplicationContext(),"No tiene DATOS", Toast.LENGTH_LONG).show();

                            }
                        }catch (Error error){
                            Log.d("ERROR Ay",error.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR X",error.toString());
            }
        });

        requestQueue.add(jsonArrayRequestDatosDelproducto);

        //return data_productos;
    }



    public Section<Cliente, Cliente> getSection(int i) {

        Section<Cliente, Cliente> section = new Section<>();
        Cliente padre = clientes.get(i);
        Cliente ruc = clientes.get(i);
        //Cliente direccion = clientes.get(0);

        section.parent = padre;
        section.children.add(ruc);
        //section.children.add(direccion);
        section.expanded = false;

        return section;
    }

    public void conjuntoDeSecciones(ArrayList<Cliente> clientes, ExpandableLayout sectionLinearLayout ) {

        for (int i = 0; i < clientes.size(); i++) {

            Section<Cliente, Cliente> section = new Section<>();
            Cliente padre = clientes.get(i);
            Cliente ruc = clientes.get(i);
            //Cliente direccion = clientes.get(0);

            section.parent = padre;
            section.children.add(ruc);
            //section.children.add(direccion);
            section.expanded = false;

            sectionLinearLayout.addSection(section);
        }

    }

    public void traerRutaDeClientes(final List<ClienteMin> clienteMins, String token, final ExpandableLayout sectionLinearLayout) {

        final ClienteMin clienteMin = new ClienteMin();
        final String[] id = new String[1];
        final String[] nombre = new String[1];
        final String[] latitud = new String[1];
        final String[] longitud = new String[1];
        final String[] direccion = new String[1];
        final String[] ruc = new String[1];
        final String[] telefono = new String[1];

        JSONObject jsonObjectCliente = new JSONObject();
        try {
            jsonObjectCliente.put("metodo","listaClientes");
            jsonObjectCliente.put("token",token);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final JSONObject[] jsonObjectRutas = {new JSONObject()};
        final JSONArray[] jsonArrayDetalleRutas = {new JSONArray()};
        final JSONObject[] jsonObjectUno = {new JSONObject()};

        String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";
        Log.d("Web S. ListaClientes:", url);
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
                            Log.d("Tamaño :", String.valueOf(jsonArrayDetalleRutas[0].length()));
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
                                clienteMin.setLattCliente(latitud[0]);
                                clienteMin.setLongCliente(longitud[0]);
                                clienteMin.setNameCliente(nombre[0]);
                                clienteMin.setDirCliente(direccion[0]);

                                Section<Cliente, Cliente> section = new Section<>();
                                Cliente clienteTest = new Cliente(Integer.parseInt(id[0]),ruc[0],nombre[0],direccion[0],telefono[0],Double.parseDouble(latitud[0]),Double.parseDouble(longitud[0]));
                                Cliente padre = clienteTest;
                                Cliente ruc = clienteTest;

                                section.parent = padre;
                                section.children.add(ruc);
                                //clienteMins.add(clienteMin);
                                section.expanded = false;

                                sectionLinearLayout.addSection(section);

                                //appDatabase.clienteMinDao().insertOne(clienteMin);
                                Log.d("detalle ruta :",nombre[0] );
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


    public  static List<ClienteMin> getClienteMinsLocales(List<ClienteMin> clienteMins, final ExpandableLayout sectionLinearLayout) {
        final ArrayList<Cliente> clientes = new ArrayList<>();
        new AsyncTask<List<ClienteMin>, Void, List<ClienteMin>>() {
            @Override
            protected List<ClienteMin> doInBackground(List<ClienteMin>... lists) {
                Log.d("trabajando en :","traer clientes db" );
                //List<ClienteMin> clientesxxx;
                //clientesxxx = appDatabase.clienteMinDao().getAll();
                //Log.d("trabajando en :",clientesxxx.toString() );
                return appDatabase.clienteMinDao().getAll();
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);

                Log.d("clientes cargados :", String.valueOf(values));
            }

            @Override
            protected void onPostExecute(List<ClienteMin> clienteMins) {
                super.onPostExecute(clienteMins);
                Log.d("clientes cargados :",clienteMins.get(0).getNameCliente());

                clienteMinTOcliente(clienteMins,clientes);
                for ( Cliente clienteN : clientes) {
                    Section<Cliente, Cliente> section = new Section<>();
                    //Cliente clienteTest = new Cliente(ruc[0],nombre[0],direccion[0],telefono[0],Double.parseDouble(latitud[0]),Double.parseDouble(longitud[0]));
                    Cliente padre = clienteN;
                    Cliente ruc = clienteN;

                    section.parent = padre;
                    section.children.add(ruc);
                    //clienteMins.add(clienteMin);
                    section.expanded = false;

                    sectionLinearLayout.addSection(section);
                }
            }
        }.execute(clienteMins);
        return clienteMins;
    }

    private static void clienteMinTOcliente(List<ClienteMin> clienteMins, ArrayList<Cliente> clientes) {

        for (ClienteMin clienteMin : clienteMins) {
            clientes.add(new Cliente(Integer.parseInt(clienteMin.getIdCliente()),clienteMin.getRucCliente(),clienteMin.getNameCliente(),clienteMin.getDirCliente(),clienteMin.getTelfCliente(),Double.parseDouble(clienteMin.getLattCliente()),Double.parseDouble(clienteMin.getLongCliente())));
        }
    }
}
