package com.example.pasantias.appcamaleon.DataBase;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "actualizacion")
public class Actualizacion {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "idupdate")
    @NonNull
    private int idActualizacion;

    @ColumnInfo(name = "updateclientes")
    //@TypeConverters(DataConverter.class)
    private String fechaUpdateClientes;

    @ColumnInfo(name = "updateproductos")
    //@TypeConverters(DataConverter.class)
    private String fechaUpdateProductos;

    @ColumnInfo(name = "updatestockprecio")
    private String fechaUpdateStockPrecios;

    public Actualizacion(@NonNull int idActualizacion, String fechaUpdateClientes, String fechaUpdateProductos, String fechaUpdateStockPrecios) {
        this.idActualizacion = idActualizacion;
        this.fechaUpdateClientes = fechaUpdateClientes;
        this.fechaUpdateProductos = fechaUpdateProductos;
        this.fechaUpdateStockPrecios = fechaUpdateStockPrecios;
    }

    @NonNull
    public int getIdActualizacion() {
        return idActualizacion;
    }

    public void setIdActualizacion(@NonNull int idActualizacion) {
        this.idActualizacion = idActualizacion;
    }

    public String getFechaUpdateClientes() {
        return fechaUpdateClientes;
    }

    public void setFechaUpdateClientes(String fechaUpdateClientes) {
        this.fechaUpdateClientes = fechaUpdateClientes;
    }

    public String getFechaUpdateProductos() {
        return fechaUpdateProductos;
    }

    public void setFechaUpdateProductos(String fechaUpdateProductos) {
        this.fechaUpdateProductos = fechaUpdateProductos;
    }

    public String getFechaUpdateStockPrecios() {
        return fechaUpdateStockPrecios;
    }

    public void setFechaUpdateStockPrecios(String fechaUpdateStockPrecios) {
        this.fechaUpdateStockPrecios = fechaUpdateStockPrecios;
    }
}
