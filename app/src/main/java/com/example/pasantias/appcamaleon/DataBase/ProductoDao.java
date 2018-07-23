package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ProductoDao {

    @Query("SELECT * FROM producto")
    List<Producto> loadAllProducto();

    /*@Query("SELECT * FROM producto WHERE name = :nombre")
    String findByNombreProducto(String[] nombre);*/

    @Insert
    void insert(Producto producto);

    @Insert
    void insertAll(ArrayList<Producto> productos);

    @Insert
    void insterAll2(Producto... productos);

    @Update
    void update(Producto producto);

    @Delete
    void delete(Producto producto);
}
