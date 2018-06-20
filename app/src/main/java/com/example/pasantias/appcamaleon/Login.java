package com.example.pasantias.appcamaleon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pasantias.appcamaleon.Pojos.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    String servicioLogin = "iniciarSesion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);


        TextView tvUsuario;
        final EditText etDataUsuario;
        TextView tvPassword;
        final EditText etDataPassword;
        Button btLogin;

        tvUsuario =  findViewById(R.id.tv_usuario);
        etDataUsuario =  findViewById(R.id.et_dataUsuario);
        tvPassword =  findViewById(R.id.tv_password);
        etDataPassword =  findViewById(R.id.et_dataPassword);
        btLogin =  findViewById(R.id.bt_login);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Editable usuarioD = etDataUsuario.getText();
                Editable paswordD = etDataPassword.getText();

                Log.d("Usuario" , String.valueOf(usuarioD));
                Log.d("Password" , String.valueOf(paswordD));

                HashMap datauser = new HashMap();
                datauser.put("metodo", servicioLogin);
                datauser.put("username", usuarioD);
                datauser.put("password", paswordD);

                JSONObject jsonData = new JSONObject();
                try {
                    jsonData.put("metodo", servicioLogin);
                    jsonData.put("username", usuarioD);
                    jsonData.put("password", paswordD);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                consultarWSLogin(jsonData, usuarioD);

            }
        });




    }




    public void consultarWSLogin(JSONObject data, Editable user){


        final Usuario data_productos = new Usuario();

        Log.d("ZZZZZZZZZ: ", String.valueOf(user));

        //Consulta al web services
        String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";

        Log.d("Ruta al web service: ", url);
        ////Uso del web service para traer los productos

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Data del WS", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);


    }

    public void start(){


    }


}
