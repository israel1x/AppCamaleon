package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "usuario")
public class Usuario {

    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "vendedor")
    private String nameVendedor;

    @NonNull
    @ColumnInfo(name = "user")
    private String user;

    @NonNull
    @ColumnInfo(name = "pass")
    private String password;

    @ColumnInfo(name = "token")
    private String token;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public String getUser() {
        return user;
    }

    public void setUser(@NonNull String user) {
        this.user = user;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @NonNull
    public String getNameVendedor() {
        return nameVendedor;
    }

    public void setNameVendedor(@NonNull String nameVendedor) {
        this.nameVendedor = nameVendedor;
    }
}
