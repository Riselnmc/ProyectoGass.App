package com.example.proyectogassapp.entidades;

import java.io.Serializable;

public class Servicios implements Serializable {

    private String Nombre;
    private Integer Valor;
    private String idEstacion;

    public Servicios() {
    }

    public Servicios(String nombre, Integer valor, String idEstacion) {
        this.Nombre = nombre;
        this.Valor = valor;
        this.idEstacion = idEstacion;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Integer getValor() {
        return Valor;
    }

    public void setValor(Integer valor) {
        Valor = valor;
    }

    public String getIdEstacion() {
        return idEstacion;
    }

    public void setIdEstacion(String idEstacion) {
        this.idEstacion = idEstacion;
    }
}
