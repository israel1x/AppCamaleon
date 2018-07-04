package com.example.pasantias.appcamaleon.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pasantias.appcamaleon.Pojos.Cliente;
import com.example.pasantias.appcamaleon.R;

import java.util.ArrayList;

public class ClientesAdaptador extends RecyclerView.Adapter<ClientesAdaptador.ClientesViewHolder> implements View.OnClickListener {

    ArrayList<Cliente> listaClientes;
    private  View.OnClickListener listener;

    @Override
    public ClientesAdaptador.ClientesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_clientes_row,null,false);
        v.setOnClickListener(listener);
        return new ClientesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ClientesAdaptador.ClientesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onClick(View view) {

    }

    public class ClientesViewHolder extends RecyclerView.ViewHolder{



        public ClientesViewHolder(View itemView) {
            super(itemView);
        }
    }
}
