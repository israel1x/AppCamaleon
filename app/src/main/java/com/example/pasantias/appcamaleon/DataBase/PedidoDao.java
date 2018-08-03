package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
//import android.arch.lifecycle.LiveData;
import java.util.List;

@Dao
public interface PedidoDao {

    @Query("SELECT * FROM pedido")
    List<Pedido> getAll();

    @Query("SELECT * FROM pedido where idPedido = :idPedido")
    Pedido findByIdPedido(String idPedido);

   // @Query("SELECT * from pedido ORDER BY estadoPedido ASC")
    //LiveData<List<Pedido>> getAllPedido();

    @Query("SELECT COUNT(*) from pedido")
    int countPedido();

    @Insert
    void insert(Pedido pedido);

    @Insert
    Long insertId(Pedido pedido);

    @Update
    void update(Pedido pedido);

    @Delete
    void delete(Pedido pedido);
}
