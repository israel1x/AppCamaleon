package com.example.pasantias.appcamaleon.Fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pasantias.appcamaleon.R;


public class IngresarPedidoFragment extends Fragment {

    private OnListenerIngresarPedido mListener;
    private FragmentActivity myContext;

    public IngresarPedidoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static IngresarPedidoFragment newInstance() {
        IngresarPedidoFragment fragment = new IngresarPedidoFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_ingresar_pedido, container, false);
        final FragmentManager fm = myContext.getFragmentManager();
        final ProductoDialog df = new ProductoDialog();


        Button button = rootView.findViewById(R.id.agregar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                df.show(fm, "Producto_tag");
                onFragmentHitorial("wilson");
                // fragmentListener.changeText();
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onFragmentHitorial(String idCliente) {
        if (mListener != null) {
            mListener.onBuscarHistorial(idCliente);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListenerIngresarPedido) {
            mListener = (OnListenerIngresarPedido) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
        mListener = null;
    }

    public interface OnListenerIngresarPedido {
        // TODO: Update argument type and name
        void onBuscarHistorial(String idCliente);
    }
}
