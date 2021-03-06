package com.example.pasantias.appcamaleon;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pasantias.appcamaleon.Adapters.ItemPedidoAdapter;
import com.example.pasantias.appcamaleon.DataBase.AppDatabase;
import com.example.pasantias.appcamaleon.DataBase.ClienteMin;
import com.example.pasantias.appcamaleon.DataBase.DetallePedido;
import com.example.pasantias.appcamaleon.DataBase.Pedido;
import com.example.pasantias.appcamaleon.R;
import com.example.pasantias.appcamaleon.Util.Cart;
import com.example.pasantias.appcamaleon.Util.GpsTracker;
import com.example.pasantias.appcamaleon.Util.ServicioEnvioPedidosPendientes;
import com.example.pasantias.appcamaleon.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class IngresarPedidoFragment extends Fragment {

    private OnListenerIngresarPedido mListener;
    private FragmentActivity myContext;



    private GpsTracker gpsTracker;

    public static AppDatabase appDatabase;
    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";

    private Button btIngProducto, bt_ingresar_pedido, bt_ingresar_pendiente;

    private RecyclerView recyclerView;
    private ItemPedidoAdapter itemPedidoAdapter;

    private TextView textFecha, textCliente, textAhorro, textSubtotal, textIva, textTotal;
    private String currentDateandTime;
    private Double latitud;
    private Double longitud;

    private int modoTrabajo;
    private Intent intent;

    public IngresarPedidoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static IngresarPedidoFragment newInstance() {
        IngresarPedidoFragment fragment = new IngresarPedidoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_ingresar_pedido, container, false);
        final FragmentManager fm = myContext.getFragmentManager();

        appDatabase = AppDatabase.getAppDatabase(myContext.getApplication());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("datosAplicacion", MODE_PRIVATE);
        modoTrabajo = sharedPreferences.getInt("modoDeTrabajo", 0);




        btIngProducto = (Button) rootView.findViewById(R.id.bt_ing_producto);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.tableLayoutProducto);

        //textFecha = (TextView) rootView.findViewById(R.id.textFecha);
        textCliente = (TextView) rootView.findViewById(R.id.textCliente);
        textAhorro = (TextView) rootView.findViewById(R.id.textAhorro);
        textSubtotal = (TextView) rootView.findViewById(R.id.textSubtotal);
        textIva = (TextView) rootView.findViewById(R.id.textIva);
        textTotal = (TextView) rootView.findViewById(R.id.textTotal);

        bt_ingresar_pedido = (Button) rootView.findViewById(R.id.bt_ingresar_pedido);
        bt_ingresar_pendiente = (Button) rootView.findViewById(R.id.bt_ingresar_pendiente);

        try {
            if (ContextCompat.checkSelfPermission(myContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(myContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDateandTime = sdf.format(new Date());

       // textFecha.setText(currentDateandTime);

    /*   textCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(appDatabase.clienteMinDao().countUsers()>0) {
                    Intent i = new Intent(IngresarPedido.this, ListaClientes.class);
                    startActivity(i);
                }else{
                    showMensaje();
                }
                //  finish();
            }
        });*/
        intent = new Intent(myContext, ServicioEnvioPedidosPendientes.class);
        bt_ingresar_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_ingresar_pedido.setEnabled(false);
                if (modoTrabajo == 1) {
                    if (comprobarSalidaInternet()) {
                        obtenerDatos(tokenEjemplo);
                        redirecBack();
                    }else{
                        guardarPedidoPendiente(Util.Constantes.ESTADO_EN_PROCESO_ENVIO);
                        myContext.startService(intent);
                        redirecBack();
                        Toast.makeText(getContext(), "No hay conección a Internet envio se realizará cuando se detecte la conección" , Toast.LENGTH_SHORT).show();
                    }
                }else{
                    guardarPedidoPendiente(Util.Constantes.ESTADO_ENVIADO_OFFLINE);
                    redirecBack();
                }


            }
        });

        bt_ingresar_pendiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_ingresar_pendiente.setEnabled(false);
                guardarPedidoPendiente(Util.Constantes.ESTADO_PENDIENTE);
                redirecBack();
            }
        });


       /* btIngProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngresarPedido.this, ListaProductos.class);
                startActivity(intent);
                // finish();
            }
        });*/

        itemPedidoAdapter = new ItemPedidoAdapter(myContext);
        recyclerView.setAdapter(itemPedidoAdapter);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(myContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        updateData();







        return rootView;
    }

    public void redirecBack(){
        Intent intent =new Intent(myContext,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("fragment", Util.Constantes.FRAGRUTACLIENTE);
        startActivity(intent);

        myContext.finish();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onFragmentHitorial(String idCliente) {
        if (mListener != null) {
            mListener.onBuscarHistorial(idCliente);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListenerIngresarPedido) {
            mListener = (OnListenerIngresarPedido) context;
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
        mListener = null;
    }

    public void actualizarLista() {
       // Toast.makeText(myContext, "Ingreso", Toast.LENGTH_SHORT).show();
        updateData();
    }

    public interface OnListenerIngresarPedido {
        // TODO: Update argument type and name
        void onBuscarHistorial(String idCliente);
    }







    public void updateData() {
        itemPedidoAdapter.limpiarLista();
        itemPedidoAdapter.adicionarListaItem(Cart.conItems());
        calculoProductos();
    }

    public void showMensaje() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(myContext);
        alertDialog.setTitle("Descargar clientes");
        alertDialog.setMessage("No hay descargado su lista de cliente de hoy, desea descargar los clientes");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(myContext, Offline.class);
                        startActivity(intent);
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
    }

    private void calculoProductos() {
        if (Cart.getCliente().getId_cliente() != 0) {
            textCliente.setText(Cart.getCliente().nombre);
        } else {
            textCliente.setText("Seleccionar Cliente");
        }
        textSubtotal.setText("$ " + Cart.df2.format(Cart.subTotal()));
        textIva.setText("$ " + Cart.df2.format(Cart.iva()));
        textTotal.setText("$ " + Cart.df2.format(Cart.total()));
    }

    private void obtenerDatos(String token) {
        if (Cart.getCliente().getId_cliente() != 0) {
            if (Cart.countList() != 0) {
            JSONObject jsonObjectGuardarPedido = new JSONObject();
            JSONArray jsonObjectParametros = new JSONArray();
            JSONObject jsonObjectItem;
            try {

                for (int i = 0; i < Cart.countList(); i++) {
                    jsonObjectItem = new JSONObject();
                    jsonObjectItem.put("id_producto", Cart.conItems().get(i).getProducto().getId_producto());
                    jsonObjectItem.put("cantidad", Cart.conItems().get(i).getCantidad());
                    String total = Cart.subTotalItem(Cart.conItems().get(i).getProducto().getProductoPrecio(), Cart.conItems().get(i).getCantidad());
                    jsonObjectItem.put("total", total);
                    jsonObjectItem.put("sub_total", total);
                    jsonObjectParametros.put(jsonObjectItem);
                }


                jsonObjectGuardarPedido.put("metodo", "crearPedido");
                jsonObjectGuardarPedido.put("token", token);
                jsonObjectGuardarPedido.put("idcliente", Cart.getCliente().getId_cliente());
                jsonObjectGuardarPedido.put("fecha_pedido", currentDateandTime);
                jsonObjectGuardarPedido.put("hora_pedido", "12:00");
                jsonObjectGuardarPedido.put("parametros", jsonObjectParametros);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(this, jsonObjectGuardarPedido.toString(), Toast.LENGTH_SHORT).show();
//        final JSONObject[] jsonObjectRespuestaProductos = {new JSONObject()};
//        final JSONArray[] jsonArrayDetalleProductos = {new JSONArray()};
//        final JSONObject[] jsonObjectProducto = {new JSONObject()};
//
//
            String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";
//        Log.d("Web S. ListaClientes:", url);
//        ////Uso del web service para traer la ruta de clientes
//
            RequestQueue requestQueue = Volley.newRequestQueue(myContext);
            JsonObjectRequest jsonObjectRequestRequestedido = new JsonObjectRequest(
                    url,
                    jsonObjectGuardarPedido,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Data del WS", response.toString());
                            try {

                                String aJsonString = response.getString("texto");
                                guardarPedidoPendiente(Util.Constantes.ESTADO_ENVIADO);
                                Cart.removeListCart();
                                updateData();
                                bt_ingresar_pedido.setEnabled(true);
                                //Toast.makeText(getApplicationContext(), aJsonString, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                bt_ingresar_pedido.setEnabled(true);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    bt_ingresar_pedido.setEnabled(true);
                    Log.d("ERROR", error.toString());
                }
            }
            );
            requestQueue.add(jsonObjectRequestRequestedido);
            } else {
                bt_ingresar_pedido.setEnabled(true);
                Toast.makeText(myContext, "No a seleccionado item", Toast.LENGTH_SHORT).show();
            }
        } else {
            bt_ingresar_pedido.setEnabled(true);
            Toast.makeText(myContext, "No a seleccionado el cliente", Toast.LENGTH_SHORT).show();
        }
        //return data_productos;


    }

    private void guardarPedidoPendiente(int estado) {
        // getLocation();
        if (Cart.getCliente().getId_cliente() != 0) {
            if (Cart.countList() != 0) {
                final Pedido pedido = new Pedido();
                // final DetallePedido detallePedido = new DetallePedido();

                pedido.setIdCliente(Cart.getCliente().id_cliente);
                pedido.setNombreCliente(Cart.getCliente().nombre);
                pedido.setFecha(currentDateandTime);
                // if (latitud != 0 && latitud != null) {
                pedido.setLattPedido(String.valueOf(latitud));
                //  }
                //if (longitud != 0 && longitud != null) {
                pedido.setLongPedido(String.valueOf(longitud));
                // }
                pedido.setEstadoPedido(estado);


                insertPedido(pedido);
                bt_ingresar_pendiente.setEnabled(false);
                String mensaje = "";
                mensaje=Util.Constantes.estadoPedido(estado);
                Toast.makeText(myContext, mensaje, Toast.LENGTH_SHORT).show();
            } else {
                bt_ingresar_pedido.setEnabled(true);
                Toast.makeText(myContext, "No a seleccionado item", Toast.LENGTH_SHORT).show();
            }

        } else {
            bt_ingresar_pedido.setEnabled(true);
            Toast.makeText(myContext, "No a seleccionado el cliente", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(getApplicationContext(), String.valueOf(baseRepository.countDetallePedido()), Toast.LENGTH_LONG).show();

    }

    public void getLocation() {
        gpsTracker = new GpsTracker(myContext);
        if (gpsTracker.canGetLocation()) {
            latitud = gpsTracker.getLatitude();
            longitud = gpsTracker.getLongitude();
            //Toast.makeText(getApplicationContext(), String.valueOf(longitud), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(myContext, "GPS no activo", Toast.LENGTH_LONG).show();
            // gpsTracker.showSettingsAlert();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void insertPedido(final Pedido pedido) {
        new AsyncTask<Pedido, Void, Long>() {
            @Override
            protected Long doInBackground(Pedido... pedidos) {
                Long id = appDatabase.pedidoDao().insertId(pedido);
                if (pedido.getEstadoPedido() == 1 || pedido.getEstadoPedido() == 2) {
                    ClienteMin clienteMin = appDatabase.clienteMinDao().getCliente(pedido.getIdCliente());
                    if(clienteMin!=null) {
                        clienteMin.setEstadoVisita(2);
                        appDatabase.clienteMinDao().update(clienteMin);
                    }
                }
                Log.d("Se gguardo", "XXXXXXX X");
                return id;
            }

            @Override
            protected void onPostExecute(Long result) {
                processValue(result);
            }

        }.execute(pedido);
    }

    void processValue(Long id) {
        if (id != null && id > 0) {
            final List<DetallePedido> detallePedidos = new ArrayList<DetallePedido>();
            DetallePedido detallePedido;
            for (int i = 0; i < Cart.countList(); i++) {
                detallePedido = new DetallePedido();
                detallePedido.setIdPedido(id);
                detallePedido.setIdProducto(Cart.conItems().get(i).getProducto().getId_producto());
                detallePedido.setProductoNombre(Cart.conItems().get(i).getProducto().getProductoNombre());
                detallePedido.setProductoPresentacion(Cart.conItems().get(i).getProducto().getProductoPresentacion());
                detallePedido.setProductoPrecio(Cart.conItems().get(i).getProducto().getProductoPrecio());
                detallePedido.setCantidad(Cart.conItems().get(i).getCantidad());
                detallePedidos.add(detallePedido);
            }
            Cart.removeListCart();
            updateData();
            insertDetallePedido(detallePedidos);
        } else {
            Toast.makeText(myContext, "No se realizo el registro del producto", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void insertDetallePedido(final List<DetallePedido> detallePedidos) {
        new AsyncTask<List<DetallePedido>, Void, Void>() {
            @Override
            protected Void doInBackground(List<DetallePedido>... lstDetallePedidos) {
                appDatabase.detallePedidoDao().insertAll(lstDetallePedidos[0]);
                Log.d("Cantidad", String.valueOf(appDatabase.detallePedidoDao().countDetallePedido()));
                return null;
            }
        }.execute(detallePedidos);
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
