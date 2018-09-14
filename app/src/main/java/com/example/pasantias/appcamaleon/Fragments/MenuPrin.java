package com.example.pasantias.appcamaleon.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasantias.appcamaleon.IngresarPedido;
import com.example.pasantias.appcamaleon.ListaClientes;
import com.example.pasantias.appcamaleon.ListaPedidos;
import com.example.pasantias.appcamaleon.MainActivity;
import com.example.pasantias.appcamaleon.Offline;
import com.example.pasantias.appcamaleon.R;
import com.example.pasantias.appcamaleon.RutaClientesPedido;
import com.example.pasantias.appcamaleon.RutaDeClientes;
import com.example.pasantias.appcamaleon.Util.Util;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuPrin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuPrin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuPrin extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button btIngPedidos;
    private Button btVerPedidos;
    private Button btRutaClientes;
    private Button btListClientes;
    private Button btOffline;
    private Button btReportes;

    private TextView tvMododetrabajo;
    private ImageView imgvModotrabajo;

    public MenuPrin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuPrin.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuPrin newInstance(String param1, String param2) {
        MenuPrin fragment = new MenuPrin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        btIngPedidos = (Button) v.findViewById(R.id.bt_ing_pedidos);
        btVerPedidos = (Button) v.findViewById(R.id.bt_ver_pedidos);
        btRutaClientes = (Button) v.findViewById(R.id.bt_ruta_clientes);
        btListClientes = (Button) v.findViewById(R.id.bt_list_clientes);
        btOffline = (Button) v.findViewById(R.id.bt_offline);
        btReportes = (Button) v.findViewById(R.id.bt_reportes);

        imgvModotrabajo = (ImageView) v.findViewById(R.id.imgv_modotrabajo);
        tvMododetrabajo = (TextView) v.findViewById(R.id.tv_mododetrabajo);

        consultarSetearModoDeTrabajo();

        /*SharedPreferences sharedPreferences = getActivity().getSharedPreferences("datosAplicacion", MODE_PRIVATE);
        int modoTrabajo = sharedPreferences.getInt("modoDeTrabajo", 0);
        Log.d("MODO TRABAJO" , String.valueOf(modoTrabajo));
        if (modoTrabajo == 1) {
            tvMododetrabajo.setText("Modo Online");
        } else {
            tvMododetrabajo.setText("Modo Offline");
            imgvModotrabajo.setImageResource(R.drawable.ic_modotrabajo_off);
        }
*/
        btIngPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(getContext(), IngresarPedido.class);
               // startActivity(intent);
                callFragment(R.id.fr_mainContent,new RutaClientesPedido());
                Toast.makeText(getContext(),"Ingresar pedido",Toast.LENGTH_SHORT ).show();

            }
        });

        btVerPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ListaPedidos.class);
                startActivity(intent);
            }
        });

        btRutaClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment rutaDeClientesFra = new RutaDeClientes();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fr_mainContent,rutaDeClientesFra);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });

        btListClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment listaClientesFra = new ListaClientes();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fr_mainContent,listaClientesFra);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });

        btOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment offlineFra = new Offline();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fr_mainContent,offlineFra);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });

        btReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"reportes",Toast.LENGTH_SHORT ).show();
            }
        });



        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        consultarSetearModoDeTrabajo();
    }

    public void consultarSetearModoDeTrabajo() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("datosAplicacion", MODE_PRIVATE);
        int modoTrabajo = sharedPreferences.getInt("modoDeTrabajo", 0);
        Log.d("MODO TRABAJO" , String.valueOf(modoTrabajo));
        if (modoTrabajo == 1) {
            tvMododetrabajo.setText("Modo Online");
            imgvModotrabajo.setImageResource(R.drawable.ic_modotrabajo);
        } else {
            tvMododetrabajo.setText("Modo Offline");
            imgvModotrabajo.setImageResource(R.drawable.ic_modotrabajo_off);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void callFragment(int containerViewId,Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId,fragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
