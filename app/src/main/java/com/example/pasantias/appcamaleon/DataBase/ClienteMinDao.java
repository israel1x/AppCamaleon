package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ClienteMinDao {

    @Query("SELECT * FROM clientemin")
    List<ClienteMin> getAll();

    @Query("SELECT * FROM clientemin where name LIKE  :firstName")
    ClienteMin findByName(String firstName);

    @Query("SELECT id, name, ruc, dir, telf, estadovisita FROM clientemin")
    List<ClienteMin> findForListCliente();

    @Query("SELECT id, name, lattitud, longitud, estadovisita FROM clientemin")
    List<ClienteMin> findForRutaClientes();

    @Query("SELECT * FROM clientemin LIMIT :limite OFFSET :offset")
    List<ClienteMin> getClienteMinOffset(String limite,String offset);

    @Query("SELECT * FROM clientemin WHERE name LIKE :string COLLATE NOCASE LIMIT :limite OFFSET :offset")
    List<ClienteMin> getClienteMinOffsetString(String limite,String offset,String string);

    @Query("SELECT COUNT(*) from clientemin")
    int countUsers();

    //RETORNA LA FECHA DE VISITA DE UN CLIENTE
    @Query("SELECT fechavisita from clientemin where id = :clienteBuscado")
    String getFechaDeVisitaAlCliente(int clienteBuscado);

    //RETORNA  UNA LISTA CON TODOS CLIENTES POR FECHA DE VISITA
    @Query("SELECT * FROM clientemin WHERE fechavisita = :fechaDeVisita")
    List<ClienteMin> getAllClientesByFechaDeVisita(String fechaDeVisita);

    @Query("SELECT * from clientemin where id = :idClienteBuscado")
    ClienteMin getCliente(int idClienteBuscado);

    @Insert(onConflict = REPLACE)
    void insertAll(List<ClienteMin> clienteMins);

    @Insert(onConflict = REPLACE)
    void insertOne(ClienteMin clienteMin);

    @Update(onConflict = REPLACE)
    void update(ClienteMin clienteMin);

    @Delete
    void delete(ClienteMin clienteMin);
}
