package com.example.proyectogassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CambiarClaveActivity extends AppCompatActivity {

    //Variable de los EditText, para la clave actual, la nueva y la confirmación de la calve
    private TextInputLayout claveActual,claveNueva,confirmarClaveNueva;
    //Variable para la base de datos
    private FirebaseFirestore db;
    //Variable para el autenticador
    private FirebaseAuth mAuth;

    private FirebaseUser user;

    //Variable para el id del usuario
    String id;
    //Variable para la clave que esta en la base de datos
    String clave;
    //Variable para confirmar la nueva clave
    String confimarClave;
    //Variable para la nueva clave
    String claveN;
    //Variable para la clave actual
    String claveA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_clave);

        //Instanciar autenticador
        mAuth = FirebaseAuth.getInstance();
        //Instanciar base de datos
        db = FirebaseFirestore.getInstance();
        //Obtener información del usuario logueado
        user = mAuth.getCurrentUser();

        //Referenciar la parte lógica con la vista
        claveActual = findViewById(R.id.claveActual);
        claveNueva = findViewById(R.id.claveNueva);
        confirmarClaveNueva = findViewById(R.id.confirmarClaveNueva);
        //Botón para cambiar la clave
        Button btnCambiarClave = findViewById(R.id.actualizarClave);

        //Obtener id del usuario logueado
        id = user.getUid();

        //Evento click para el botón de cambiar clave
        btnCambiarClave.setOnClickListener(v -> cambiarClave());
    }

    //Metodo para cambiar la clave
    private void cambiarClave(){
        //Obtener valor del campo de texto y eliminar espacios
        claveA = claveActual.getEditText().getText().toString().trim();
        //Verificar que el campo este vacio
        if (claveA.isEmpty()){
            //Mostrar error
            claveActual.setError("Debes ingresar la contraseña actual");
        }else {
            //No mostrar error
            claveActual.setError(null);
            //Obtener la información del usuario logueado
            db.collection("Usuarios").document(id).get().addOnSuccessListener(documentSnapshot -> {
                //Obtener clave del usuario en la base de datos
                clave = documentSnapshot.getString("clave");
                //Verificar que la clave actual es igual a la que esta en la base de datos
                if (claveA.equals(clave)){
                    //No mostrar error
                    claveActual.setError(null);
                    //Obtener valor de los campos de texto y eliminar espacios
                    claveN = claveNueva.getEditText().getText().toString().trim();
                    confimarClave = confirmarClaveNueva.getEditText().getText().toString().trim();
                    //Verificar que la clave nueva tenga 8 o más caracteres
                    if (claveN.length() >= 8){
                        //No mostrar error
                        claveNueva.setError(null);
                        //Verificar que la confirmación de la clave este vacia
                        if (confimarClave.isEmpty()){
                            //Mostar error
                            confirmarClaveNueva.setError("Debes confirmar la nueva contraseña");
                        }else {
                            //No mostrar error
                            confirmarClaveNueva.setError(null);
                            //Verificar que la clave nueva y la de confirmar sean iguales
                            if (claveN.equals(confimarClave)){
                                //No mostrar error
                                confirmarClaveNueva.setError(null);
                                /*
                                    * Actualizar contraseña
                                    * @param claveN nueva contraseña
                                 */
                                user.updatePassword(claveN).addOnCompleteListener(task -> {
                                    //Verificar que la tarea fue exitosa
                                    if (task.isSuccessful()){
                                        /*
                                         * Objeto Map para guardar los datos
                                         * El primer parametro es el nombre del campo
                                         * El segundo es la variable que tiene el valor que queremos guardar
                                         */
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("clave",claveN);
                                        /*
                                         * Definir colección en la que vamos a actualizar el dato
                                         * @param id definir documento
                                         */
                                        db.collection("Usuarios").document(id).update(map).addOnCompleteListener(task1 -> {
                                            //Verificar que la tarea fue exitosa
                                            if (task1.isSuccessful()){
                                                //Mostrar mensaje
                                                Toast.makeText(this, "Se ha cambiado la contraseña", Toast.LENGTH_SHORT).show();
                                                //Cerrar sesión
                                                mAuth.signOut();
                                                //Enviar al login
                                                startActivity(new Intent(CambiarClaveActivity.this, LoginActivity.class));
                                                //No dejar volver a esta activity
                                                finish();
                                            }else {
                                                //Mostrar error
                                                Toast.makeText(this, "No se pudo cambiar la contraseña", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else {
                                        //Mostrar error
                                        Toast.makeText(this, "No se pudo cambiar la contraseña", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                //Mostrar error
                                confirmarClaveNueva.setError("Las contraseñas no coinciden");
                            }
                        }
                    }else {
                        //Mostrar error
                        claveNueva.setError("La contraseña debe ser menor o igual a 8 caracteres");
                    }
                }else {
                    //Mostrar error
                    claveActual.setError("Contraseña incorrecta");
                }
            });
        }
    }
}
