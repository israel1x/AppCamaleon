package com.example.pasantias.appcamaleon.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasantias.appcamaleon.IngresarPedido;
import com.example.pasantias.appcamaleon.Pojos.Producto;
import com.example.pasantias.appcamaleon.R;
import com.example.pasantias.appcamaleon.Util.Cart;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductosViewHolder> {
    private ArrayList<Producto> listaProductos;
    private Context context;

    public ProductosAdapter(Context context) {
        this.context = context;
        listaProductos = new ArrayList<Producto>();
    }

    @Override
    public ProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_procuto, parent, false);
        return new ProductosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductosViewHolder holder, int position) {
        String text = "<font color=#FFB233>PROMOCIÓN: </font> <font color=#B4B0AA> &nbsp; Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s</font>";

        final Producto p = listaProductos.get(position);
        holder.productoNombre.setText(p.getProductoNombre());
        holder.productoPresentacion.setText(p.getProductoPresentacion());
        holder.productoPrecioUnitario.setText("P.UNI: " + p.getProductoPrecio().toString());
        holder.productoPromocion.setText(Html.fromHtml(text));


        holder.bt_ing_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.productoCantidad.getText().length() != 0) {
                    if (!Cart.isExists(p)) {
                        Integer val = Integer.parseInt(String.valueOf(holder.productoCantidad.getText()));
                        if(val!=0) {
                            Cart.setInsertarData(true);
                            Intent intent = new Intent(context, IngresarPedido.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("producto", p);
                            intent.putExtra("cantidad", val);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }else {
                            Toast.makeText(context, "El producto " + p.getProductoNombre() + " tiene que ser mayor a 0", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "El producto " + p.getProductoNombre() + " ya se encuentra ingresado en la lista", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "El producto " + p.getProductoNombre() + " tiene que tener una cantidad", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public void limpiarLista() {
        listaProductos.clear();
    }

    public void adicionarListaAmiibo(ArrayList<Producto> list) {
        listaProductos.addAll(list);
        notifyDataSetChanged();
    }


    public class ProductosViewHolder extends RecyclerView.ViewHolder {
        private TextView productoNombre;
        private TextView productoPresentacion;
        private TextView productoPrecioUnitario;
        private TextView productoPromocion;
        private EditText productoCantidad;
        private Button bt_ing_producto;
        private CardView cardProducto;

        public ProductosViewHolder(View itemView) {
            super(itemView);
            productoNombre = (TextView) itemView.findViewById(R.id.productoNombre);
            productoPresentacion = (TextView) itemView.findViewById(R.id.productoPresentacion);
            productoPrecioUnitario = (TextView) itemView.findViewById(R.id.productoPrecioUnitario);
            productoPromocion = (TextView) itemView.findViewById(R.id.productoPromocion);
            productoCantidad = (EditText) itemView.findViewById(R.id.productoCantidad);
            bt_ing_producto = (Button) itemView.findViewById(R.id.bt_ing_producto);
            cardProducto = (CardView) itemView.findViewById(R.id.cardProducto);
        }
    }
}
