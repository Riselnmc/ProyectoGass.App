//Nombre del paquete principal
package com.example.proyectogassapp;

//Importación de librerias
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//Creación de la clase con herencia del AppCompatActivity
public class TerminosYCondicionesActivity extends AppCompatActivity {

    /*
     * Ciclo de vida principal del activity
     * Aquí se ponen todos las funciones que queremos ver al iniciar el activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Obtener contenido de la vista
        setContentView(R.layout.activity_terminos_y_condiciones);
    }
}
