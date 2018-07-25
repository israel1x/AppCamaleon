package com.example.pasantias.appcamaleon;

import android.content.Context;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pasantias.appcamaleon.DataBase.ClienteMin;
import com.example.pasantias.appcamaleon.Pojos.Cliente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

public class Offline extends AppCompatActivity {

    private Button btCargarClientes;
    private ProgressBar pbDescargas;
    private TextView tvPb;

    List<ClienteMin> listaClientesDelDia = new ArrayList<>();
    String fechaHoy;
    Date fechaDelDiaDeHoy;

    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        btCargarClientes =  findViewById(R.id.bt_cargar_clientes);
        pbDescargas = findViewById(R.id.pb_descargas);
        tvPb =  findViewById(R.id.tv_pb);


        //fechaHoy = "2018-07-25";

        fechaHoy = obtenerFechaDelDia(fechaHoy);
        Log.d("Fecha hoy 3: ", fechaHoy );

        if (!fechaHoy.isEmpty() || fechaHoy.equals(null)) {
            btCargarClientes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (comprobarSalidaInternet()) {
                        pbDescargas.setVisibility(View.VISIBLE);
                        pbDescargas.setProgress(0);
                        //descargarClientesDelDia(listaClientesDelDia,tokenEjemplo,fechaHoy);
                        tareaDescargarClientes(listaClientesDelDia,tokenEjemplo,fechaHoy);
                        //pbDescargas.setProgress(100);
                    } else {
                        Toast.makeText(getApplicationContext(), "Debe tener una conexión a internet" , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "La fecha no es correcta" , Toast.LENGTH_SHORT).show();
        }

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

    public String obtenerFechaDelDia(String fechaHoy) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar  calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
            fechaHoy =  mdformat.format(calendar.getTime());
        } else {
            java.util.Calendar calendar1 = java.util.Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
            Date date  = calendar1.getTime();
            fechaHoy = mdformat.format(date);
        }
        return fechaHoy;
    }


    public void descargarClientesDelDia(final List<ClienteMin> clienteMins, String token, String dateHoy) {

        final ClienteMin clienteMin = new ClienteMin();
        final String[] id = new String[1];
        final String[] nombre = new String[1];
        final String[] latitud = new String[1];
        final String[] longitud = new String[1];
        final String[] direccion = new String[1];
        final String[] ruc = new String[1];
        final String[] telefono = new String[1];

        //Fecha del dia
        //dateHoy = "2018-07-25";

        JSONObject jsonObjectCliente = new JSONObject();
        try {
            jsonObjectCliente.put("metodo","listaClientes");
            jsonObjectCliente.put("token",token);
            jsonObjectCliente.put("offset", 0);
            jsonObjectCliente.put(	"limit",0);
            jsonObjectCliente.put("fecha",dateHoy );
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
        JsonObjectRequest jsonObjectRequestDescargaDeListaDeClientes = new JsonObjectRequest(
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


                                Log.d("nombre cliente:",nombre[0] );
                            }
                            Log.d("detalle de clientes:",clienteMins.toString() );
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
        requestQueue.add(jsonObjectRequestDescargaDeListaDeClientes);
    }


    public void tareaDescargarClientes( final List<ClienteMin> clienteMins, final String token,final String fechaHoy) {

        new AsyncTask<String, Integer, List<ClienteMin>>() {
            @Override
            protected List<ClienteMin> doInBackground(String... strings) {
                //descargarClientesDelDia(clienteMins,token,fechaHoy);

                final ClienteMin clienteMin = new ClienteMin();
                final String[] id = new String[1];
                final String[] nombre = new String[1];
                final String[] latitud = new String[1];
                final String[] longitud = new String[1];
                final String[] direccion = new String[1];
                final String[] ruc = new String[1];
                final String[] telefono = new String[1];
                final int[] cont = {0};

                JSONObject jsonObjectCliente = new JSONObject();
                try {
                    jsonObjectCliente.put("metodo","listaClientes");
                    jsonObjectCliente.put("token",token);
                    jsonObjectCliente.put("offset", 0);
                    jsonObjectCliente.put(	"limit",0);
                    jsonObjectCliente.put("fecha",fechaHoy );
                }catch (JSONException e){
                    e.printStackTrace();
                }
                final JSONObject[] jsonObjectRutas = {new JSONObject()};
                final JSONArray[] jsonArrayDetalleRutas = {new JSONArray()};
                final JSONObject[] jsonObjectUno = {new JSONObject()};

                String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";
                Log.d("Web S. ListaClientes:", url);

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsonObjectRequestDescargaDeListaDeClientes = new JsonObjectRequest(
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
                                    int numElementos = jsonArrayDetalleRutas[0].length();
                                    cont[0] = 0;
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
                                        //cont[0] = cont[0] + 1;
                                        int porcentaje = (int) ((i+1/(float)(numElementos)) * 100);
                                        //publishProgress((int)porcentaje);
                                        publishProgress((int) ((i+1/(float)numElementos) * 100));

                                       // Log.d("Num E:", String.valueOf(numElementos));
                                        //Log.d("Contador:", String.valueOf(cont[0]));
                                        Log.d("Porcentaje:", String.valueOf(porcentaje));
                                        Log.d("nombre cliente:",nombre[0] );
                                    }
                                    //Log.d("detalle de clientes:",clienteMins.toString() );
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
                requestQueue.add(jsonObjectRequestDescargaDeListaDeClientes);

                return clienteMins;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                tvPb.setText(values[0].toString());
                pbDescargas.setProgress(values[0]);
            }


            @Override
            protected void onPostExecute(List<ClienteMin> clienteMins) {
                super.onPostExecute(clienteMins);
                Toast.makeText(getApplicationContext(), "Se descargaron los clientes" , Toast.LENGTH_SHORT).show();
            }
        }.execute(fechaHoy);
    }

}
