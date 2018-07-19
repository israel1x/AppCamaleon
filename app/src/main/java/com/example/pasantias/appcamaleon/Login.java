package com.example.pasantias.appcamaleon;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pasantias.appcamaleon.DataBase.AppDatabase;
import com.example.pasantias.appcamaleon.DataBase.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class Login extends AppCompatActivity {
    String servicioLogin = "iniciarSesion";
    public static AppDatabase appDatabase;
    Usuario usuarioX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        appDatabase = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"clientesdb").allowMainThreadQueries().build();

        final EditText etDataUsuario;
        final EditText etDataPassword;
        Button btLogin;

        etDataUsuario =  findViewById(R.id.et_dataUsuario);
        etDataPassword =  findViewById(R.id.et_dataPassword);
        btLogin =  findViewById(R.id.bt_login);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuarioD = etDataUsuario.getText().toString().toLowerCase();
                String paswordD = etDataPassword.getText().toString();

                Log.d("Usuario" , String.valueOf(usuarioD));
                Log.d("Password" , String.valueOf(paswordD));

               if ( usuarioD.isEmpty() || paswordD.isEmpty()) {
                   Toast.makeText(Login.this, "Ingrese su usuario y contraseña", Toast.LENGTH_SHORT).show();
               } else {
                   //Comprobamos si hay conexion a internet
                   if ( comprobarSalidaInternet() ) {

                       JSONObject jsonData = new JSONObject();
                       try {
                           jsonData.put("metodo", servicioLogin);
                           jsonData.put("username", usuarioD);
                           jsonData.put("password", paswordD);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                       consultarWSLogin(jsonData, usuarioD);
                   } else {
                    // si no hay salida a internet, consulto  a la db local
                       Toast.makeText(Login.this, "No hay salida a internet, login local", Toast.LENGTH_LONG).show();
                       getDataUser(1, usuarioD, paswordD);
                   }
               }
            }
        });
    }

    //Metodo para conectar al web services
    public void consultarWSLogin(JSONObject data, String user){

        final Usuario usuarioTemp = new Usuario();
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
                        try {
                            if ( response.getString("resultado").equals("ok") ) {
                                Toast.makeText(Login.this, "Iniciando sesion....", Toast.LENGTH_LONG).show();
                                //lanzo la segunda actividad , que es el menu principal
                                JSONObject jsonObject = response.getJSONObject("lista");
                                Log.d("data de la LISTA", jsonObject.toString());
                                usuarioTemp.setToken(jsonObject.getString("token"));
                                usuarioTemp.setUser(jsonObject.getString("usuario"));
                                usuarioTemp.setPassword("1");
                                insertUser(usuarioTemp);

                                Intent i = new Intent(Login.this, MainActivity.class);
                                startActivity(i);
                            } else if ( response.getString("resultado").equals("error") ) {
                                Toast.makeText(Login.this, "Usuario o Contraseña incorrectos", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Data del WS", response.toString());
                        ///startMenuPrincipal(getApplicationContext());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    //Metodo para lanzar otra actividad
    public void startMenuPrincipal(Context context){
        Intent starter = new Intent(context, ListaClientes.class);
        //starter.putExtra(AppConstants.BUNDLE, bundle);
        context.startActivity(starter);
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

    @SuppressLint("StaticFieldLeak")
    private void insertUser(final Usuario usuario) {
        new AsyncTask<Usuario, Void, Void>() {
            @Override
            protected Void doInBackground(Usuario... usuarios) {
                appDatabase.usuarioDao().insert(usuario);
                Log.d("Se gguardo", "XXXXXXX X");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute(usuario);
    }

    @SuppressLint("StaticFieldLeak")
    private void getDataUser(final int id, final String user, final String pass) {
        new AsyncTask<Integer, Void , Usuario>() {
            @Override
            protected Usuario doInBackground(Integer... integers) {
                return appDatabase.usuarioDao().findByIdUser(id);
            }

            @Override
            protected void onPostExecute(Usuario usuario) {
                super.onPostExecute(usuario);
                usuario.getPassword();
                usuario.getUser();

                Log.d("De la base", usuario.getUser());
                if (usuario.getUser().equals(user) && usuario.getPassword().equals(pass)) {
                    Toast.makeText(Login.this, "Inicio local exitoso", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(Login.this, "Datos incorrectos", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(id);
    }

}
