package com.example.pasantias.appcamaleon.Fragments;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pasantias.appcamaleon.DataBase.AppDatabase;
import com.example.pasantias.appcamaleon.DataBase.ClienteMin;
import com.example.pasantias.appcamaleon.Pojos.Cliente;
import com.example.pasantias.appcamaleon.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static AppDatabase appDatabase;
    List<ClienteMin> clienteMins = new ArrayList<>();
    List<ClienteMin> rutas = new ArrayList<>();

    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_maps,container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        super.onViewCreated(view, savedInstanceState);
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

        // Add a marker in Sydney and move the camera
        LatLng guayaquil = new LatLng(-2.16753, -79.89369);
        LatLng prueba2   = new LatLng(-2.175027, -79.886518);
        mMap.addMarker(new MarkerOptions().position(guayaquil).title("Marker in Innova"));
        mMap.addMarker(new MarkerOptions().position(prueba2).title("Marker Test!!!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(guayaquil));
        //mMap.setIndoorEnabled(false);
    }

    @SuppressLint("LongLogTag")
    public void traerRutaDeClientes(final List<ClienteMin> clienteMins, String token) {
    //public void traerRutaDeClientes(String token) {

        final ClienteMin clienteMin = new ClienteMin();
        final String[] id = new String[1];
        final String[] nombre = new String[1];
        final String[] latitud = new String[1];
        final String[] longitud = new String[1];
        final String[] direccion = new String[1];

        JSONObject jsonObjectCliente = new JSONObject();
        try {
            jsonObjectCliente.put("metodo","rutaCliente");
            jsonObjectCliente.put("token",token);
        }catch (JSONException e){
            e.printStackTrace();
        }

        //final ArrayList<Cliente> data_Clientes = new ArrayList<Cliente>();

        final JSONObject[] jsonObjectRutas = {new JSONObject()};
        final JSONArray[] jsonArrayDetalleRutas = {new JSONArray()};

        final JSONObject[] jsonObjectUno = {new JSONObject()};

        String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";
        Log.d("Web service rutaClientes:", url);
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
                                clienteMin.setIdCliente(id[0]);
                                clienteMin.setLattCliente(latitud[0]);
                                clienteMin.setLongCliente(longitud[0]);
                                clienteMin.setNameCliente(nombre[0]);
                                clienteMin.setDirCliente(direccion[0]);

                                clienteMins.add(clienteMin);

                                Log.d("detalle ruta :",id[0] );
                            }
                            Log.d("detalle ruta :",clienteMins.toString() );
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
}
