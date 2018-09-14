package com.example.pasantias.appcamaleon.Fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasantias.appcamaleon.R;

import java.util.ArrayList;

public class HistorialFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "Historial";
    private FragmentActivity myContext;
    private TextView textView;


    public HistorialFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HistorialFragment newInstance() {
        HistorialFragment fragment = new HistorialFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_historial, container, false);
        textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(ARG_SECTION_NUMBER);


        return rootView;
    }





    // TODO: Rename method, update argument and hook method into UI event
    public void buscarHistorialCliente(String idCliente) {
        //Toast.makeText(myContext, idCliente, Toast.LENGTH_LONG).show();
        if(idCliente.equals(null)) {
            textView.setText(idCliente);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        myContext = null;
    }


}
