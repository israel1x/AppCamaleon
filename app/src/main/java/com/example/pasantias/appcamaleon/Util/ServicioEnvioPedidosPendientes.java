package com.example.pasantias.appcamaleon.Util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pasantias.appcamaleon.DataBase.AppDatabase;
import com.example.pasantias.appcamaleon.DataBase.DetallePedido;
import com.example.pasantias.appcamaleon.DataBase.Pedido;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ServicioEnvioPedidosPendientes extends Service {
    private static final String TAG = "MyService";
    private boolean isRunning = false;
    private Looper looper;
    private MyServiceHandler myServiceHandler;

    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";
    private String currentDateandTime;
    public static AppDatabase appDatabase;
    public List<Pedido> pedidosPendientes;
    public List<DetallePedido> detallePedidos;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        HandlerThread handlerthread = new HandlerThread("MyThread", 1);
        handlerthread.start();
        appDatabase = AppDatabase.getAppDatabase(getApplication());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDateandTime = sdf.format(new Date());
        looper = handlerthread.getLooper();
        myServiceHandler = new MyServiceHandler(looper);
        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = myServiceHandler.obtainMessage();
        msg.arg1 = startId;
        myServiceHandler.sendMessage(msg);
        Toast.makeText(this, "MyService Started.", Toast.LENGTH_SHORT).show();
        //If service is killed while starting, it restarts.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
    }



    public void sendData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                pedidosPendientes = appDatabase.pedidoDao().findByIdEstado(String.valueOf(Util.Constantes.ESTADO_EN_PROCESO_ENVIO));
                int sizeList = pedidosPendientes.size();
                if (sizeList != 0) {
                    for (int j = 0; j < sizeList; j++) {

                        detallePedidos = appDatabase.detallePedidoDao().findByDetallePedido(String.valueOf(pedidosPendientes.get(j).getIdPedido()));
                        try {
                            JSONObject jsonObjectGuardarPedido = new JSONObject();
                            JSONArray jsonObjectParametros = new JSONArray();
                            JSONObject jsonObjectItem;
                            try {

                                for (int i = 0; i < detallePedidos.size(); i++) {
                                    jsonObjectItem = new JSONObject();
                                    jsonObjectItem.put("id_producto", detallePedidos.get(i).getIdPedido());
                                    jsonObjectItem.put("cantidad", detallePedidos.get(i).getCantidad());
                                    String total = Cart.subTotalItem(detallePedidos.get(i).getProductoPrecio(), detallePedidos.get(i).getCantidad());
                                    jsonObjectItem.put("total", total);
                                    jsonObjectItem.put("sub_total", total);
                                    jsonObjectParametros.put(jsonObjectItem);
                                }


                                jsonObjectGuardarPedido.put("metodo", "crearPedido");
                                jsonObjectGuardarPedido.put("idBaseMovil", pedidosPendientes.get(j).getIdPedido());
                                jsonObjectGuardarPedido.put("token", tokenEjemplo);
                                jsonObjectGuardarPedido.put("idcliente", pedidosPendientes.get(j).getIdCliente());
                                jsonObjectGuardarPedido.put("fecha_pedido", currentDateandTime);
                                jsonObjectGuardarPedido.put("hora_pedido", "12:00");
                                jsonObjectGuardarPedido.put("parametros", jsonObjectParametros);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final Pedido pedido = pedidosPendientes.get(j);
                            String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            JsonObjectRequest jsonObjectRequestRequestedido = new JsonObjectRequest(
                                    url,
                                    jsonObjectGuardarPedido,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("Data del WS", response.toString());
                                            try {
                                                String aJsonString = response.getString("resultado");
                                                String aJsonId = response.getString("lista");
                                                Log.d(TAG, aJsonString);
                                                if (aJsonString.equals("ok")) {
                                                    // Pedido pedido = appDatabase.pedidoDao().findByIdPedido(aJsonId);
                                                    pedido.setEstadoPedido(1);
                                                    appDatabase.pedidoDao().update(pedido);
                                                    //Log.d(TAG, "ENVIADO");
                                                } else {
                                                   // Log.d(TAG, "NO ENVIADO");
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG, error.toString());
                                }
                            }
                            );
                            requestQueue.add(jsonObjectRequestRequestedido);


                            //fileUploadNotification.deleteNotification();
                        } catch (Exception e) {
                            //fileUploadNotification.failUploadNotification();
                            e.printStackTrace();
                        }
                        int per = (j == sizeList ? (j / sizeList * 100) : 100);


                    }
                }


                return null;
            }
        }.execute();
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


    private final class MyServiceHandler extends Handler {
        public MyServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            synchronized (this) {
             /*   for (int i = 0; i < 5; i++) {

                    try {
                        Log.i(TAG, "MyService running...");
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                    if(!isRunning){
                        break;
                    }
                }*/
                int sizeList = 0;
                do {
                    try {
                        pedidosPendientes = appDatabase.pedidoDao().findByIdEstado(String.valueOf(Util.Constantes.ESTADO_EN_PROCESO_ENVIO));
                        sizeList = pedidosPendientes.size();
                        if(sizeList!=0){
                            if(comprobarSalidaInternet()) {
                                sendData();
                            }else{
                                Log.i(TAG, "Sin internet");
                            }
                        }
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                    if(!isRunning){
                        break;
                    }
                } while (sizeList != 0);
                stopSelf();
            }

        }
    }

}
