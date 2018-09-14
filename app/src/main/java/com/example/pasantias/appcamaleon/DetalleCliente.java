package com.example.pasantias.appcamaleon;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pasantias.appcamaleon.DataBase.AppDatabase;
import com.example.pasantias.appcamaleon.DataBase.ClienteMin;
import com.example.pasantias.appcamaleon.Pojos.Cliente;
import com.example.pasantias.appcamaleon.Util.AlertDialogRadio;
import com.example.pasantias.appcamaleon.Util.Cart;
import com.example.pasantias.appcamaleon.Util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetalleCliente extends AppCompatActivity implements View.OnClickListener,AlertDialogRadio.AlertPositiveListener {

    private TextView textInfoNombre, textInfoNegocio, textInfoDireccion, textInfoEstado, textAccionComentar, textAccionPedido;
    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";

    AlertDialog alertDialog1;
    String[] values = Util.Constantes.CVISITA;
    private String currentDateandTime;
    public static AppDatabase appDatabase;

    int position = 0;
    private int modoTrabajo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cliente);

        appDatabase = AppDatabase.getAppDatabase(getApplication());
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("datosAplicacion", MODE_PRIVATE);
        modoTrabajo = sharedPreferences.getInt("modoDeTrabajo", 0);

        textInfoNombre = (TextView) findViewById(R.id.textInfoNombre);
        textInfoNegocio = (TextView) findViewById(R.id.textInfoNegocio);
        textInfoDireccion = (TextView) findViewById(R.id.textInfoDireccion);
        textInfoEstado = (TextView) findViewById(R.id.textInfoEstado);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDateandTime = sdf.format(new Date());

        textAccionComentar = (TextView) findViewById(R.id.textAccionComentar);
        textAccionComentar.setOnClickListener(this);


        textAccionPedido = (TextView) findViewById(R.id.textAccionPedido);
        textAccionPedido.setOnClickListener(this);
        if (Cart.getCliente().getId_cliente() != 0) {
            textInfoNombre.setText(Cart.getCliente().getNombre());
            textInfoNegocio.setText(Cart.getCliente().getNombre());
            textInfoDireccion.setText(Cart.getCliente().getDireccion());
            textInfoEstado.setText(Util.Constantes.estadoVisita(Cart.getCliente().getEstado()));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textAccionPedido:
                Intent intent = new Intent(this, IngresarPedido.class);
                startActivity(intent);
                break;
            case R.id.textAccionComentar:
                commentarVisita();
                break;
        }
    }

    public void commentarVisita(){
        FragmentManager manager = getFragmentManager();
        AlertDialogRadio alert = new AlertDialogRadio(values);
        Bundle b  = new Bundle();
        b.putInt("position", 0);
        alert.setArguments(b);
        alert.show(manager, "alert_dialog_radio");
    }


    @Override
    public void onPositiveClick(int position) {
        this.position = position;
        if(modoTrabajo==1) {
            if (comprobarSalidaInternet()) {
                sendComentarioVisita(tokenEjemplo, position + 3);
                sendBack();
            }else{
                Toast.makeText(getApplicationContext(), "No hay conección a Internet" , Toast.LENGTH_SHORT).show();
            }

        }else{
            if (Cart.getCliente().getId_cliente() != 0) {
                Cart.getCliente().setEstado(position + 3);
                insertPedido(Cart.getCliente());
                sendBack();
            }
        }

        //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Toast.makeText(getApplicationContext(), "Your Choice : " + values[this.position], Toast.LENGTH_SHORT).show();
    }

    public void sendBack(){
        Intent intent =new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("fragment",Util.Constantes.FRAGRUTACLIENTE);
        startActivity(intent);

        finish();
    }


    private void sendComentarioVisita(String token,int estado) {
        if (Cart.getCliente().getId_cliente() != 0) {
                JSONObject jsonObjectGuardarPedido = new JSONObject();

                try {
                    jsonObjectGuardarPedido.put("metodo", "comentarVisita");
                    jsonObjectGuardarPedido.put("token", token);
                    jsonObjectGuardarPedido.put("idcliente", Cart.getCliente().getId_cliente());
                    jsonObjectGuardarPedido.put("fecha_pedido", currentDateandTime);
                    jsonObjectGuardarPedido.put("estado", String.valueOf(estado));
                    jsonObjectGuardarPedido.put("hora_pedido", "12:00");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";

                RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                                    //Toast.makeText(getApplicationContext(), aJsonString, Toast.LENGTH_SHORT).show();
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

        } else {
            Toast.makeText(this, "No a seleccionado el cliente", Toast.LENGTH_SHORT).show();
        }
        //return data_productos;


    }

    @SuppressLint("StaticFieldLeak")
    private void insertPedido(final Cliente cliente) {
        new AsyncTask<Cliente, Void, Void>() {
            @Override
            protected Void doInBackground(Cliente... clientes) {
                ClienteMin clienteMin = appDatabase.clienteMinDao().getCliente(cliente.getId_cliente());
                if(clienteMin!=null) {
                    clienteMin.setEstadoVisita(cliente.estado);
                    appDatabase.clienteMinDao().update(clienteMin);
                }
                return null;
            }

        }.execute(cliente);
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
