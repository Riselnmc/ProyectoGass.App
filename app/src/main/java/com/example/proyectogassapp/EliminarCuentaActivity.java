//Nombre del paquete principal
package com.example.proyectogassapp;

//Importación de librerias
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class EliminarCuentaActivity extends AppCompatActivity {

    //Variable del EditText, para verificar la contraseña
    TextInputLayout claveCuenta;
    //Variable para mostrar el correo vinculado a al cuenta
    TextView correoV;
    //Variable para los botones del AlertDialog
    Button btnCancelar,btnAceptar;

    //Variable para la base de datos
    private FirebaseFirestore db;
    private FirebaseUser user;

    //Variable para el id del usuario
    private String id;
    //Variable para el correo del usuario
    String correo;
    //Variable para la clave del usuario
    private String clave;
    //Variable para comparar las claves
    private String claveC;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_cuenta);

        //Instanciar autenticador
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //Obtener información del usuario logueado
        user = mAuth.getCurrentUser();

        //Referenciar  la parte lógica con la vista
        correoV = findViewById(R.id.correoVinculado);
        claveCuenta = findViewById(R.id.claveCuenta);

        //Obtener id del usuario logueado
        id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        /*
            * Traer información del usuario
            * @param id para traer la información del usuario logueado
         */
        db.collection("Usuarios").document(id).get().addOnSuccessListener(documentSnapshot -> {
            //Verificar que existan datos
            if (documentSnapshot.exists()){
                //Obtener clave del usaurio
                clave = documentSnapshot.getString("clave");
            }
        });

        //Evento click para volver al perfil del usuario
        findViewById(R.id.Imgvolver).setOnClickListener(v -> {
            startActivity(new Intent(EliminarCuentaActivity.this, PerfilActivity.class));
        });

        //Obtener correo del usuario logueado
        correo = user.getEmail();
        //Dar valor al TextView
        correoV.setText("¿Deseas eliminar la cuenta actual de Gass.app vinculada al correo "+correo+"?");

        //Evento click para el botón de eliminar cuenta
        findViewById(R.id.btn_EliminarCuenta).setOnClickListener(v -> {
            //Verificar que la clave este vacia
            if (clave.isEmpty()){
                //Mostrar error
                claveCuenta.setError("Debes ingresar la contraseña");
            }else {
                //No mostrar error
                claveCuenta.setError(null);
                //Obtener valor del EditText y eliminar espacios
                claveC = claveCuenta.getEditText().getText().toString().trim();
                //Verificar que las claves sean iguales
                if (claveC.equals(clave)){
                    //Usar metodo para eliminar usuario
                    eliminarUsuario();
                    //No mostrar error
                    claveCuenta.setError(null);
                }else {
                    //Mostrar error
                    claveCuenta.setError("Contraseña incorrecta");
                }
            }
        });
    }

    //Metodo para eliminar un usuario
    private void eliminarUsuario(){

        //Varible de AlertDialog.Builder para construir e instanciar el objeto
        AlertDialog.Builder builder = new AlertDialog.Builder(EliminarCuentaActivity.this);

        LayoutInflater inflater = getLayoutInflater();

        //Obtener AlertDialog personalizado con el layout especificado
        View view = inflater.inflate(R.layout.item_alert_advertencia, null);

        //Establecer vista personalizada
        builder.setView(view);

        //Variable de AlertDialog para construir y crear la vista
        AlertDialog dialog = builder.create();
        //Mostrar AlertDialog
        dialog.show();

        //Referenciar la parte lógica con la vista
        btnAceptar = view.findViewById(R.id.btn_aceptar);
        btnCancelar = view.findViewById(R.id.btn_cancelar);

        //Evento click para el botón cancelar, cerrará el AlertDialog
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        //Evento click para el botón aceptar
        btnAceptar.setOnClickListener(v -> {
            //Obtener credencial del usuario logueado
            AuthCredential credential = EmailAuthProvider.getCredential(correo,clave);
            /*
                * Reautenticar el usuario
                * @param credential credencial del usuario logueado
                * Si la tarea es exitosa eliminará al usuario autenticado
             */
            user.reauthenticate(credential).addOnCompleteListener(task -> user.delete().addOnCompleteListener(task1 -> {
                //Verificar que la tarea fue exitosa
                if (task1.isSuccessful()){
                    /*
                        * Eliminar usuario de la base de datos
                        * @param id para eliminar el usuario logueado
                     */
                    db.collection("Usuarios").document(id)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Query query = db.collection("Tarjetas").whereEqualTo("IdUsuario",id);

                            });
                }
                //Mostrar error si no se logra autenticar
            })).addOnFailureListener(e -> Toast.makeText(EliminarCuentaActivity.this, "Error al autenticar", Toast.LENGTH_SHORT).show());
        });
    }

}
