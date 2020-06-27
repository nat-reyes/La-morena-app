package com.example.lamorena.Entities;

import java.io.Serializable;

public class Servicio implements Serializable {
    private String nombre,precio;

    public Servicio(String nombre, String precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
    public Servicio(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
