package com.example.pasantias.appcamaleon;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pasantias.appcamaleon.Adapters.ProductoAutoCompleteAdapte;
import com.example.pasantias.appcamaleon.Adapters.ProductosAdapter;
import com.example.pasantias.appcamaleon.DataBase.AppDatabase;
import com.example.pasantias.appcamaleon.Pojos.Producto;
import com.example.pasantias.appcamaleon.R;
import com.example.pasantias.appcamaleon.Util.MyInterface;
import com.example.pasantias.appcamaleon.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HistorialFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "Historial";
    private FragmentActivity myContext;
    private OnListenerProductoItem mListener;
    // private TextView textView;


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
    private boolean blockSearch = false;
    // private Button bt_buscar_producto;
    private TextView textFecha;
    String currentDateandTime;
    private AutoCompleteTextView textView;

    private int modoTrabajo;


    public HistorialFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HistorialFragment newInstance() {
        HistorialFragment fragment = new HistorialFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_historial, container, false);
        // textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(ARG_SECTION_NUMBER);


        textFecha = (TextView) rootView.findViewById(R.id.textFecha);
        //bt_buscar_producto = (Button) findViewById(R.id.bt_buscar_producto);

        textView = rootView.findViewById(R.id.edit_buscar_producto);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewProductos);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDateandTime = sdf.format(new Date());
        textFecha.setText(currentDateandTime);

        appDatabase = AppDatabase.getAppDatabase(myContext.getApplication());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("datosAplicacion", MODE_PRIVATE);
        modoTrabajo = sharedPreferences.getInt("modoDeTrabajo", 0);


        productosAdapter = new ProductosAdapter(myContext);
        recyclerView.setAdapter(productosAdapter);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(myContext.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
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
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            // if (comprobarSalidaInternet()) {
                            aptoParaCargar = false;
                            offset += 1;
                            if (modoTrabajo == 1) {
                                if (comprobarSalidaInternet()) {
                                    obtenerDatos(tokenEjemplo, offset, keyWord);
                                }else{
                                    Toast.makeText(getContext(), "No hay conección a Internet" , Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                obtenerDatosOffline(offset, keyWord);
                            }


                            //}
                        }
                    }
                }
            }
        });


        productoAutoCompleteAdapte = new ProductoAutoCompleteAdapte(myContext, android.R.layout.simple_dropdown_item_1line);
        textView.setThreshold(3);
        textView.setAdapter(productoAutoCompleteAdapte);

        textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !blockSearch) {
                    blockSearch = true;
                    keyWord = String.valueOf(textView.getText());
                    aptoParaCargar = true;
                    offset = 0;
                    productosAdapter.limpiarLista();
                    if (modoTrabajo == 1) {
                        if (comprobarSalidaInternet()) {
                            obtenerDatos(tokenEjemplo, offset, keyWord);
                        }else{
                            Toast.makeText(getContext(), "No hay conección a Internet" , Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        obtenerDatosOffline(offset, keyWord);
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
                        if (modoTrabajo == 1) {
                            if (comprobarSalidaInternet()) {
                                llenarAutocomplete(textView.getText().toString());
                            }else{
                                Toast.makeText(getContext(), "No hay conección a Internet" , Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            llenarAutocompleteOffline(textView.getText().toString());
                        }



                    }
                }
                return false;
            }
        });


        /* recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(myContext,"click re",Toast.LENGTH_LONG).show();
                onFragmentIngresarPedido();
            }
        });


     /*   textFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(myContext,"click fecha",Toast.LENGTH_LONG).show();
               //7 onFragmentIngresarPedido();
            }
        });*/

        LinearLayout contenedorProducto = rootView.findViewById(R.id.contenedorProducto);


        return rootView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void buscarHistorialCliente(String idCliente) {
        //Toast.makeText(myContext, idCliente, Toast.LENGTH_LONG).show();
        if (idCliente.equals(null)) {
            //textView.setText(idCliente);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListenerProductoItem) {
            mListener = (OnListenerProductoItem) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        try {
            myContext = (FragmentActivity) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myContext = null;
        mListener = null;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onFragmentIngresarPedido() {
        if (mListener != null) {
            mListener.actualizarListaItem();
        } else {
            Toast.makeText(myContext, "No hay listener", Toast.LENGTH_LONG).show();
        }
    }


    public interface OnListenerProductoItem {
        // TODO: Update argument type and name
        void actualizarListaItem();
    }


    public void back() {
        Intent intent = new Intent(myContext, IngresarPedido.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //finish();
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

        RequestQueue requestQueue = Volley.newRequestQueue(myContext);
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
                            if (jsonObjectRespuestaProductos[0].length() > 0) {
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
                                blockSearch = false;
                                // bt_buscar_producto.setEnabled(true);
                                aptoParaCargar = true;
                            }
                            blockSearch = false;
                            // bt_buscar_producto.setEnabled(true);

                        } catch (JSONException e) {
                            blockSearch = false;
                            //  bt_buscar_producto.setEnabled(true);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                blockSearch = false;
                // bt_buscar_producto.setEnabled(true);
                Log.d("ERROR", error.toString());
            }
        }
        );
        requestQueue.add(jsonObjectRequestRequestRutaDeClientes);

        //return data_productos;


    }

    public void obtenerDatosOffline(int offset, String keyWord) {
        ArrayList<Producto> list = new ArrayList<>();
        List<com.example.pasantias.appcamaleon.DataBase.Producto> productos;
        int limit = 5;
        int offsetcan = limit * offset;
        if (!keyWord.equals(null) && !keyWord.equals(" ") && !keyWord.equals("")) {
            productos = appDatabase.productoDao().getProductoOffsetString(String.valueOf(limit), String.valueOf(offsetcan), keyWord);
        } else {
            productos = appDatabase.productoDao().getProductoOffset(String.valueOf(limit), String.valueOf(offsetcan));
        }

        if (productos.size() > 0) {

            for (int i = 0; i < productos.size(); i++) {
                Producto producto = new Producto();
                producto.setId_producto(productos.get(i).getIdProducto());
                producto.setProductoNombre(productos.get(i).getName());
                producto.setProductoPresentacion(productos.get(i).getPresentacion());
                producto.setProductoPrecio(productos.get(i).getpUnitario());
                list.add(producto);
            }
            productosAdapter.adicionarListaAmiibo(list);
            blockSearch = false;
            // bt_buscar_producto.setEnabled(true);
            aptoParaCargar = true;
        } else {
            Toast.makeText(myContext, "No existen productos en la base", Toast.LENGTH_LONG).show();
        }
        blockSearch = false;

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

        RequestQueue requestQueue = Volley.newRequestQueue(myContext);
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
        int limit = 5;
        int offsetcan = 1;
        List<com.example.pasantias.appcamaleon.DataBase.Producto> productos = appDatabase.productoDao().getProductoOffsetString(String.valueOf(limit), String.valueOf(offsetcan), query);
        List<String> stringList = new ArrayList<>();
        if (productos.size() > 0) {
            for (int i = 0; i < productos.size(); i++) {
                stringList.add(productos.get(i).getName());
            }
        }
        productoAutoCompleteAdapte.setData(stringList);
        productoAutoCompleteAdapte.notifyDataSetChanged();
    }

    public boolean comprobarSalidaInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
