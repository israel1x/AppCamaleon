package com.example.pasantias.appcamaleon.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pasantias.appcamaleon.Pojos.Cliente;
import com.example.pasantias.appcamaleon.R;

import java.util.ArrayList;

import iammert.com.expandablelib.ExpandCollapseListener;
import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

public class ListaDeClientes extends Fragment {

    ArrayList<Cliente> clientes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_de_clientes, container,false );

        //return super.onCreateView(inflater, container, savedInstanceState);

        clientes.add(new Cliente("0988829914", "Israel Zurita", "La troncal","2421191"));
        clientes.add(new Cliente("0988888888", "Pedro Figueroa", "Guayaquil","2421191"));
        clientes.add(new Cliente("0999999999", "Luis Lainez", "Quito","2421191"));


        ExpandableLayout sectionLinearLayout = (ExpandableLayout) v.findViewById(R.id.el_lista_de_Clientes);

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
                ((TextView) view.findViewById(R.id.tv_child_telf)).setText(model.telefono);
            }
        });


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


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
