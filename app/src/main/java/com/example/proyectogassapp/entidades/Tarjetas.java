package com.example.proyectogassapp.entidades;

import java.io.Serializable;

public class Tarjetas implements Serializable {

    private String id_T;
    private String Titular;
    private String NumeroTarjeta;
    private String FechaVencimiento;
    private String Clave;

    public Tarjetas(){

    }

    public Tarjetas(String id_T, String titular, String numeroTarjeta, String fechaVencimiento, String clave) {
        this.id_T = id_T;
        Titular = titular;
        NumeroTarjeta = numeroTarjeta;
        FechaVencimiento = fechaVencimiento;
        Clave = clave;
    }

    public String getId_T() {
        return id_T;
    }

    public void setId_T(String id_T) {
        this.id_T = id_T;
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

    public void setFechaVencimiento(String fechaVencimiento) {
        FechaVencimiento = fechaVencimiento;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }
}
