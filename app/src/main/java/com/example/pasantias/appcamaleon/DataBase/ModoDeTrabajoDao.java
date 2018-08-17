package com.example.pasantias.appcamaleon.DataBase;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ModoDeTrabajoDao {

    //RETORNA EL ESTADO O EL VALOR DEL MODO DE TRABAJO ACTUAL DE LA APLICACION
    @Query("SELECT modotrabajo from mododetrabajo where idmodotrabajo = :idmododetrabajo")
    int getEstadoDeModoDeTrabajo(int idmododetrabajo);

    @Query("SELECT COUNT(*) from mododetrabajo")
    int countModoDeTrabajo();

    @Insert(onConflict = REPLACE)
    void insertOne(ModoDeTrabajo modoDeTrabajo);

    @Update(onConflict = REPLACE)
    void update(ModoDeTrabajo modoDeTrabajo);

    @Delete
    void delete(ModoDeTrabajo modoDeTrabajo);
}
