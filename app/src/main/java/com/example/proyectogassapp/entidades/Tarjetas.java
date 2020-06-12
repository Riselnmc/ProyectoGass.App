package com.example.proyectogassapp.entidades;

import java.io.Serializable;

public class Tarjetas implements Serializable {

    private String Titular;
    private String NumeroTarjeta;
    private String FechaVencimiento;
    private String Clave;
    private String IdUsuario;

    public Tarjetas(){

    }

    public Tarjetas(String titular, String numeroTarjeta, String fechaVencimiento, String clave, String idUsuario) {
        Titular = titular;
        NumeroTarjeta = numeroTarjeta;
        FechaVencimiento = fechaVencimiento;
        Clave = clave;
        IdUsuario = idUsuario;
    }

    public String getTitular() {
        return Titular;
    }

    public void setTitular(String titular) {
        Titular = titular;
    }

    public String getNumeroTarjeta() {
        return NumeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        NumeroTarjeta = numeroTarjeta;
    }

    public String getFechaVencimiento() {
        return FechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) { FechaVencimiento = fechaVencimiento; }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public String getIdUsuario() { return IdUsuario; }

    public void setIdUsuario(String idUsuario) { IdUsuario = idUsuario; }
}
