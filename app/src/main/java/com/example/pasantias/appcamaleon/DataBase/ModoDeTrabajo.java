package com.example.pasantias.appcamaleon.DataBase;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "mododetrabajo")
public class ModoDeTrabajo {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private int idmodotrabajo;

    // VALORES PARA EL MODO DE TRABAJO
    // ONLINE: 1
    // OFFLINE: 0
    @ColumnInfo(name = "modotrabajo")
    private int modoTrabajo;


    // METODOS

    public ModoDeTrabajo(@NonNull int idmodotrabajo, int modoTrabajo) {
        this.idmodotrabajo = idmodotrabajo;
        this.modoTrabajo = modoTrabajo;
    }

    @NonNull
    public int getIdmodotrabajo() {
        return idmodotrabajo;
    }

    public void setIdmodotrabajo(@NonNull int idmodotrabajo) {
        this.idmodotrabajo = idmodotrabajo;
    }

    public int getModoTrabajo() {
        return modoTrabajo;
    }

    public void setModoTrabajo(int modoTrabajo) {
        this.modoTrabajo = modoTrabajo;
    }
}
