package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class PerfilActivity extends AppCompatActivity {

    TextView nameU,lastNameU,emailU;
    String id;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameU = findViewById(R.id.nombreUsuario);
        lastNameU = findViewById(R.id.apellidoUsuario);
        emailU = findViewById(R.id.correoUsuario);
        getUserInfo();

        findViewById(R.id.Imgvolver).setOnClickListener(v -> {
            startActivity(new Intent(PerfilActivity.this, MapaActivity.class));
        });

        findViewById(R.id.moveCambiarCorreo).setOnClickListener(v -> {
            startActivity(new Intent(PerfilActivity.this, CambiarCorreoActivity.class));
        });

        findViewById(R.id.EliminarCuenta).setOnClickListener(v -> {

            startActivity(new Intent(PerfilActivity.this,EliminarCuentaActivity.class));

        });
    }

    private void getUserInfo(){
        id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        db.collection("Usuarios").document(id).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                String nombre = documentSnapshot.getString("nombres");
                String apellido = documentSnapshot.getString("apellidos");
                String correo = documentSnapshot.getString("correo");
                nameU.setText(nombre);
                lastNameU.setText(apellido);
                emailU.setText(correo);
            }
        });
    }

}
