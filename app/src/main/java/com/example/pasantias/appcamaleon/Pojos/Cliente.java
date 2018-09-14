package com.example.pasantias.appcamaleon.Pojos;

import android.widget.Filter;
import android.widget.Filterable;

public class Cliente implements Filterable{

    public int id_cliente;
    public String ruc;
    public int id_persona;
    public String nombre;
    public String direccion;
    public String telefono;
    public Double latC;
    public Double lngC;

    public int estado;

    public Cliente() {
    }

    public Cliente(String ruc, String nombre, String direccion) {
        this.ruc = ruc;
        this.nombre = nombre;
        this.direccion = direccion;
    }


    public Cliente(String ruc, String nombre, String direccion, String telefono) {
        this.ruc = ruc;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public Cliente(int id_cliente,String ruc, String nombre, String direccion, String telefono, Double latC, Double lngC) {
        this.id_cliente=id_cliente;
        this.ruc = ruc;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.latC = latC;
        this.lngC = lngC;
    }

    public Cliente(int id_cliente,String ruc, String nombre, String direccion, String telefono, Double latC, Double lngC,int estado) {
        this.id_cliente=id_cliente;
        this.ruc = ruc;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.latC = latC;
        this.lngC = lngC;
        this.estado=estado;
    }

    public Cliente(Cliente cliente) {
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Double getLatC() {
        return latC;
    }

    public void setLatC(Double latC) {
        this.latC = latC;
    }

    public Double getLngC() {
        return lngC;
    }

    public void setLngC(Double lngC) {
        this.lngC = lngC;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
