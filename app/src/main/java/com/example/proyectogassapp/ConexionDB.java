package com.example.proyectogassapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.proyectogassapp.utilidades.Utilidades;


public class ConexionDB extends SQLiteOpenHelper {

    private static final String NAME_DATABASE = "GassApp.db";

    ConexionDB(Context context) {
        super(context, NAME_DATABASE, null, 1);
    }


    boolean validarcorreo(String correo){
        String sql = "Select count(*) from "+Utilidades.TABLA_USUARIO+" where "+Utilidades.CAMPO_CORREO+"='"+correo+"'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        long valor = statement.simpleQueryForLong();
        statement.close();
        return valor == 1;
    }

    @Override
    public void onCreate(SQLiteDatabase GassApp) {
        GassApp.execSQL(Utilidades.CREAR_TABLA_USUARIO);
        GassApp.execSQL(Utilidades.CREAR_TABLA_TARJETA);
        GassApp.execSQL(Utilidades.CREAR_TABLA_ESTACION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase GassApp, int versionAntigua, int versionNueva) {
        GassApp.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_USUARIO);
        GassApp.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_TARJETAS);
        GassApp.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_ESTACIONES);
        onCreate(GassApp);
    }
}

