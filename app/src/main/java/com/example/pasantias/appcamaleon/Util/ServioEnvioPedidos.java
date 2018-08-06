package com.example.pasantias.appcamaleon.Util;

import android.app.IntentService;
import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
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
import com.example.pasantias.appcamaleon.DataBase.Producto;
import com.example.pasantias.appcamaleon.IngresarPedido;
import com.example.pasantias.appcamaleon.ListaPedidos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServioEnvioPedidos extends Service {
    public static final String ACTION_PROGRESO =
            "net.sgoliver.intent.action.PROGRESO";
    public static final String ACTION_FIN =
            "net.sgoliver.intent.action.FIN";


    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";
    private String currentDateandTime;
    public static AppDatabase appDatabase;
    public List<Pedido> pedidosPendientes;
    public List<DetallePedido> detallePedidos;

    private Looper mServiceLooper;
    // private ServiceHandler mServiceHandler;
    private int ID_NOTIFICATION = 1220;
    public int counter = 0;
    Context context;

    public ServioEnvioPedidos(Context applicationContext) {
        super();
        context = applicationContext;
        Log.i("HERE", "here service created!");
    }

    public ServioEnvioPedidos() {
    }

    @Override
    public void onCreate() {
        //super.onCreate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDateandTime = sdf.format(new Date());
        appDatabase = AppDatabase.getAppDatabase(getApplication());
        pedidosPendientes = new ArrayList<>();
        detallePedidos = new ArrayList<>();

       /* HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);*/

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        // Message msg = mServiceHandler.obtainMessage();
        //msg.arg1 = ID_NOTIFICATION;
        //mServiceHandler.sendMessage(msg);

        sendData();
        return START_STICKY;
        // return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("EXIT", "ondestroy!");

        /*Intent broadcastIntent = new Intent("ac.in.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
        stoptimertask();*/
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /*    private final class ServiceHandler extends Handler {
            public ServiceHandler(Looper looper) {
                super(looper);
            }

            @Override
            public void handleMessage(Message msg) {
                // super.handleMessage(msg);



                *//*FileUploadNotification fileUploadNotification = new FileUploadNotification(getApplicationContext());
            try {

                for (int i = 0; i < 10; i++) {
                    fileUploadNotification.updateNotificatio(String.valueOf(i * 10), "Pedidos", "Pruebas");
                    Thread.sleep(1000);
                }
                fileUploadNotification.deleteNotification();
            } catch (InterruptedException e) {
                fileUploadNotification.failUploadNotification();
                e.printStackTrace();
            }*//*
            // stopSelf(msg.arg1);
            new sendData().execute();

        }

        public class sendData extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                FileUploadNotification fileUploadNotification = new FileUploadNotification(getApplicationContext());
                pedidosPendientes = appDatabase.pedidoDao().findByIdEstado("2");
                int sizeList = pedidosPendientes.size();
                final boolean[] error = {false};
                if (sizeList != 0) {
                    for (int j = 0; j < sizeList; j++) {

                        detallePedidos = appDatabase.detallePedidoDao().findByDetallePedido(String.valueOf(pedidosPendientes.get(j).getIdPedido()));
                        try {
                            JSONObject jsonObjectGuardarPedido = new JSONObject();
                            JSONArray jsonObjectParametros = new JSONArray();
                            JSONObject jsonObjectItem;
                            try {

                                for (int i = 0; i < Cart.countList(); i++) {
                                    jsonObjectItem = new JSONObject();
                                    jsonObjectItem.put("id_producto", detallePedidos.get(i).getIdPedido());
                                    jsonObjectItem.put("cantidad", detallePedidos.get(i).getCantidad());
                                    String total = Cart.subTotalItem(detallePedidos.get(i).getProductoPrecio(), detallePedidos.get(i).getCantidad());
                                    jsonObjectItem.put("total", total);
                                    jsonObjectItem.put("sub_total", total);
                                    jsonObjectParametros.put(jsonObjectItem);
                                }


                                jsonObjectGuardarPedido.put("metodo", "crearPedido");
                                jsonObjectGuardarPedido.put("token", tokenEjemplo);
                                jsonObjectGuardarPedido.put("idcliente", pedidosPendientes.get(j).getIdCliente());
                                jsonObjectGuardarPedido.put("fecha_pedido", currentDateandTime);
                                jsonObjectGuardarPedido.put("hora_pedido", "12:00");
                                jsonObjectGuardarPedido.put("parametros", jsonObjectParametros);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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
                                                String aJsonString = response.getString("texto");
                                                if (aJsonString.equals("ok")) {
                                                    error[0] = true;
                                                }
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
                            requestQueue.add(jsonObjectRequestRequestedido);
                            if (!error[0]) {
                                fileUploadNotification.alertNotification("Pedidos Pendientes", "Sincronización de pedido exitosa");
                            } else {
                                fileUploadNotification.alertNotification("Pedidos Pendientes", "Error al sincronizar el pedido");
                            }

                            //fileUploadNotification.deleteNotification();
                        } catch (Exception e) {
                            fileUploadNotification.failUploadNotification();
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"No hay pedidos a sincronizar",Toast.LENGTH_LONG).show();
                }
                stopSelf();
                return null;
            }
        }
    }*/
    public void sendData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {


                FileUploadNotification fileUploadNotification = new FileUploadNotification(getApplicationContext());
                pedidosPendientes = appDatabase.pedidoDao().findByIdEstado("2");
                int sizeList = pedidosPendientes.size();
                final boolean[] error = {false};
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
                                                if (aJsonString.equals("ok")) {
                                                    // Pedido pedido = appDatabase.pedidoDao().findByIdPedido(aJsonId);
                                                    pedido.setEstadoPedido(1);
                                                    appDatabase.pedidoDao().update(pedido);
                                                } else {
                                                    error[0] = true;
                                                }
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
                            requestQueue.add(jsonObjectRequestRequestedido);


                            //fileUploadNotification.deleteNotification();
                        } catch (Exception e) {
                            //fileUploadNotification.failUploadNotification();
                            e.printStackTrace();
                        }
                        int per = (j == sizeList ? (j / sizeList * 100) : 100);

                        //Comunicamos el progreso
                        Intent bcIntent = new Intent();
                        bcIntent.setAction(ACTION_PROGRESO);
                        bcIntent.putExtra("progreso", per);
                        sendBroadcast(bcIntent);

                        fileUploadNotification.updateNotificatio(String.valueOf(per), "Subiendo Pedidos Pendientes", "Sincronizando");
                    }
                    fileUploadNotification.deleteNotification();
                    if (error[0]) {
                        fileUploadNotification.alertNotification("Error al sincronizar el pedido", getApplicationContext());
                    } else {
                        fileUploadNotification.alertNotification("Sincronización de pedido exitosa", getApplicationContext());
                    }
                }
                Intent bcIntent = new Intent();
                bcIntent.setAction(ACTION_FIN);
                sendBroadcast(bcIntent);
                stopSelf();
                return null;
            }
        }.execute();
    }
/*    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (counter++));
            }
        };
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }*/


}





