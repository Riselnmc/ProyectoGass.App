package com.example.proyectogassapp.utilidades;

public class Utilidades {

    //Constantes tabla usuario
    public static final String TABLA_USUARIO = "Usuarios";
    public static final String CAMPO_ID = "id_U";
    public static final String CAMPO_NOMBRES = "nombres";
    public static final String CAMPO_APELLIDOS = "apellidos";
    public static final String CAMPO_CORREO = "correo";
    public static final String CAMPO_CLAVE = "clave";
    public static final String CAMPO_TELEFONO = "telefono";

    //SQL TABLA USUARIO
    public static final String CREAR_TABLA_USUARIO = "create table "+TABLA_USUARIO+ "("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_NOMBRES+" TEXT,"+ CAMPO_APELLIDOS+" TEXT, "+CAMPO_CORREO+" TEXT, "+CAMPO_CLAVE+" TEXT, "+CAMPO_TELEFONO+" TEXT)";

    //Constantes tabla tarjetas
    public static final String TABLA_TARJETAS = "Tarjetas";
    public static final String ID_TARJETA = "id_T";
    public static final String TITULAR_TARJETA = "Titular";
    public static final String NUMERO_TARJETA = "NumeroTarjeta";
    public static final String MES_TARJETA = "Mes";
    public static final String YEAR_TARJETA = "Year";
    public static final String CLAVE_TARJETA = "Clave";

    //SQL TABLA TARJETA
    public static final String CREAR_TABLA_TARJETA = "create table "+TABLA_TARJETAS+ "("+ID_TARJETA+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TITULAR_TARJETA+" TEXT,"+ NUMERO_TARJETA+" TEXT, "+MES_TARJETA+" INTEGER, "+YEAR_TARJETA+" INTEGER, "+CLAVE_TARJETA+" TEXT)";

    //Constantes tabla estaciones
    public static final String TABLA_ESTACIONES = "Estaciones";
    public static final String ID_ESTACION = "id_E";
    public static final String NOMBRE_ESTACION = "NombreEstacion";
    public static final String DIRECCION_ESTACION = "DireccionEstacion";
    public static final String TELEFONO_ESTACIONES = "TelefonoEstacion";

    //SQL TABLA ESTACIÓN
    public static final String CREAR_TABLA_ESTACION = "create table "+TABLA_ESTACIONES+ "("+ID_ESTACION+" INTEGER PRIMARY KEY, "+NOMBRE_ESTACION+" TEXT,"+ DIRECCION_ESTACION+" TEXT,"+TELEFONO_ESTACIONES+" TEXT)";

    public static final String INSERTAR_ESTACIONES = "Insert into "+TABLA_ESTACIONES+" ( "+ID_ESTACION+","+NOMBRE_ESTACION+","+DIRECCION_ESTACION+","+TELEFONO_ESTACIONES+") values (1,'Esso','Buenas buenas','3054422132'),(2,'Texaco','Ven','1234567890')";
}
