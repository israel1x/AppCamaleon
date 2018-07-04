package com.example.pasantias.appcamaleon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.pasantias.appcamaleon.Pojos.Cliente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import iammert.com.expandablelib.ExpandCollapseListener;
import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

public class ListaClientes extends AppCompatActivity {

    final String tokenEjemplo = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzAwYWY5NmMzZDE2MjYyNDRmYzMyMjc3M2U2MmJjNWFjNmM0NGRlIiwiZGF0YSI6eyJ1c3VhcmlvSWQiOjEsInZlbmRlZG9ySWQiOjEsInVzZXJuYW1lIjoid2lsc29uIn19.e-yTp8RRMecWB6-ZJODHnCnxEJXtODydjVxWmHVFFjY";

    
    ArrayList<Cliente> clientes = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);

        clientes.add(new Cliente("0988829914", "Israel Zurita", "La troncal"));
        clientes.add(new Cliente("0988888888", "Pedro Figueroa", "Guayaquil"));
        clientes.add(new Cliente("0999999999", "Luis Lainez", "Quito"));


        ExpandableLayout sectionLinearLayout = (ExpandableLayout) findViewById(R.id.el_listaClientes);

        sectionLinearLayout.setRenderer(new ExpandableLayout.Renderer<Cliente, Cliente>() {
            @Override
            public void renderParent(View view, Cliente model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.tv_parent_nameCliente)).setText(model.nombre);
                 view.findViewById(R.id.arrow).setBackgroundResource(isExpanded ? R.drawable.arrow_up : R.drawable.arrow_down);
            }

            @Override
            public void renderChild(View view, Cliente model, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.tv_child_ci)).setText(model.ruc);
                ((TextView) view.findViewById(R.id.tv_child_dir)).setText(model.direccion);
            }
        });


        conjuntoDeSecciones(clientes,sectionLinearLayout);
        //sectionLinearLayout.addSection(getSection());
        //sectionLinearLayout.addSection(getSection());
        //sectionLinearLayout.addSection(getSection());


        sectionLinearLayout.setExpandListener(new ExpandCollapseListener.ExpandListener<Cliente>() {
            @Override
            public void onExpanded(int parentIndex, Cliente parent, View view) {
                //Layout expanded
            }
        });

        sectionLinearLayout.setCollapseListener(new ExpandCollapseListener.CollapseListener<Cliente>() {
            @Override
            public void onCollapsed(int parentIndex, Cliente parent, View view) {
                //Layout collapsed
            }
        });


    }

    public void consultarWSListaClientes(String token){


        JSONObject jsonObjectCliente = new JSONObject();
        try {
            jsonObjectCliente.put("metodo","listaClientes");
            jsonObjectCliente.put("token",token);
        }catch (JSONException e){
            e.printStackTrace();
        }

        final ArrayList<Cliente> data_Clientes = new ArrayList<Cliente>();

        //Log.d("ZZZZZZZZZ: ", productoBuscado);
        //productoBuscado = productoBuscado.toString();
        //Consulta al api de productos
        String url = "http://innovasystem.ddns.net:8089/wsCamaleon/servicios";


        Log.d("Ruta al web service: ", url);
        ////Uso del web service para traer los productos

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequestDatosDelproducto = new JsonArrayRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseProductos) {
                        //Response OK!! :)
                        Log.d("DATOS DEL PRODUCTO",responseProductos.toString());


                        try {
                            JSONArray dataProductos = responseProductos;
                            Log.d("ISRAEL", dataProductos.toString());

                            if ((dataProductos).length()== 0) {
                                Log.d("No DATOS DEL PRODUCTO: ", "No tiene DATOS");
                                Toast.makeText(getApplicationContext(),"No tiene DATOS",Toast.LENGTH_LONG).show();
                                //Si no existe ningun elemento muestro un mensahj que no hay datos del  PRODUCTO

                            } else {
                                Log.d("ISRAEL", dataProductos.toString());
                                Toast.makeText(getApplicationContext(),"No tiene DATOS", Toast.LENGTH_LONG).show();

                            }
                        }catch (Error error){
                            Log.d("ERROR Ay",error.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR X",error.toString());
            }
        });

        requestQueue.add(jsonArrayRequestDatosDelproducto);

        //return data_productos;
    }


    public Section<Cliente, Cliente> getSection(int i) {

        Section<Cliente, Cliente> section = new Section<>();
        Cliente padre = clientes.get(i);
        Cliente ruc = clientes.get(i);
        //Cliente direccion = clientes.get(0);

        section.parent = padre;
        section.children.add(ruc);
        //section.children.add(direccion);
        section.expanded = false;

        return section;
    }

    public void conjuntoDeSecciones(ArrayList<Cliente> clientes, ExpandableLayout sectionLinearLayout ) {

        for (int i = 0; i < clientes.size(); i++) {

            Section<Cliente, Cliente> section = new Section<>();
            Cliente padre = clientes.get(i);
            Cliente ruc = clientes.get(i);
            //Cliente direccion = clientes.get(0);

            section.parent = padre;
            section.children.add(ruc);
            //section.children.add(direccion);
            section.expanded = false;

            sectionLinearLayout.addSection(section);
        }

    }

}
