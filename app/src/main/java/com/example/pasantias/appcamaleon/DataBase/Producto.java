package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "producto")
public class Producto {

    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey()
    private int idProducto;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "marca")
    private String marca;

    @NonNull
    @ColumnInfo(name = "presentacion")
    private String presentacion;

    @NonNull
    @ColumnInfo(name = "stock")
    private int stock;

    @NonNull
    @ColumnInfo(name = "pUnitario")
    public double pUnitario;

    @NonNull
    @ColumnInfo(name = "subcategoria")
    private String subcategoria_pro;

    @NonNull
    public String getSubcategoria_pro() {
        return subcategoria_pro;
    }

    public void setSubcategoria_pro(@NonNull String subcategoria_pro) {
        this.subcategoria_pro = subcategoria_pro;
    }

    @NonNull
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(@NonNull int idProducto) {
        this.idProducto = idProducto;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getMarca() {
        return marca;
    }

    public void setMarca(@NonNull String marca) {
        this.marca = marca;
    }

    @NonNull
    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(@NonNull String presentacion) {
        this.presentacion = presentacion;
    }

    @NonNull
    public int getStock() {
        return stock;
    }

    public void setStock(@NonNull int stock) {
        this.stock = stock;
    }

    @NonNull
    public double getpUnitario() {
        return pUnitario;
    }

    public void setpUnitario(@NonNull double pUnitario) {
        this.pUnitario = pUnitario;
    }
}
