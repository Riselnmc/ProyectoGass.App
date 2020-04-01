package com.example.proyectogassapp.entidades;

import java.io.Serializable;

public class Tarjetas implements Serializable {

    private Integer id_T;
    private String Titular;
    private String NumeroTarjeta;
    private String Mes;
    private String Year;
    private String Clave;

    public Tarjetas(){

    }

    public Tarjetas(Integer id_T, String titular, String numeroTarjeta, String mes, String year, String clave) {
        this.id_T = id_T;
        Titular = titular;
        NumeroTarjeta = numeroTarjeta;
        Mes = mes;
        Year = year;
        Clave = clave;
    }

    public Integer getId_T() {
        return id_T;
    }

    public void setId_T(Integer id_T) {
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

    public String getMes() {
        return Mes;
    }

    public void setMes(String mes) {
        Mes = mes;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }
}
