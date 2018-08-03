package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "pedido")
public class Pedido {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idPedido")
    @NonNull
    private Long idPedido;

    @NonNull
    @ColumnInfo(name = "idCliente")
    private Integer idCliente;

    @NonNull
    @ColumnInfo(name = "nombreCliente")
    private String nombreCliente;

    @NonNull
    @ColumnInfo(name = "pfecha")
    private String fecha;

    @ColumnInfo(name = "lattitud")
    private String lattPedido;

    @ColumnInfo(name = "longitud")
    private String longPedido;

    @NonNull
    @ColumnInfo(name = "estadoPedido")
    private Integer estadoPedido;


    @NonNull
    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(@NonNull Long idPedido) {
        this.idPedido = idPedido;
    }

    @NonNull
    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(@NonNull Integer idCliente) {
        this.idCliente = idCliente;
    }

    @NonNull
    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(@NonNull String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getLattPedido() {
        return lattPedido;
    }

    public void setLattPedido(String lattPedido) {
        this.lattPedido = lattPedido;
    }

    public String getLongPedido() {
        return longPedido;
    }

    public void setLongPedido(String longPedido) {
        this.longPedido = longPedido;
    }

    @NonNull
    public Integer getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(@NonNull Integer estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    @NonNull
    public String getFecha() {
        return fecha;
    }

    public void setFecha(@NonNull String fecha) {
        this.fecha = fecha;
    }
}
