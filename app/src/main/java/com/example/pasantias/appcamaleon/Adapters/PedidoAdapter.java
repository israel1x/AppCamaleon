package com.example.pasantias.appcamaleon.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pasantias.appcamaleon.DataBase.Pedido;
import com.example.pasantias.appcamaleon.Pojos.Item;
import com.example.pasantias.appcamaleon.R;

import java.util.ArrayList;
import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {
    private List<Pedido> pedidos;
    private Context context;

    public PedidoAdapter(Context context) {
        this.context = context;
        pedidos = new ArrayList<>();
    }

    @Override
    public PedidoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_table_list_pedidos, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.cellN.setText(String.valueOf(position+1));
        holder.cellNombre.setText(pedido.getNombreCliente());
        holder.cellFecha.setText(pedido.getFecha());
        holder.cellEstado.setText(String.valueOf(pedido.getEstadoPedido()));

        holder.rowTable.setBackgroundColor(position % 2 == 0 ? Color.LTGRAY : Color.WHITE);

    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public void limpiarLista() {
        pedidos.clear();
    }

    public void adicionarListaItem(List<Pedido> lstPedido) {
        pedidos.addAll(lstPedido);
        notifyDataSetChanged();
    }

    public class PedidoViewHolder extends RecyclerView.ViewHolder {
        private TextView cellN;
        private TextView cellNombre;
        private TextView cellFecha;
        private TextView cellEstado;
        private LinearLayout rowTable;

        public PedidoViewHolder(View itemView) {
            super(itemView);
            cellN = (TextView) itemView.findViewById(R.id.cellN);
            cellNombre = (TextView) itemView.findViewById(R.id.cellNombre);
            cellFecha = (TextView) itemView.findViewById(R.id.cellFecha);
            cellEstado = (TextView) itemView.findViewById(R.id.cellEstado);
            rowTable = (LinearLayout) itemView.findViewById(R.id.rowTable);
        }
    }
}
