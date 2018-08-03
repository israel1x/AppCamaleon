package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "detallePedido",foreignKeys = @ForeignKey(entity = Pedido.class,parentColumns ="idPedido",childColumns = "idPedido",onDelete = CASCADE))
public class DetallePedido {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idDetPedido")
    @NonNull
    private Integer idDetPedido;

    @NonNull
    @ColumnInfo(name = "idPedido")
    private Long idPedido;

    @NonNull
    @ColumnInfo(name = "idProducto")
    private Integer idProducto;

    @NonNull
    @ColumnInfo(name = "pNombre")
    private String productoNombre;

    @NonNull
    @ColumnInfo(name = "pPresentacion")
    private String productoPresentacion;

    @NonNull
    @ColumnInfo(name = "pPrecio")
    private Double productoPrecio;

    @NonNull
    @ColumnInfo(name = "cantidad")
    private Integer cantidad;

    @NonNull
    public Integer getIdDetPedido() {
        return idDetPedido;
    }

    public void setIdDetPedido(@NonNull Integer idDetPedido) {
        this.idDetPedido = idDetPedido;
    }

    @NonNull
    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(@NonNull Long idPedido) {
        this.idPedido = idPedido;
    }

    @NonNull
    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(@NonNull Integer idProducto) {
        this.idProducto = idProducto;
    }

    @NonNull
    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(@NonNull String productoNombre) {
        this.productoNombre = productoNombre;
    }

    @NonNull
    public String getProductoPresentacion() {
        return productoPresentacion;
    }

    public void setProductoPresentacion(@NonNull String productoPresentacion) {
        this.productoPresentacion = productoPresentacion;
    }

    @NonNull
    public Double getProductoPrecio() {
        return productoPrecio;
    }

    public void setProductoPrecio(@NonNull Double productoPrecio) {
        this.productoPrecio = productoPrecio;
    }

    @NonNull
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(@NonNull Integer cantidad) {
        this.cantidad = cantidad;
    }
}
