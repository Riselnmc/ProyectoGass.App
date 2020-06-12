package com.example.proyectogassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CambiarCorreoActivity extends AppCompatActivity {

    TextInputLayout nuevoCorreo;
    Button actualizar;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_correo);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nuevoCorreo = findViewById(R.id.cambiarCorreo);
        actualizar = findViewById(R.id.actualizarCorreo);

        actualizar.setOnClickListener(v -> actualizarCorreo());
    }

    private void actualizarCorreo(){
        mUser = mAuth.getCurrentUser();
        String correo = nuevoCorreo.getEditText().getText().toString().toLowerCase().trim();
        String id = mUser.getUid();
        if (correo.isEmpty()){
            nuevoCorreo.setError("El correo no puede estar vacio");
        }else{
            mUser.updateEmail(correo).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Map<String,Object> map = new HashMap<>();
                    map.put("correo",correo);
                    db.collection("Usuarios").document(id).update(map).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()){

                        }else {
                            Toast.makeText(CambiarCorreoActivity.this, "Error al cambiar correo", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    Toast.makeText(CambiarCorreoActivity.this, "Error al cambiar correo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
