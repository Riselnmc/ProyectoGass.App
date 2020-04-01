package com.example.proyectogassapp.entidades;

import java.io.Serializable;

public class Estaciones implements Serializable {

    private int id_E;
    private String NombreEstacion;
    private String DireccionEstacion;
    private String TelefonoEstacion;

    public Estaciones(){

    }

    public Estaciones(int id_E, String nombreEstacion, String direccionEstacion, String telefonoEstacion) {
        this.id_E = id_E;
        NombreEstacion = nombreEstacion;
        DireccionEstacion = direccionEstacion;
        TelefonoEstacion = telefonoEstacion;
    }

    public int getId_E() {
        return id_E;
    }

    public void setId_E(int id_E) {
        this.id_E = id_E;
    }

    public String getNombreEstacion() {
        return NombreEstacion;
    }

    public void setNombreEstacion(String nombreEstacion) {
        NombreEstacion = nombreEstacion;
    }

    public String getDireccionEstacion() {
        return DireccionEstacion;
    }

    public void setDireccionEstacion(String direccionEstacion) {
        DireccionEstacion = direccionEstacion;
    }

    public String getTelefonoEstacion() {
        return TelefonoEstacion;
    }

    public void setTelefonoEstacion(String telefonoEstacion) {
        TelefonoEstacion = telefonoEstacion;
    }
}
