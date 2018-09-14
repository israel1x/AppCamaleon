package com.example.pasantias.appcamaleon.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasantias.appcamaleon.DetalleCliente;
import com.example.pasantias.appcamaleon.IngresarPedido;
import com.example.pasantias.appcamaleon.Pojos.Cliente;
import com.example.pasantias.appcamaleon.R;
import com.example.pasantias.appcamaleon.Util.Cart;
import com.example.pasantias.appcamaleon.Util.Util;

import java.util.ArrayList;

public class ClientesRutaAdaptador extends RecyclerView.Adapter<ClientesRutaAdaptador.ClientesRutaViewHolder> {
    private ArrayList<Cliente> listaClientes;
    private Context context;

    public ClientesRutaAdaptador(Context context) {
        this.context = context;
        listaClientes = new ArrayList<Cliente>();
    }

    @Override
    public ClientesRutaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ruta_cliente_fragmento, parent, false);
        return new ClientesRutaViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ClientesRutaViewHolder holder, int position) {
        final Cliente cliente = listaClientes.get(position);
        Log.d("ERROR", String.valueOf(cliente.estado));
        holder.textRuc.setText(cliente.ruc);
        holder.textEstado.setText(Util.Constantes.estadoVisita(cliente.estado));
        holder.textNombre.setText(cliente.nombre);
        holder.textDireccion.setText(cliente.direccion);
        holder.viewEstado.setBackgroundResource(Util.Constantes.estadoVisitaColor(cliente.estado));


        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    updateMensaje(cliente);
            }
        });

    }

    public void updateMensaje(final Cliente cliente) {
        final Cliente c = cliente;
        String mensaje="";
        if (cliente.estado == Util.Constantes.ESTADO_ACTIVO) {
            mensaje="¿Esta seguro de visitar al cliente " + c.getNombre();
        } else {
            mensaje="Cliente " + c.getNombre()+" ya fue visitado, ¿Esta seguro de realizar nuevamente la visita?";
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Ruta");
        alertDialog.setMessage(mensaje);

        alertDialog.setPositiveButton("SI",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (cliente != null) {
                            Cart.setCliente(cliente);
                            Intent intent = new Intent(context, DetalleCliente.class);
                            context.startActivity(intent);
                            //Util.callFragment(R.id.fr_mainContent,new ,context.getFragmentManager());
                            // notifyDataSetChanged();
                            //Toast.makeText(context, "Cliente " + cliente.getNombre(),  Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "No ha seleccionado cliente", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }



    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    public void limpiarLista() {
        listaClientes.clear();
    }

    public void adicionarListaCliente(ArrayList<Cliente> list) {
        listaClientes.addAll(list);
        notifyDataSetChanged();
    }

    public class ClientesRutaViewHolder extends RecyclerView.ViewHolder {
        private CardView cardItem;
        private View viewEstado;
        private TextView textRuc, textEstado, textNombre, textDireccion;

        public ClientesRutaViewHolder(View itemView) {
            super(itemView);
            cardItem = (CardView) itemView.findViewById(R.id.cardItem);
            viewEstado = (View) itemView.findViewById(R.id.viewEstado);
            textRuc = (TextView) itemView.findViewById(R.id.textRuc);
            textEstado = (TextView) itemView.findViewById(R.id.textEstado);
            textNombre = (TextView) itemView.findViewById(R.id.textNombre);
            textDireccion = (TextView) itemView.findViewById(R.id.textDireccion);
        }
    }
}
