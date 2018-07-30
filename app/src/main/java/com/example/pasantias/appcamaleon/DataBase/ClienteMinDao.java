package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ClienteMinDao {

    @Query("SELECT * FROM clientemin")
    List<ClienteMin> getAll();

    @Query("SELECT * FROM clientemin where name LIKE  :firstName")
    ClienteMin findByName(String firstName);

    @Query("SELECT id, name, ruc, dir, telf FROM clientemin")
    List<ClienteMin> findForListCliente();

    @Query("SELECT id, name, lattitud, longitud FROM clientemin")
    List<ClienteMin> findForRutaClientes();

    @Query("SELECT COUNT(*) from clientemin")
    int countUsers();

    @Insert
    void insertAll(List<ClienteMin> clienteMins);

    @Insert
    void insertOne(ClienteMin clienteMin);

    @Update
    void update(ClienteMin clienteMin);

    @Delete
    void delete(ClienteMin clienteMin);
}
