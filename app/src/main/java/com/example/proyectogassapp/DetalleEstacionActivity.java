package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.proyectogassapp.entidades.Estaciones;
import com.example.proyectogassapp.entidades.Usuario;

public class DetalleEstacionActivity extends AppCompatActivity {

    private TextView tvName,tvDireccion,tvTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_estacion);
        findViewById(R.id.Imgvolver).setOnClickListener(v -> {
            Intent atras = new Intent(DetalleEstacionActivity.this, EstacionesActivity.class);
            startActivity(atras);
            finish();
        });
        tvName = findViewById(R.id.tvNameEstacion);
        tvDireccion = findViewById(R.id.tvDireccionEstacion);
        tvTelefono = findViewById(R.id.tvTelefonoEstacion);

        Bundle datosRecibidos = getIntent().getExtras();
        Estaciones estacion=null;
        if (datosRecibidos!=null){
            estacion= (Estaciones) datosRecibidos.getSerializable("Estacion");
            assert estacion != null;
            tvName.setText(estacion.getNombreEstacion());
            tvDireccion.setText(estacion.getDireccionEstacion());
            tvTelefono.setText(estacion.getTelefonoEstacion());
        }
    }
}
