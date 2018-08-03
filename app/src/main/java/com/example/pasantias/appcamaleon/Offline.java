package com.example.pasantias.appcamaleon;

import android.arch.persistence.room.Room;
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
import com.example.pasantias.appcamaleon.DataBase.Actualizacion;
import com.example.pasantias.appcamaleon.DataBase.AppDatabase;
import com.example.pasantias.appcamaleon.DataBase.ClienteMin;
import com.example.pasantias.appcamaleon.DataBase.Producto;
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

    public static AppDatabase appDatabase;
    private TextView tvPb;

    private Button btOfflineDownClientes;
    private Button btOfflineDownProductos;
    private Button btOfflineActualizarStock;
    private Button btOfflineActualizarPrecios;
    private ProgressBar pbarOffline;
    private TextView tvPbarPorcentaje;

    List<ClienteMin> listaClientesDelDia = new ArrayList<>();
    List<Producto> listaProductos = new ArrayList<>();
    String fechaHoy;
    String fechaDelDiaDeHoy;

    Actualizacion actualizacionHoy;

    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        appDatabase = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"clientesdb").allowMainThreadQueries().build();


        btOfflineDownClientes = (Button) findViewById(R.id.bt_offline_down_clientes);
        btOfflineDownProductos = (Button) findViewById(R.id.bt_offline_down_productos);
        btOfflineActualizarStock = (Button) findViewById(R.id.bt_offline_actualizar_stock);
        btOfflineActualizarPrecios = (Button) findViewById(R.id.bt_offline_actualizar_precios);
        pbarOffline = (ProgressBar) findViewById(R.id.pbar_offline);

        tvPbarPorcentaje = (TextView) findViewById(R.id.tv_pbar_porcentaje);

        fechaHoy = obtenerFechaDelDia(fechaHoy);
        Log.d("Fecha hoy 3: ", fechaHoy );
        //creamos una nueva fecha de actualizacion
        fechaDelDiaDeHoy = obtenerFechaDeHoyCompleta();
        Log.d("Fecha hoy 4: ", fechaDelDiaDeHoy );
        actualizacionHoy = new Actualizacion(1, fechaDelDiaDeHoy,fechaDelDiaDeHoy);

        //Log.d("Fecha hoy completa: ", fechaDelDiaDeHoy.toString() );

        final int[] countClicks = {0};
        final int[] countClicksProductos = {0};

        if (!fechaHoy.isEmpty() || fechaHoy.equals(null)) {

            btOfflineDownClientes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //appDatabase.actualizacionDao().insertActualizacion(actualizacionHoy);

                    //if (countClicks[0] == 0) {
                        if (comprobarSalidaInternet()) {
                            pbarOffline.setVisibility(View.VISIBLE);
                            tvPbarPorcentaje.setVisibility(View.VISIBLE);
                            pbarOffline.setProgress(0);
                            tvPbarPorcentaje.setText("00%");
                            //descargarClientesDelDia(listaClientesDelDia,tokenEjemplo,fechaHoy);
                            getFechasDeActualizacionDeClientes(1);
                            //tareaDescargarClientes(listaClientesDelDia,tokenEjemplo,fechaHoy);
                            //pbDescargas.setProgress(100);
                        } else {
                            Toast.makeText(getApplicationContext(), "Debe tener una conexión a internet" , Toast.LENGTH_SHORT).show();
                        }
                   // } else {
                        //Toast.makeText(getApplicationContext(), "Ya actualizó sus clientes de hoy" , Toast.LENGTH_SHORT).show();
                   // }
                   // countClicks[0]++;
                }
            });


            btOfflineDownProductos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (countClicksProductos[0] == 0) {
                        if (comprobarSalidaInternet()) {
                            pbarOffline.setVisibility(View.VISIBLE);
                            tvPbarPorcentaje.setVisibility(View.VISIBLE);
                            pbarOffline.setProgress(0);
                            tvPbarPorcentaje.setText("00%");
                            tareaDescargarProductos(listaProductos,tokenEjemplo);

                        } else {
                            Toast.makeText(getApplicationContext(), "Debe tener una conexión a internet" , Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Ya se descargaron los productos de hoy" , Toast.LENGTH_SHORT).show();
                    }
                    countClicksProductos[0]++;
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

    public String obtenerFechaDeHoyCompleta() {
        String fecha = "";
        java.util.Calendar calendar1 = java.util.Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date  = calendar1.getTime();
        fecha = mdformat.format(date);
        return fecha;
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
                                        clienteMin.setNameCliente(nombre[0]);
                                        clienteMin.setRucCliente(ruc[0]);
                                        clienteMin.setDirCliente(direccion[0]);
                                        clienteMin.setTelfCliente(telefono[0]);
                                        clienteMin.setLattCliente(latitud[0]);
                                        clienteMin.setLongCliente(longitud[0]);

                                        appDatabase.clienteMinDao().insertOne(clienteMin);
                                        //cont[0] = cont[0] + 1;
                                        //int porcentaje = (int) ((i+1/(float)(numElementos)) * 50);
                                        int porcentaje = ((i+1) * (100/numElementos));
                                        publishProgress((int)porcentaje);
                                        //publishProgress((int) ((i+1/(float)numElementos) * 50));

                                        //pbarOffline.setProgress(porcentaje);
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
                tvPbarPorcentaje.setText(values[0].toString() + "%");
                pbarOffline.setProgress(values[0]);
                Log.d("valores de progreso:", String.valueOf(values[0]));
            }


            @Override
            protected void onPostExecute(List<ClienteMin> clienteMins) {
                super.onPostExecute(clienteMins);
                Toast.makeText(getApplicationContext(), "Se descargaron los clientes" , Toast.LENGTH_SHORT).show();

                Actualizacion actualizacion = new Actualizacion(1, fechaHoy, fechaHoy);
                //GUARDO EN LA BASE EL REGISTRO (LA FECHA DE DESCARGA DE CLIENTES)
                appDatabase.actualizacionDao().insertActualizacion(actualizacion);
            }
        }.execute(fechaHoy);
    }



    public void tareaDescargarProductos(final List<Producto> listaProductos, final String token) {

        new AsyncTask<String, Integer, List<Producto>>() {
            @Override
            protected List<Producto> doInBackground(String... strings) {

                final Producto productoX = new Producto();
                final String[] id_producto = new String[1];
                final String[] nombre_pro = new String[1];
                final int[] stock_pro = {0};
                final String[] valor_unitario = new String[1];
                final String[] presentacion_pro = new String[1];
                final String[] marca_pro = new String[1];
                final String[] subcategoria_pro = new String[1];
                final int[] cont = {0};

                JSONObject jsonObjectCliente = new JSONObject();
                try {
                    jsonObjectCliente.put("metodo","listaProductos");
                    jsonObjectCliente.put("token",token);
                    jsonObjectCliente.put("offset", 0);
                    jsonObjectCliente.put(	"limit",20);
                    //jsonObjectCliente.put("fecha",fechaHoy );
                }catch (JSONException e){
                    e.printStackTrace();
                }
                final JSONObject[] jsonObjectRutas = {new JSONObject()};
                final JSONArray[] jsonArrayDetalleRutas = {new JSONArray()};
                final JSONObject[] jsonObjectUno = {new JSONObject()};

                String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";
                Log.d("Web S. listaProductos:", url);

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
                                    jsonArrayDetalleRutas[0] = jsonObjectRutas[0].getJSONArray("listaProductos");
                                    Log.d("PRODUCTOS :", jsonArrayDetalleRutas[0].toString());
                                    Log.d("Tamaño :", String.valueOf(jsonArrayDetalleRutas[0].length()));
                                    int numElementos = jsonArrayDetalleRutas[0].length();
                                    cont[0] = 0;
                                    for (int i = 0; i < jsonArrayDetalleRutas[0].length(); i++) {
                                        jsonObjectUno[0] = jsonArrayDetalleRutas[0].getJSONObject(i);
                                        id_producto[0] = jsonObjectUno[0].getString("id_producto");
                                        nombre_pro[0] = jsonObjectUno[0].getString("nombre_producto");
                                        stock_pro[0] = jsonObjectUno[0].getInt("stock");
                                        valor_unitario[0] = jsonObjectUno[0].getString("valor_unitario");
                                        presentacion_pro[0] = jsonObjectUno[0].getString("nombre_presentacion");
                                        marca_pro[0] = jsonObjectUno[0].getString("nombre_marca");
                                        subcategoria_pro[0] = jsonObjectUno[0].getString("nombre_subcategoria");
                                       /* clienteMin.setIdCliente(id[0]);
                                        clienteMin.setNameCliente(nombre[0]);
                                        clienteMin.setRucCliente(ruc[0]);
                                        clienteMin.setDirCliente(direccion[0]);
                                        clienteMin.setTelfCliente(telefono[0]);
                                        clienteMin.setLattCliente(latitud[0]);
                                        clienteMin.setLongCliente(longitud[0]);*/

                                        //appDatabase.clienteMinDao().insertOne(clienteMin);
                                        //cont[0] = cont[0] + 1;
                                        //int porcentaje = (int) ((i+1/(float)(numElementos)) * 50);
                                        int porcentaje = (int) ((i+1) * (100/numElementos));
                                        publishProgress((int)porcentaje);
                                        //publishProgress((int) ((i+1/(float)numElementos) * 50));

                                        //pbarOffline.setProgress(porcentaje);
                                        // Log.d("Num E:", String.valueOf(numElementos));
                                        //Log.d("Contador:", String.valueOf(cont[0]));
                                        Log.d("Porcentaje:", String.valueOf(porcentaje));
                                        Log.d("nombre producto:",nombre_pro[0] );
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

                return listaProductos;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                tvPbarPorcentaje.setText(values[0].toString() + "%");
                pbarOffline.setProgress(values[0]);
                Log.d("valores de progreso:", String.valueOf(values[0]));
            }


            @Override
            protected void onPostExecute(List<Producto> listaProductos) {
                super.onPostExecute(listaProductos);
                Toast.makeText(getApplicationContext(), "Se descargaron los productos" , Toast.LENGTH_SHORT).show();

            }
        }.execute(fechaHoy);
    }



    //METODO QUE VALIDA LA FECHA DE DESCARGA DE LA LISTA DE CLIENTES
    //EVITA DESCARGAR LA MISMA LISTA DE CLIENTES
    public void getFechasDeActualizacionDeClientes(final int id) {
        new AsyncTask<Integer, Void, String>() {

            String actualizacionClientes;
            @Override
            protected String doInBackground(Integer... integers) {
                actualizacionClientes = appDatabase.actualizacionDao().getFechaActualizacionDeClientes(id);
                return actualizacionClientes;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String fechaUpdateClientes;
                String fechaUpdateProductos;

                if ( actualizacionClientes == null ) {
                    tareaDescargarClientes(listaClientesDelDia,tokenEjemplo,fechaHoy);
                    Log.d("Descargando clientes", "de: " + fechaHoy);
                } else {
                    //fechaUpdateClientes = actualizacion.getFechaUpdateClientes();
                    Toast.makeText(getApplicationContext(), "Ya se descargaron los clientes del: " + fechaHoy, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(id);
    }



    //METODO QUE VALIDA LA FECHA DE DESCARGA DE LA LISTA DE PRODUCTOS
    //EVITA DESCARGAR LA MISMA LISTA DE PRODUCTOS
    public void getFechaDeActualizacionDeProductos(final int id) {
        new AsyncTask<Integer, Void, String>() {

            String actualizacionProductos;
            @Override
            protected String doInBackground(Integer... integers) {
                actualizacionProductos = appDatabase.actualizacionDao().getFechaActualizacionDeProductos(id);
                return actualizacionProductos;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (actualizacionProductos == null) {


                    Log.d("Descargando productos", "de: " + fechaHoy);
                } else {
                    Toast.makeText(getApplicationContext(), "Ya se descargaron los productos del: " + fechaHoy, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(id);
    }

}
