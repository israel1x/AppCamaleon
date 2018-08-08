package com.example.pasantias.appcamaleon.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pasantias.appcamaleon.Pojos.Item;
import com.example.pasantias.appcamaleon.R;

import java.util.ArrayList;
import java.util.List;

public class ItemPedidoAdapter extends RecyclerView.Adapter<ItemPedidoAdapter.ItemViewHolder> {
    private List<Item> productos;
    private Context context;

    public ItemPedidoAdapter(Context context) {
        this.context = context;
        productos = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_table_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = productos.get(position);
        holder.cellNombre.setText(item.getProducto().getProductoNombre());
        holder.cellPresentacion.setText(item.getProducto().getProductoPresentacion());
        holder.cellPrecio.setText(item.getProducto().getProductoPrecio().toString());
        holder.cellCantidad.setText(item.getCantidad().toString());
        holder.cellSubtotal.setText(String.valueOf(item.getCantidad() * item.getProducto().getProductoPrecio()));

        holder.rowTable.setBackgroundColor(position % 2 == 0 ? Color.LTGRAY : Color.WHITE);

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void limpiarLista() {
        productos.clear();
    }

    public void adicionarListaItem(List<Item> lstItem) {
        productos.addAll(lstItem);
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView cellNombre;
        private TextView cellPresentacion;
        private TextView cellPrecio;
        private TextView cellCantidad;
        private TextView cellSubtotal;
        private LinearLayout rowTable;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cellNombre = (TextView) itemView.findViewById(R.id.cellNombre);
            cellPresentacion = (TextView) itemView.findViewById(R.id.cellPresentacion);
            cellPrecio = (TextView) itemView.findViewById(R.id.cellPrecio);
            cellCantidad = (TextView) itemView.findViewById(R.id.cellCantidad);
            cellSubtotal = (TextView) itemView.findViewById(R.id.cellSubtotal);
            rowTable = (LinearLayout) itemView.findViewById(R.id.rowTable);
        }
    }
}
