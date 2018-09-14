package com.example.pasantias.appcamaleon.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.pasantias.appcamaleon.HistorialFragment;
import com.example.pasantias.appcamaleon.IngresarPedido;
import com.example.pasantias.appcamaleon.Pojos.Item;
import com.example.pasantias.appcamaleon.Pojos.Producto;
import com.example.pasantias.appcamaleon.R;
import com.example.pasantias.appcamaleon.Util.Cart;
import com.example.pasantias.appcamaleon.Util.MyInterface;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductosViewHolder> {
    private ArrayList<Producto> listaProductos;
    private Context context;


    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;

    public ProductosAdapter(Context context) {
        this.context = context;
        listaProductos = new ArrayList<Producto>();
    }


    @Override
    public ProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductosViewHolder holder, int position) {
        String text = "<font color=#FFB233>PROMOCIÓN: </font> <font color=#B4B0AA> &nbsp; Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s</font>";

        final Producto p = listaProductos.get(position);
        final int post = position;
        holder.productoNombre.setText(p.getProductoNombre());
        holder.productoPresentacion.setText(p.getProductoPresentacion());
        holder.productoPrecioUnitario.setText("P.UNI: " + p.getProductoPrecio().toString());
        holder.productoPromocion.setText(Html.fromHtml(text));
        holder.productoCantidad.setText(!Cart.getExistesItem(p).equals("") ? Cart.getExistesItem(p) : "0");
        //  if(p.isSelect()) {
        // holder.productoNombre.setTextColor(p.isSelect()==true ? Color.RED:Color.GREEN);
        holder.cardProducto.setCardBackgroundColor(Cart.isExists(p) ? Color.LTGRAY : Color.WHITE);
        // }

        holder.bt_mas_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer val = !Cart.getExistesItem(p).equals("") ? Integer.parseInt(Cart.getExistesItem(p)) : 0;
                if (!Cart.isExists(p)) {
                    Cart.insert(new Item(p, val + 1));
                } else {
                    Cart.update(new Item(p, val + 1));
                }
                notifyDataSetChanged();
                try {
                    if (context instanceof IngresarPedido) {
                        ((IngresarPedido) context).actualizar();
                    }
                } catch (ClassCastException exception) {
                    // do something
                }
            }
        });

        holder.bt_menos_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Cart.isExists(p)) {
                    Integer val = !Cart.getExistesItem(p).equals("") ? Integer.parseInt(Cart.getExistesItem(p)) : 0;
                    if (val <= 1) {
                        Cart.remove(p);
                    } else {
                        Cart.update(new Item(p, val - 1));
                    }
                    notifyDataSetChanged();
                    if (context instanceof IngresarPedido) {
                        ((IngresarPedido) context).actualizar();
                    }
                }
            }
        });
    }

    public void updateMensaje(Producto producto, Integer cantidad) {
        final Producto p = producto;
        final Integer val = cantidad;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Producto");
        alertDialog.setMessage("El item ya tiene ingresada la cantidad de " + Cart.getExistesItem(producto) + " desea cambiar la cantidad a " + cantidad);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (val != 0) {
                            Cart.update(new Item(p, val));
                            notifyDataSetChanged();
                            Toast.makeText(context, "El producto " + p.getProductoNombre() + " actualizado", Toast.LENGTH_LONG).show();
                        } else {
                            //Toast.makeText(context, "El producto " + p.getProductoNombre() + " tiene que ser mayor a 0", Toast.LENGTH_LONG).show();
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
        private TextView productoCantidad;
        private Button bt_mas_producto;
        private Button bt_menos_producto;
        private CardView cardProducto;


        public ProductosViewHolder(View itemView) {
            super(itemView);
            productoNombre = (TextView) itemView.findViewById(R.id.nombreProducto);
            productoPresentacion = (TextView) itemView.findViewById(R.id.presentacioProducto);
            productoPrecioUnitario = (TextView) itemView.findViewById(R.id.precioProducto);
            productoPromocion = (TextView) itemView.findViewById(R.id.promocioProducto);
            productoCantidad = (TextView) itemView.findViewById(R.id.cantidadProducto);
            bt_mas_producto = (Button) itemView.findViewById(R.id.bt_mas_producto);
            bt_menos_producto = (Button) itemView.findViewById(R.id.bt_menos_producto);
            cardProducto = (CardView) itemView.findViewById(R.id.cardProducto);
        }
    }

}
