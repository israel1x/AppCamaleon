package com.example.pasantias.appcamaleon.Pojos;

import java.io.Serializable;

public class Producto implements Serializable{
    private int id_producto;
    private String productoNombre;
    private String productoPresentacion;
    private Double productoPrecio;

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public String getProductoPresentacion() {
        return productoPresentacion;
    }

    public void setProductoPresentacion(String productoPresentacion) {
        this.productoPresentacion = productoPresentacion;
    }

    public Double getProductoPrecio() {
        return productoPrecio;
    }

    public void setProductoPrecio(Double productoPrecio) {
        this.productoPrecio = productoPrecio;
    }
}
