package com.example.pasantias.appcamaleon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pasantias.appcamaleon.DataBase.AppDatabase;
import com.example.pasantias.appcamaleon.DataBase.ClienteMin;
import com.example.pasantias.appcamaleon.Pojos.Cliente;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RutaDeClientes extends Fragment implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private GoogleMap mMap;
    List<ClienteMin> clienteMins = new ArrayList<>();
    List<LatLng> direccionesClientes = new ArrayList<>();
    public static AppDatabase appDatabase;
    public static int modoTrabajo;

    private static final String LOG_TAG = "CamaleonApp";
    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_ruta_de_clientes,container,false);

        appDatabase = AppDatabase.getAppDatabase(getActivity());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("datosAplicacion", MODE_PRIVATE);
        modoTrabajo = sharedPreferences.getInt("modoDeTrabajo", 0);
        Log.d("MODO TRABAJO" , String.valueOf(modoTrabajo));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(12);

        enableMyLocation();
        Log.d("MODO TRABAJO" , String.valueOf(modoTrabajo));
        if (modoTrabajo == 1) {
            if (comprobarSalidaInternet()) {
                traerRutaDeClientes(clienteMins,tokenEjemplo);
            } else {
                Toast.makeText(getContext(), "Cargando clientes guardados" , Toast.LENGTH_SHORT).show();
                cargarRutaDeClientesDB(clienteMins,tokenEjemplo);
            }
        } else {
            Toast.makeText(getContext(), "Cargando clientes guardados" , Toast.LENGTH_SHORT).show();
            cargarRutaDeClientesDB(clienteMins,tokenEjemplo);
        }

    }



    void createMarks(List<ClienteMin> clienteMin) {
        LatLng guayaquil = new LatLng(-2.16753, -79.89369);
        mMap.addMarker(new MarkerOptions().position(guayaquil).title("Marker in Sydney"));
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @SuppressLint("LongLogTag")
    public void traerRutaDeClientes(final List<ClienteMin> clienteMins, String token) {
        final ClienteMin clienteMin = new ClienteMin();
        final String[] id = new String[1];
        final String[] nombre = new String[1];
        final String[] latitud = new String[1];
        final String[] longitud = new String[1];
        final String[] direccion = new String[1];
        final String[] telefono = new String[1];
        final int[] estadoVisita = {0};

        final LatLng guayaquil = new LatLng(-2.16753, -79.89369);
        mMap.addMarker(new MarkerOptions().position(guayaquil).title("Marker in Innova"));

        final MarkerOptions marcaClienteVisitado = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.visitado_150)).anchor(0.0f,1.0f);
        final MarkerOptions marcaCliente = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.visita_150)).anchor(0.0f,1.0f);


        JSONObject jsonObjectCliente = new JSONObject();
        try {
            jsonObjectCliente.put("metodo","listaClientes");
            jsonObjectCliente.put("token",token);
        }catch (JSONException e){
            e.printStackTrace();
        }

        final JSONObject[] jsonObjectRutas = {new JSONObject()};
        final JSONArray[] jsonArrayDetalleRutas = {new JSONArray()};

        final JSONObject[] jsonObjectUno = {new JSONObject()};
        String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";
        Log.d("Web service listaClientes:", url);
        ////Uso del web service para traer la ruta de clientes
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequestRequestRutaDeClientes = new JsonObjectRequest(
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
                            Log.d("RUTAS :", String.valueOf(jsonArrayDetalleRutas[0].length()));
                            for (int i = 0; i < jsonArrayDetalleRutas[0].length(); i++) {
                                jsonObjectUno[0] = jsonArrayDetalleRutas[0].getJSONObject(i);
                                id[0] = jsonObjectUno[0].getString("id_cliente");
                                latitud[0] = jsonObjectUno[0].getString("latitud");
                                longitud[0] = jsonObjectUno[0].getString("longitud");
                                nombre[0] = jsonObjectUno[0].getString("nombre");
                                direccion[0] = jsonObjectUno[0].getString("direccion");
                                telefono[0] = jsonObjectUno[0].getString("telefono");
                                estadoVisita[0] = jsonObjectUno[0].getInt("id_estado");
                                clienteMin.setIdCliente(id[0]);
                                clienteMin.setLattCliente(latitud[0]);
                                clienteMin.setLongCliente(longitud[0]);
                                clienteMin.setNameCliente(nombre[0]);
                                clienteMin.setDirCliente(direccion[0]);
                                clienteMin.setTelfCliente(telefono[0]);
                                clienteMin.setEstadoVisita(estadoVisita[0]);

                                double lat = Double.parseDouble(clienteMin.getLattCliente());
                                double lng = Double.parseDouble(clienteMin.getLongCliente());
                                LatLng mark = new LatLng(lat,lng);
                                Log.d("Latitud:", String.valueOf(lat));
                                Log.d("Longitud:", String.valueOf(lng));
                               // mMap.addMarker(marcaCliente.position(mark).title(clienteMin.getNameCliente() + " " + clienteMin.getTelfCliente() + " "  + clienteMin.getDirCliente() ));

                               if (estadoVisita[0] == 2) {
                                    mMap.addMarker(marcaClienteVisitado.position(mark).title(clienteMin.getNameCliente() + " " + clienteMin.getTelfCliente() + " "  + clienteMin.getDirCliente() ));
                                } else {
                                    mMap.addMarker(marcaCliente.position(mark).title(clienteMin.getNameCliente() + " " + clienteMin.getTelfCliente() + " "  + clienteMin.getDirCliente() ));
                                }
                                //clienteMins.add(clienteMin);
                                Log.d("detalle ruta :",id[0] );
                            }
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(guayaquil));

                            //Log.d("detalle ruta :",clienteMins.toString() );
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
        requestQueue.add(jsonObjectRequestRequestRutaDeClientes);
    }


    private void cargarRutaDeClientesDB(List<ClienteMin> clienteMins, String tokenEjemplo) {

        final MarkerOptions marcaClienteVisitado = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.visitado_150)).anchor(0.0f,1.0f);

        final MarkerOptions marcaCliente = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.visita_150)).anchor(0.0f,1.0f);

         new  AsyncTask<List<ClienteMin>, Void, List<ClienteMin>>() {
            @Override
            protected List<ClienteMin> doInBackground(List<ClienteMin>... lists) {
                return appDatabase.clienteMinDao().getAll();
            }

            @Override
            protected void onPostExecute(List<ClienteMin> clienteMins) {
                super.onPostExecute(clienteMins);
                final LatLng guayaquil = new LatLng(-2.16753, -79.89369);
                mMap.addMarker(new MarkerOptions().position(guayaquil).title("Marker at Innova"));

                for (ClienteMin clienteMin : clienteMins) {
                    //double lat = Double.parseDouble(clienteMin.getLattCliente());
                    //double lng = Double.parseDouble(clienteMin.getLongCliente());
                    //LatLng mark = new LatLng(lat,lng);
                    //Log.d("Latitud:", String.valueOf(lat));
                    //Log.d("Longitud:", String.valueOf(lng));
                    LatLng mark = new LatLng(Double.parseDouble(clienteMin.getLattCliente()),Double.parseDouble(clienteMin.getLongCliente()));
                    if (clienteMin.getEstadoVisita() == 2) {
                        mMap.addMarker(marcaClienteVisitado.position(mark).title(clienteMin.getNameCliente() + " " + clienteMin.getTelfCliente() + " "  + clienteMin.getDirCliente() ));
                    } else {
                        mMap.addMarker(marcaCliente.position(mark).title(clienteMin.getNameCliente() + " " + clienteMin.getTelfCliente() + " "  + clienteMin.getDirCliente() ));
                    }
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(guayaquil));
            }
        }.execute(clienteMins);
    }


    public boolean comprobarSalidaInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Si hay conexión a Internet en este momento
            Log.d("Ruta de Clientes", " Estado actual: " + networkInfo.getState());
            return true;
        } else {
            // No hay conexión a Internet en este momento
            Log.d("Ruta de Clientes", "Estás offline");
            return false;
        }
    }
}
