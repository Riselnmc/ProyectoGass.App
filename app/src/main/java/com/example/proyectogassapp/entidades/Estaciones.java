package com.example.proyectogassapp.entidades;

import java.io.Serializable;

public class Estaciones implements Serializable {

    private String id_E;
    private String NombreEstacion;
    private String DireccionEstacion;
    private String TelefonoEstacion;
    private Double Latitud;
    private Double Longitud;

    public Estaciones(){

    }

    public Estaciones(String id_E, String nombreEstacion, String direccionEstacion, String telefonoEstacion, Double latitud, Double longitud) {
        this.id_E = id_E;
        NombreEstacion = nombreEstacion;
        DireccionEstacion = direccionEstacion;
        TelefonoEstacion = telefonoEstacion;
        Latitud = latitud;
        Latitud = longitud;
    }

    public Double getLatitud() {
        return Latitud;
    }

    public void setLatitud(Double latitud) {
        Latitud = latitud;
    }

    public Double getLongitud() {
        return Longitud;
    }

    public void setLongitud(Double longitud) {
        Longitud = longitud;
    }

    public String getId_E() {
        return id_E;
    }

    public void setId_E(String id_E) {
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
