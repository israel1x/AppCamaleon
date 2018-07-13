package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface UsuarioDao {

    @Query("SELECT * FROM usuario WHERE id = :iddata")
    Usuario findByIdUser(int iddata);

    @Query("SELECT token FROM usuario WHERE id = :iddata")
    String findByTokenUser(int iddata);

    @Insert
    void insert(Usuario usuario);

    @Update
    void update(Usuario usuario);

    @Delete
    void delete(Usuario usuario);
}
