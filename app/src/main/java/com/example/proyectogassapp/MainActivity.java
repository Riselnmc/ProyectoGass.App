//Nombre del paquete
package com.example.proyectogassapp;

//Importación de librerias
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    /*
        * Ciclo de vida principal del activity
        * Aquí se ponen todos las funciones que queremos ver al iniciar el activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
            *Controlador de tiempo para iniciar las siguiente vista
            *@Intent definir de donde va a iniciar y a que activity lo va a llevar es necesario poner @startActivity para activarlo
            *@finish no deja regresar a la activity anterior
         */
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 1500);
    }

}
