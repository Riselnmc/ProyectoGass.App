package com.example.proyectogassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.AuthProvider;

public class PerfilActivity extends AppCompatActivity {

    TextView nameU,lastNameU,emailU;
    String id;
    String correo;
    String clave;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        nameU = findViewById(R.id.nombreUsuario);
        lastNameU = findViewById(R.id.apellidoUsuario);
        emailU = findViewById(R.id.correoUsuario);
        getUserInfo();

        findViewById(R.id.Imgvolver).setOnClickListener(v -> {
            Intent atras = new Intent(PerfilActivity.this, MapaActivity.class);
            startActivity(atras);
            finish();
        });

        //Evento para eliminar cuenta
        findViewById(R.id.EliminarCuenta).setOnClickListener(v -> {

            startActivity(new Intent(PerfilActivity.this,EliminarCuentaActivity.class));

        });
    }

    private void getUserInfo(){
        id = mAuth.getCurrentUser().getUid();
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
