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
    @ColumnInfo(name = "idupd")
    @NonNull
    private int idActualizacion;

    @ColumnInfo(name = "updclientes")
    @TypeConverters(DataConverter.class)
    private Date fechaUpdateClientes;

    @ColumnInfo(name = "updproductos")
    @TypeConverters(DataConverter.class)
    private Date fechaUpdateProductos;


    public Actualizacion(@NonNull int idActualizacion, Date fechaUpdateClientes, Date fechaUpdateProductos) {
        this.idActualizacion = idActualizacion;
        this.fechaUpdateClientes = fechaUpdateClientes;
        this.fechaUpdateProductos = fechaUpdateProductos;
    }

    @NonNull
    public int getIdActualizacion() {
        return idActualizacion;
    }

    public void setIdActualizacion(@NonNull int idActualizacion) {
        this.idActualizacion = idActualizacion;
    }

    public Date getFechaUpdateClientes() {
        return fechaUpdateClientes;
    }

    public void setFechaUpdateClientes(Date fechaUpdateClientes) {
        this.fechaUpdateClientes = fechaUpdateClientes;
    }

    public Date getFechaUpdateProductos() {
        return fechaUpdateProductos;
    }

    public void setFechaUpdateProductos(Date fechaUpdateProductos) {
        this.fechaUpdateProductos = fechaUpdateProductos;
    }
}
