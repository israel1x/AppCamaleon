package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "clientemin")
public class ClienteMin {

    //@PrimaryKey(autoGenerate = true)
   // private int cid;

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @NonNull
    private String idCliente;

    @ColumnInfo(name = "name")
    private String nameCliente;

    @ColumnInfo(name = "ruc")
    private String rucCliente;

    @ColumnInfo(name = "dir")
    private String dirCliente;

    @ColumnInfo(name = "telf")
    private String telfCliente;

    @ColumnInfo(name = "lattitud")
    private String lattCliente;

    @ColumnInfo(name = "longitud")
    private String longCliente;

    @ColumnInfo(name = "fechavisita")
    private String fechaVisita;

    @ColumnInfo(name = "estadovisita")
    private int estadoVisita;


    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNameCliente() {
        return nameCliente;
    }

    public void setNameCliente(String nameCliente) {
        this.nameCliente = nameCliente;
    }

    public String getRucCliente() {
        return rucCliente;
    }

    public void setRucCliente(String rucCliente) {
        this.rucCliente = rucCliente;
    }

    public String getDirCliente() {
        return dirCliente;
    }

    public void setDirCliente(String dirCliente) {
        this.dirCliente = dirCliente;
    }

    public String getTelfCliente() {
        return telfCliente;
    }

    public void setTelfCliente(String telfCliente) {
        this.telfCliente = telfCliente;
    }

    public String getLattCliente() {
        return lattCliente;
    }

    public void setLattCliente(String lattCliente) {
        this.lattCliente = lattCliente;
    }

    public String getLongCliente() {
        return longCliente;
    }

    public void setLongCliente(String longCliente) {
        this.longCliente = longCliente;
    }

    public String getFechaVisita() {
        return fechaVisita;
    }

    public void setFechaVisita(String fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    public int getEstadoVisita() {
        return estadoVisita;
    }


    public void setEstadoVisita(int estadoVisita) {
        this.estadoVisita = estadoVisita;
    }
}
