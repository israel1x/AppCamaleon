package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.Date;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@TypeConverters(DataConverter.class)
public interface ActualizacionDao {

    @Query("SELECT * FROM actualizacion WHERE idupd = :idupdate")
    Actualizacion getByIdActualizacion(int idupdate);

    //@Query("SELECT updclientes FROM actualizacion WHERE idupd = :idupdate")
    //Date getFechaActualizacionDeClientes(int idupdate);

    //@Query("SELECT updproductos FROM actualizacion WHERE idupd = :idupdate")
    //Date getFechaActualizacionDeProductos(int idupdate);

    @Insert(onConflict = REPLACE)
    void insertActualizacion(Actualizacion actualizacion);

    @Update(onConflict = REPLACE)
    void update(Actualizacion actualizacion);

    @Delete
    void delete(Actualizacion actualizacion);

    @Query("DELETE  FROM actualizacion")
    void deleteAllActualizaciones();

}
