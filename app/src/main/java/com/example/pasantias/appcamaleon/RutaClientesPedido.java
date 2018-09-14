package com.example.pasantias.appcamaleon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.pasantias.appcamaleon.Adapters.ClientesRutaAdaptador;
import com.example.pasantias.appcamaleon.DataBase.AppDatabase;
import com.example.pasantias.appcamaleon.DataBase.ClienteMin;
import com.example.pasantias.appcamaleon.Pojos.Cliente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RutaClientesPedido extends Fragment {

    private FragmentActivity myContext;

    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";

    private static final String TAG = "ClientesRuta";
    private RecyclerView recyclerView;
    private ClientesRutaAdaptador clientesRutaAdaptador;
    private int offset;
    private boolean aptoParaCargar;

    public static AppDatabase appDatabase;

    private int modoTrabajo;

    // private OnFragmentInteractionListener mListener;

    public RutaClientesPedido() {

    }


    // TODO: Rename and change types and number of parameters
    public static RutaClientesPedido newInstance(String param1, String param2) {
        RutaClientesPedido fragment = new RutaClientesPedido();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ruta_clientes_pedido, container, false);
        appDatabase = AppDatabase.getAppDatabase(myContext.getApplication());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewClientes);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("datosAplicacion", MODE_PRIVATE);
        modoTrabajo = sharedPreferences.getInt("modoDeTrabajo", 0);

        clientesRutaAdaptador = new ClientesRutaAdaptador(myContext);

        recyclerView.setAdapter(clientesRutaAdaptador);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(myContext, LinearLayoutManager.VERTICAL, false);
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
                            aptoParaCargar = false;
                            offset += 1;

                            if (modoTrabajo == 1) {
                                if (comprobarSalidaInternet()) {
                                    obtenerDatos(tokenEjemplo, offset, "sin datos");
                                }else{
                                    Toast.makeText(getContext(), "No hay conección a Internet" , Toast.LENGTH_SHORT).show();
                                }
                            }else{
                               obtenerDatosOffline(offset, null);
                            }


                        }
                    }
                }
            }
        });
        offset = 0;

        if (modoTrabajo == 1) {
            if (comprobarSalidaInternet()) {
                obtenerDatos(tokenEjemplo, offset, "sin datos");
            }else{
                Toast.makeText(getContext(), "No hay conección a Internet" , Toast.LENGTH_SHORT).show();
            }
        }else{
              obtenerDatosOffline(offset, null);
        }

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
       /* if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
        //  mListener = null;
    }

    private void obtenerDatos(String token, int offset, String keyWord) {

        final String[] id = new String[1];
        final String[] nombre = new String[1];
        final String[] latitud = new String[1];
        final String[] longitud = new String[1];
        final String[] direccion = new String[1];
        final String[] ruc = new String[1];
        final String[] telefono = new String[1];
        final String[] id_estado= new String[1];

        JSONObject jsonObjectPeticionCliente = new JSONObject();
        try {
            jsonObjectPeticionCliente.put("metodo", "listaClientes");
            jsonObjectPeticionCliente.put("token", token);
            jsonObjectPeticionCliente.put("offset", offset);
            jsonObjectPeticionCliente.put("limit", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject[] jsonObjectRespuestaClientes = {new JSONObject()};
        final JSONArray[] jsonArrayDetalleClientes = {new JSONArray()};
        final JSONObject[] jsonObjectUno = {new JSONObject()};


        String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";
        Log.d("Web S. ListaClientes:", url);
        ////Uso del web service para traer la ruta de clientes

        RequestQueue requestQueue = Volley.newRequestQueue(myContext.getApplicationContext());
        JsonObjectRequest jsonObjectRequestRequestRutaDeClientes = new JsonObjectRequest(
                url,
                jsonObjectPeticionCliente,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Data del WS", response.toString());
                        try {

                            ArrayList<Cliente> list = new ArrayList<>();
                            jsonObjectRespuestaClientes[0] = response.getJSONObject("lista");
                            if (jsonObjectRespuestaClientes[0].length() > 0) {
                                jsonArrayDetalleClientes[0] = jsonObjectRespuestaClientes[0].getJSONArray("listaClientes");

                                for (int i = 0; i < jsonArrayDetalleClientes[0].length(); i++) {
                                    jsonObjectUno[0] = jsonArrayDetalleClientes[0].getJSONObject(i);
                                    id[0] = jsonObjectUno[0].getString("id_cliente");
                                    ruc[0] = jsonObjectUno[0].getString("ruc");
                                    nombre[0] = jsonObjectUno[0].getString("nombre");
                                    direccion[0] = jsonObjectUno[0].getString("direccion");
                                    telefono[0] = jsonObjectUno[0].getString("telefono");
                                    latitud[0] = jsonObjectUno[0].getString("latitud");
                                    longitud[0] = jsonObjectUno[0].getString("longitud");
                                    id_estado[0]=jsonObjectUno[0].getString("id_estado");

                                    Cliente cliente = new Cliente(Integer.parseInt(id[0]), ruc[0], nombre[0], direccion[0], telefono[0], Double.parseDouble(latitud[0]), Double.parseDouble(longitud[0]),Integer.parseInt(id_estado[0]));
                                    list.add(cliente);

                                }
                                clientesRutaAdaptador.adicionarListaCliente(list);
                                //  blockSearch=false;
                                // bt_buscar_producto.setEnabled(true);
                                aptoParaCargar = true;
                            }
                            //  blockSearch=false;
                            // bt_buscar_producto.setEnabled(true);

                        } catch (JSONException e) {
                            // blockSearch=false;
                            //  bt_buscar_producto.setEnabled(true);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  blockSearch=false;
                // bt_buscar_producto.setEnabled(true);
                Log.d("ERROR", error.toString());
            }
        }
        );
        requestQueue.add(jsonObjectRequestRequestRutaDeClientes);

        //return data_productos;


    }

    public void obtenerDatosOffline(int offset, String keyWord) {
        final String[] id = new String[1];
        final String[] nombre = new String[1];
        final String[] latitud = new String[1];
        final String[] longitud = new String[1];
        final String[] direccion = new String[1];
        final String[] ruc = new String[1];
        final String[] telefono = new String[1];
        final String[] id_estado= new String[1];



        ArrayList<Cliente> list = new ArrayList<>();
        List<ClienteMin> clienteMins;
        int limit = 5;
        int offsetcan = limit * offset;
        //if (!keyWord.equals(null) && !keyWord.equals(" ") && !keyWord.equals("")) {
           // clienteMins = appDatabase.clienteMinDao().getClienteMinOffsetString(String.valueOf(limit), String.valueOf(offsetcan), keyWord);
      //  } else {
            clienteMins = appDatabase.clienteMinDao().getClienteMinOffset(String.valueOf(limit), String.valueOf(offsetcan));
       // }

        if (clienteMins.size() > 0) {

            for (int i = 0; i < clienteMins.size(); i++) {
                id[0] = clienteMins.get(i).getIdCliente();
                ruc[0] = clienteMins.get(i).getRucCliente();
                nombre[0] = clienteMins.get(i).getNameCliente();
                direccion[0] = clienteMins.get(i).getDirCliente();
                telefono[0] = clienteMins.get(i).getTelfCliente();
                latitud[0] = clienteMins.get(i).getLattCliente();
                longitud[0] = clienteMins.get(i).getLongCliente();
                id_estado[0]=String.valueOf(clienteMins.get(i).getEstadoVisita());

                Cliente cliente = new Cliente(Integer.parseInt(id[0]), ruc[0], nombre[0], direccion[0], telefono[0], Double.parseDouble(latitud[0]), Double.parseDouble(longitud[0]),Integer.parseInt(id_estado[0]));
                list.add(cliente);
            }
            clientesRutaAdaptador.adicionarListaCliente(list);
            // bt_buscar_producto.setEnabled(true);
            aptoParaCargar = true;
        } else {
            Toast.makeText(myContext, "No existen clientes", Toast.LENGTH_LONG).show();
        }

    }



    /*   public interface OnFragmentInteractionListener {
           // TODO: Update argument type and name
           void onFragmentInteraction(Uri uri);
       }*/
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
