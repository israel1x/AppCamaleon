package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DetallePedidoDao {

    @Query("SELECT * FROM detallePedido")
    List<DetallePedido> getAll();

    @Query("SELECT * FROM detallePedido where idPedido = :idPedido")
    List<DetallePedido> findByDetallePedido(String idPedido);


    @Query("SELECT COUNT(*) from detallePedido")
    int countDetallePedido();

    @Insert
    void insertAll(List<DetallePedido> detallePedidos);

    @Insert
    void insert(DetallePedido detallePedido);

    @Update
    void update(DetallePedido detallePedido);

    @Delete
    void delete(DetallePedido detallePedido);
}
