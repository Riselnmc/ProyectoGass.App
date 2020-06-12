package com.example.proyectogassapp.entidades;

import java.io.Serializable;

public class Estaciones implements Serializable {

    private String NombreEstacion;
    private String DireccionEstacion;
    private String TelefonoEstacion;
    private Double Latitud;
    private Double Longitud;
    private String IdServicio;

    public Estaciones(){

    }

    public Estaciones(String nombreEstacion, String direccionEstacion, String telefonoEstacion, Double latitud, Double longitud, String idServicio) {
        NombreEstacion = nombreEstacion;
        DireccionEstacion = direccionEstacion;
        TelefonoEstacion = telefonoEstacion;
        Latitud = latitud;
        Longitud = longitud;
        IdServicio = idServicio;
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

    public String getIdServicio() {
        return IdServicio;
    }

    public void setIdServicio(String idServicio) {
        IdServicio = idServicio;
    }
}
