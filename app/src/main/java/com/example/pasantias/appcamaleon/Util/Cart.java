package com.example.pasantias.appcamaleon.Util;

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

    private static DecimalFormat df2 = new DecimalFormat(".##");

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

    public static void update(){

    }

    public static List<Item> conItems(){
        return items;
    }

    public static String subTotal(){
        double subTotal=0;
        for(Item item : items){
            subTotal+= item.getProducto().getProductoPrecio()*item.getCantidad();
        }
        return df2.format(subTotal);
    }
    public static String iva(){
        double iva=0;
        iva=Double.parseDouble(subTotal())*0.12;
        return df2.format(iva);
    }

    public static String total(){
        double total=0;
        total=Double.parseDouble(subTotal())+Double.parseDouble(iva());
        return df2.format(total);
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
