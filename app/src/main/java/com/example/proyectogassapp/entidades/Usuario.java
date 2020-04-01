package com.example.proyectogassapp.entidades;

import java.io.Serializable;

public class Usuario implements Serializable {

    private Integer id_U;
    private String nombres;
    private String apellidos;
    private String correo;
    private String clave;
    private String telefono;

    public Usuario(){

    }

    public Usuario(Integer id_U, String nombres, String apellidos, String correo, String clave, String telefono) {
        this.id_U = id_U;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.clave = clave;
        this.telefono = telefono;
    }

    public Integer getId_U() {
        return id_U;
    }

    public void setId_U(Integer id_U) {
        this.id_U = id_U;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
