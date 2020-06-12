package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class EliminarCuentaActivity extends AppCompatActivity {

    TextView correoV;
    String id;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_cuenta);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        correoV = findViewById(R.id.correoVinculado);
        getCorreo();
    }

    private void getCorreo(){
        id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        db.collection("Usuarios").document(id).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                String correo = documentSnapshot.getString("correo");
                correoV.setText("¿Deseas eliminar la cuenta de Gass.App vinculada al correo "+correo+"?");
            }
        });
    }
}
