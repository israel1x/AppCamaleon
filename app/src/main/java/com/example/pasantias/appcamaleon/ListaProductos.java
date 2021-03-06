package com.example.pasantias.appcamaleon;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasantias.appcamaleon.Adapters.ProductoAutoCompleteAdapte;
import com.example.pasantias.appcamaleon.Adapters.ProductosAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pasantias.appcamaleon.DataBase.AppDatabase;
import com.example.pasantias.appcamaleon.Pojos.Producto;
import com.example.pasantias.appcamaleon.Util.Cart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ListaProductos extends AppCompatActivity {

    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";

    private static final String TAG = "Producto";
    private RecyclerView recyclerView;
    private ProductosAdapter productosAdapter;
    private int offset;
    private boolean aptoParaCargar;

    public static AppDatabase appDatabase;


    private Integer autoCompleteLetter = 0;
    private ProductoAutoCompleteAdapte productoAutoCompleteAdapte;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private String keyWord = " ";
    private boolean blockSearch=false;
   // private Button bt_buscar_producto;
    private TextView textFecha;
    String currentDateandTime;
    private AutoCompleteTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        textFecha= (TextView) findViewById(R.id.textFecha);
        //bt_buscar_producto = (Button) findViewById(R.id.bt_buscar_producto);

         textView = findViewById(R.id.edit_buscar_producto);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewProductos);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDateandTime = sdf.format(new Date());
        textFecha.setText(currentDateandTime);

        appDatabase = AppDatabase.getAppDatabase(getApplication());
      /*  bt_buscar_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_buscar_producto.setEnabled(false);
                keyWord = String.valueOf(textView.getText());
                aptoParaCargar = true;
                offset = 0;
                productosAdapter.limpiarLista();
                obtenerDatos(tokenEjemplo, offset, keyWord);
            }
        });*/


        productosAdapter = new ProductosAdapter(this);
        recyclerView.setAdapter(productosAdapter);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstCompletelyVisibleItemPosition();
                    if (aptoParaCargar) {
                         if((visibleItemCount+pastVisibleItems)>=totalItemCount){
                       // if (comprobarSalidaInternet()) {
                            aptoParaCargar = false;
                            offset += 1;
                             if(comprobarSalidaInternet()) {
                                 obtenerDatos(tokenEjemplo, offset, keyWord);
                             }else{
                                 obtenerDatosOffline(offset,keyWord);
                             }
                        //}
                         }
                    }
                }
            }
        });



        productoAutoCompleteAdapte = new ProductoAutoCompleteAdapte(this,android.R.layout.simple_dropdown_item_1line);
        textView.setThreshold(3);
        textView.setAdapter(productoAutoCompleteAdapte);

        textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !blockSearch) {
                    blockSearch=true;
                    keyWord = String.valueOf(textView.getText());
                    aptoParaCargar = true;
                    offset = 0;
                    productosAdapter.limpiarLista();
                    if(comprobarSalidaInternet()) {
                        obtenerDatos(tokenEjemplo, offset, keyWord);
                    }else{
                        obtenerDatosOffline(offset,keyWord);
                    }
                    /* performSearch();*/
                    return true;
                }
                return false;
            }
        });

        textView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 3) {
                    if (autoCompleteLetter < charSequence.length()) {
                        handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                        handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                                AUTO_COMPLETE_DELAY);
                    }
                    autoCompleteLetter = charSequence.length();
                }
            }

            public void afterTextChanged(final Editable editable) {
            }

        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(textView.getText())) {
                        if(comprobarSalidaInternet()) {
                            llenarAutocomplete(textView.getText().toString());
                        }else{
                            llenarAutocompleteOffline(textView.getText().toString());
                        }
                    }
                }
                return false;
            }
        });

       // offset = 0;
       // obtenerDatos(tokenEjemplo, offset, keyWord);
        //  Toast.makeText(this, String.valueOf(Cart.countList()), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {

        back();
        //finish();
       // startActivity(getIntent());
        super.onBackPressed();

    }
    public void back(){
        Intent intent = new Intent(ListaProductos.this, IngresarPedido.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    private void obtenerDatos(String token, int offset, String keyWord) {

        final String[] id = new String[1];
        final String[] nombre = new String[1];
        final String[] presentacion = new String[1];
        final String[] precio = new String[1];

        JSONObject jsonObjectPeticionProducto = new JSONObject();
        try {
            jsonObjectPeticionProducto.put("metodo", "listaProductos");
            jsonObjectPeticionProducto.put("token", token);
            jsonObjectPeticionProducto.put("offset", offset);
            jsonObjectPeticionProducto.put("limit", 5);
            jsonObjectPeticionProducto.put("string", keyWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject[] jsonObjectRespuestaProductos = {new JSONObject()};
        final JSONArray[] jsonArrayDetalleProductos = {new JSONArray()};
        final JSONObject[] jsonObjectProducto = {new JSONObject()};


        String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";
        Log.d("Web S. ListaClientes:", url);
        ////Uso del web service para traer la ruta de clientes

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequestRequestRutaDeClientes = new JsonObjectRequest(
                url,
                jsonObjectPeticionProducto,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Data del WS", response.toString());
                        try {

                            ArrayList<Producto> list = new ArrayList<>();
                            jsonObjectRespuestaProductos[0] = response.getJSONObject("lista");
                            if(jsonObjectRespuestaProductos[0].length()>0) {
                                jsonArrayDetalleProductos[0] = jsonObjectRespuestaProductos[0].getJSONArray("listaProductos");

                                for (int i = 0; i < jsonArrayDetalleProductos[0].length(); i++) {
                                    jsonObjectProducto[0] = jsonArrayDetalleProductos[0].getJSONObject(i);
                                    id[0] = jsonObjectProducto[0].getString("id_producto");
                                    nombre[0] = jsonObjectProducto[0].getString("nombre_producto");
                                    presentacion[0] = jsonObjectProducto[0].getString("nombre_presentacion");
                                    precio[0] = jsonObjectProducto[0].getString("valor_unitario");


                                    Producto producto = new Producto();
                                    producto.setId_producto(Integer.parseInt(id[0]));
                                    producto.setProductoNombre(nombre[0]);
                                    producto.setProductoPresentacion(presentacion[0]);
                                    producto.setProductoPrecio(Double.parseDouble(precio[0]));
                                    list.add(producto);

                                }
                                productosAdapter.adicionarListaAmiibo(list);
                                blockSearch=false;
                               // bt_buscar_producto.setEnabled(true);
                                aptoParaCargar = true;
                            }
                            blockSearch=false;
                           // bt_buscar_producto.setEnabled(true);

                        } catch (JSONException e) {
                            blockSearch=false;
                          //  bt_buscar_producto.setEnabled(true);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                blockSearch=false;
               // bt_buscar_producto.setEnabled(true);
                Log.d("ERROR", error.toString());
            }
        }
        );
        requestQueue.add(jsonObjectRequestRequestRutaDeClientes);

        //return data_productos;


    }

    public void obtenerDatosOffline(int offset,String keyWord){
        ArrayList<Producto> list = new ArrayList<>();
        List<com.example.pasantias.appcamaleon.DataBase.Producto> productos;
        int limit=5;
        int offsetcan=limit*offset;
        if(!keyWord.equals(null) && !keyWord.equals(" ") && !keyWord.equals("")){
           productos=appDatabase.productoDao().getProductoOffsetString(String.valueOf(limit),String.valueOf(offsetcan),keyWord);
        }else{
            productos=appDatabase.productoDao().getProductoOffset(String.valueOf(limit),String.valueOf(offsetcan));
        }

        if(productos.size()>0) {

            for (int i = 0; i < productos.size(); i++) {
                Producto producto = new Producto();
                producto.setId_producto(productos.get(i).getIdProducto());
                producto.setProductoNombre(productos.get(i).getName());
                producto.setProductoPresentacion(productos.get(i).getPresentacion());
                producto.setProductoPrecio(productos.get(i).getpUnitario());
                list.add(producto);
            }
            productosAdapter.adicionarListaAmiibo(list);
            blockSearch=false;
            // bt_buscar_producto.setEnabled(true);
            aptoParaCargar = true;
        }else{
            Toast.makeText(getApplicationContext(),"No existen productos en la base",Toast.LENGTH_LONG).show();
        }
        blockSearch=false;

    }

    public void llenarAutocomplete(String query) {
        final String[] nombre = new String[1];
        JSONObject jsonObjectPeticionProducto = new JSONObject();
        try {
            jsonObjectPeticionProducto.put("metodo", "listaProductos");
            jsonObjectPeticionProducto.put("token", tokenEjemplo);
            jsonObjectPeticionProducto.put("offset", 1);
            jsonObjectPeticionProducto.put("limit", 5);
            jsonObjectPeticionProducto.put("string", query);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject[] jsonObjectRespuestaProductos = {new JSONObject()};
        final JSONArray[] jsonArrayDetalleProductos = {new JSONArray()};
        final JSONObject[] jsonObjectProducto = {new JSONObject()};


        String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";
        Log.d("Web S. ListaClientes:", url);
        ////Uso del web service para traer la ruta de clientes

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequestRequestRutaDeClientes = new JsonObjectRequest(
                url,
                jsonObjectPeticionProducto,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Data del WS", response.toString());
                        List<String> stringList = new ArrayList<>();
                        try {

                            jsonObjectRespuestaProductos[0] = response.getJSONObject("lista");
                            jsonArrayDetalleProductos[0] = jsonObjectRespuestaProductos[0].getJSONArray("listaProductos");

                            for (int i = 0; i < jsonArrayDetalleProductos[0].length(); i++) {
                                jsonObjectProducto[0] = jsonArrayDetalleProductos[0].getJSONObject(i);
                                nombre[0] = jsonObjectProducto[0].getString("nombre_producto");
                                stringList.add(nombre[0]);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        productoAutoCompleteAdapte.setData(stringList);
                        productoAutoCompleteAdapte.notifyDataSetChanged();
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
    public void llenarAutocompleteOffline(String query) {
        int limit=5;
        int offsetcan=1;
        List<com.example.pasantias.appcamaleon.DataBase.Producto> productos=appDatabase.productoDao().getProductoOffsetString(String.valueOf(limit),String.valueOf(offsetcan),query);
        List<String> stringList = new ArrayList<>();
        if(productos.size()>0) {
            for (int i = 0; i < productos.size(); i++) {
                stringList.add(productos.get(i).getName());
            }
        }
        productoAutoCompleteAdapte.setData(stringList);
        productoAutoCompleteAdapte.notifyDataSetChanged();
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

}
