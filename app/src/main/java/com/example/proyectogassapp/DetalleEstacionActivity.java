package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.proyectogassapp.entidades.Estaciones;
import com.example.proyectogassapp.entidades.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetalleEstacionActivity extends AppCompatActivity {

    private TextView tvName,tvDireccion,tvTelefono;

    private String idEstacion;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_estacion);

        db = FirebaseFirestore.getInstance();

        findViewById(R.id.Imgvolver).setOnClickListener(v -> {
            Intent atras = new Intent(DetalleEstacionActivity.this, EstacionesActivity.class);
            startActivity(atras);
            finish();
        });

        idEstacion = getIntent().getStringExtra("idEstacion");

        tvName = findViewById(R.id.tvNameEstacion);
        tvDireccion = findViewById(R.id.tvDireccionEstacion);
        tvTelefono = findViewById(R.id.tvTelefonoEstacion);
        mostrarInformacion();
    }

    private void mostrarInformacion(){

        db.collection("Estaciones").document(idEstacion).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                String nombreEstacion = documentSnapshot.getString("NombreEstacion");
                String direccionEstacion = documentSnapshot.getString("DireccionEstacion");
                String telefonoEstacion = documentSnapshot.getString("TelefonoEstacion");
                tvName.setText(nombreEstacion);
                tvDireccion.setText(direccionEstacion);
                tvTelefono.setText(telefonoEstacion);
            }
        });

    }
}
