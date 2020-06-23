package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CambiarCorreoActivity extends AppCompatActivity {

    //Variable del EditText, para el nuevo correo
    private TextInputLayout nuevoCorreo;

    //Variable para el autenticador
    private FirebaseAuth mAuth;
    //Variable para la base de datos
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_correo);
        //Instanciar el autenticador
        mAuth = FirebaseAuth.getInstance();
        //Instanciar la base de datos
        db = FirebaseFirestore.getInstance();

        //Referenciar la parte lógica con la vista
        nuevoCorreo = findViewById(R.id.cambiarCorreo);
        //Variable para el botón de cambiar correo
        Button actualizar = findViewById(R.id.actualizarCorreo);

        //Evento click para el botón de cambiar correo
        actualizar.setOnClickListener(v -> actualizarCorreo());
    }

    //Metodo para cambiar el correo
    private void actualizarCorreo(){
        //Obtener información del usuario logueado
        FirebaseUser user = mAuth.getCurrentUser();
        //Obtener valor del EditText, convertirlo en minuscula y eliminar espacios
        String correo = nuevoCorreo.getEditText().getText().toString().toLowerCase().trim();
        //Obtener id del usuario logueado
        String id = user.getUid();
        //Verificar que el campo este vacio
        if (correo.isEmpty()){
            //Mostrar error
            nuevoCorreo.setError("El correo no puede estar vacio");
        }else{
            //Actualizar correo
            user.updateEmail(correo).addOnCompleteListener(task -> {
                //Verificar que la tarea fue exitosa
                if (task.isSuccessful()){
                    /*
                     * Objeto Map para guardar los datos
                     * El primer parametro es el nombre del campo
                     * El segundo es la variable que tiene el valor que queremos guardar
                     */
                    Map<String,Object> map = new HashMap<>();
                    map.put("correo",correo);
                    /*
                        * Definir colección en la que vamos a actualizar el dato
                        * @param id definir documento
                     */
                    db.collection("Usuarios").document(id).update(map).addOnCompleteListener(task1 -> {
                        //Verificar que la tarea fue exitosa
                        if (task1.isSuccessful()){
                            //Mostrar mensaje si se cambio el correo
                            Toast.makeText(this, "Tu correo ha sido cambiado con exito", Toast.LENGTH_SHORT).show();
                            //Enviar a la activity del perfil
                            startActivity(new Intent(CambiarCorreoActivity.this, PerfilActivity.class));
                            //No dejar volver a esta activity
                            finish();
                        }else {
                            //Mostrar error si el correo no se pudo cambiar
                            Toast.makeText(CambiarCorreoActivity.this, "Error al cambiar correo", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    //Mostrar error si el correo no se pudo cambiar
                    Toast.makeText(CambiarCorreoActivity.this, "Error al cambiar correo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
