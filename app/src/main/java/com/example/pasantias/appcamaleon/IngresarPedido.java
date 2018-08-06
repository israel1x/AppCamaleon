package com.example.pasantias.appcamaleon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasantias.appcamaleon.Adapters.TableDynamic;
import com.example.pasantias.appcamaleon.DataBase.AppDatabase;
import com.example.pasantias.appcamaleon.DataBase.DetallePedido;
import com.example.pasantias.appcamaleon.DataBase.Pedido;
import com.example.pasantias.appcamaleon.Pojos.Item;
import com.example.pasantias.appcamaleon.Pojos.Producto;
import com.example.pasantias.appcamaleon.Util.Cart;

import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;

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
import com.example.pasantias.appcamaleon.Util.GpsTracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IngresarPedido extends AppCompatActivity {

    private GpsTracker gpsTracker;

    public static AppDatabase appDatabase;
    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";

    private Button btIngProducto, bt_ingresar_pedido, bt_ingresar_pendiente;
    private TableLayout tableLayoutProducto;
    private TableDynamic tableDynamic;

    private TextView textFecha, textCliente, textAhorro, textSubtotal, textIva, textTotal;
    private String currentDateandTime;
    private Double latitud;
    private Double longitud;

    private String[] header = new String[]{
            "NOMBRE", "PRESENTACION", "PRECIO", "CANTIDAD", "SUBTOTAL"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_pedido);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        appDatabase = AppDatabase.getAppDatabase(getApplication());

        btIngProducto = (Button) findViewById(R.id.bt_ing_producto);
        tableLayoutProducto = (TableLayout) findViewById(R.id.tableLayoutProducto);

        textFecha = (TextView) findViewById(R.id.textFecha);
        textCliente = (TextView) findViewById(R.id.textCliente);
        textAhorro = (TextView) findViewById(R.id.textAhorro);
        textSubtotal = (TextView) findViewById(R.id.textSubtotal);
        textIva = (TextView) findViewById(R.id.textIva);
        textTotal = (TextView) findViewById(R.id.textTotal);

        bt_ingresar_pedido = (Button) findViewById(R.id.bt_ingresar_pedido);
        bt_ingresar_pendiente = (Button) findViewById(R.id.bt_ingresar_pendiente);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDateandTime = sdf.format(new Date());

        textFecha.setText(currentDateandTime);
        if (Cart.getCliente().getId_cliente() != 0) {
            textCliente.setText(Cart.getCliente().nombre);
        } else {
            textCliente.setText("Seleccionar Cliente");
        }
        textCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(IngresarPedido.this, ListaClientes.class);
                startActivity(i);
              //  finish();
            }
        });
        bt_ingresar_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_ingresar_pedido.setEnabled(false);
                obtenerDatos(tokenEjemplo);
            }
        });

        bt_ingresar_pendiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_ingresar_pendiente.setEnabled(false);
                guardarPedidoPendiente();
            }
        });


        tableDynamic = new TableDynamic(tableLayoutProducto, getApplicationContext());
        tableDynamic.addHeader(header);


        btIngProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngresarPedido.this, ListaProductos.class);
                startActivity(intent);
                // finish();
            }
        });

        addItem();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Pedido");
        alertDialog.setMessage("Esta seguro que desea salir, se perderán los datos del pedido");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                         Cart.removeListCart();
                        //finish();
                         IngresarPedido.this.finish();
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

    public void showMensaje(){
        /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Pedido");
        alertDialog.setMessage("Esta seguro que desea salir, se pederan los datos de pedido");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       // Cart.removeListCart();
                        finish();
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
        alert.show();*/
    }

    private void calculoProductos() {
        textSubtotal.setText("$ " + Cart.df2.format(Cart.subTotal()));
        textIva.setText("$ " + Cart.df2.format(Cart.iva()));
        textTotal.setText("$ " + Cart.df2.format(Cart.total()));
    }

    private void addItem() {
        /*Intent intent = getIntent();
        if (intent != null) {
            Producto producto = (Producto) intent.getSerializableExtra("producto");
            Integer cantidad = intent.getIntExtra("cantidad", 0);
            if (cantidad != 0 && Cart.getInsertarData()) {
                Cart.insert(new Item(producto, cantidad));
                Cart.setInsertarData(false);
                //  Toast.makeText(this, String.valueOf(Cart.countList()), Toast.LENGTH_LONG).show();
            }
        }*/
        if (Cart.countList() != 0) {
            //List<Item> productos =Cart.conItems();
            tableDynamic.addData(Cart.conItems());
        }
        calculoProductos();
    }

    public static void restartActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
        } else {
            activity.finish();
            activity.startActivity(activity.getIntent());
        }
    }

    private void obtenerDatos(String token) {
        if (Cart.getCliente().getId_cliente() != 0) {
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
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjectRequestRequestedido = new JsonObjectRequest(
                    url,
                    jsonObjectGuardarPedido,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Data del WS", response.toString());
                            try {

                                String aJsonString = response.getString("texto");
                                Cart.removeListCart();
                                restartActivity(IngresarPedido.this);
                                bt_ingresar_pedido.setEnabled(true);
                                Toast.makeText(getApplicationContext(), aJsonString, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "No a seleccionado el cliente", Toast.LENGTH_SHORT).show();
        }
        //return data_productos;


    }

    private void guardarPedidoPendiente() {
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
                pedido.setEstadoPedido(2);


                insertPedido(pedido);
                bt_ingresar_pendiente.setEnabled(false);
                Toast.makeText(this, "Guardado como pendiente", Toast.LENGTH_SHORT).show();
            } else {
                bt_ingresar_pedido.setEnabled(true);
                Toast.makeText(this, "No a seleccionado item", Toast.LENGTH_SHORT).show();
            }

        } else {
            bt_ingresar_pedido.setEnabled(true);
            Toast.makeText(this, "No a seleccionado el cliente", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(getApplicationContext(), String.valueOf(baseRepository.countDetallePedido()), Toast.LENGTH_LONG).show();

    }

    public void getLocation() {
        gpsTracker = new GpsTracker(getApplicationContext());
        if (gpsTracker.canGetLocation()) {
            latitud = gpsTracker.getLatitude();
            longitud = gpsTracker.getLongitude();
            //Toast.makeText(getApplicationContext(), String.valueOf(longitud), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "GPS no activo", Toast.LENGTH_LONG).show();
            // gpsTracker.showSettingsAlert();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void insertPedido(final Pedido pedido) {
        new AsyncTask<Pedido, Void, Long>() {
            @Override
            protected Long doInBackground(Pedido... pedidos) {
                Long id = appDatabase.pedidoDao().insertId(pedido);
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
            restartActivity(IngresarPedido.this);
            insertDetallePedido(detallePedidos);
        } else {
            Toast.makeText(getApplicationContext(), "No se realizo el registro del producto", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void insertDetallePedido(final List<DetallePedido> detallePedidos) {
        new AsyncTask<List<DetallePedido>, Void, Void>() {
            @Override
            protected Void doInBackground(List<DetallePedido>... lstDetallePedidos) {
                appDatabase.detallePedidoDao().insertAll(lstDetallePedidos[0]);
                Log.d("Cantidad de producto ingresado a la base", String.valueOf(appDatabase.detallePedidoDao().countDetallePedido()));
                return null;
            }
        }.execute(detallePedidos);
    }

}
