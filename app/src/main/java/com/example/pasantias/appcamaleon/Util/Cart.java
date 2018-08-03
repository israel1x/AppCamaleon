package com.example.pasantias.appcamaleon.Util;

import android.content.Context;
import android.widget.Toast;

import com.example.pasantias.appcamaleon.Pojos.Cliente;
import com.example.pasantias.appcamaleon.Pojos.Item;
import com.example.pasantias.appcamaleon.Pojos.Producto;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static List<Item> items =new ArrayList<Item>();
    private static Cliente cliente =new Cliente();
    private static Boolean insertarData=false;

    public static DecimalFormat df2 = new DecimalFormat(".##");

    public static void insert(Item item){
        items.add(item);
    }

    public static void remove(Producto producto){
        int index=getIndex(producto);
        items.remove(index);
    }

    public static void removeListCart(){
        items=new ArrayList<Item>();
        cliente=new Cliente();
    }

    public static void update(Item item){
        remove(item.getProducto());
        insert(item);
    }

    public static List<Item> conItems(){
        return items;
    }

    public static Double subTotal(){
        double subTotal=0;
        for(Item item : items){
            subTotal+= item.getProducto().getProductoPrecio()*item.getCantidad();
        }
        return subTotal;
    }
    public static Double iva(){
        double iva=0;
        iva=subTotal()*0.12;
        return iva;
    }

    public static Double total(){
        double total=0;
        total=subTotal()+iva();
        return total;
        //return df2.format(total);
    }

    public static String subTotalItem(Double precio,Integer cantidad){
        double subTotalItem=0;
        subTotalItem=precio*cantidad;
        return df2.format(subTotalItem);
    }


    public static boolean isExists(Producto producto){
        return getIndex(producto)!= -1;
    }

    private static int getIndex(Producto producto){
        for(int i=0;i<countList();i++){
            if(items.get(i).getProducto().getId_producto()== producto.getId_producto()){
                return i;
            }
        }
        return -1;
    }

    public static String getExistesItem(Producto producto){
        for(int i=0;i<countList();i++){
            if(items.get(i).getProducto().getId_producto()== producto.getId_producto()){
                return items.get(i).getCantidad().toString();
            }
        }
        return "";
    }

    public static int countList(){
        return items.size();
    }

    public static Cliente getCliente() {
        return cliente;
    }

    public static void setCliente(Cliente cliente) {
        Cart.cliente = cliente;
    }

    public static Boolean getInsertarData() {
        return insertarData;
    }

    public static void setInsertarData(Boolean insertarData) {
        Cart.insertarData = insertarData;
    }
}
