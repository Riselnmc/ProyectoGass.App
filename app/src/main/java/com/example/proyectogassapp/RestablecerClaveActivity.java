package com.example.proyectogassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RestablecerClaveActivity extends AppCompatActivity {

    private EditText emailU;
    private Button btnReset;

    //Variable que contiene el email ingresado
    private String email = "";

    //Variable para la autentificación del usuario
    FirebaseAuth mAuth;

    //Variable para mostrar mensaje
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer_clave);

        //Instanciar autentificación de usuario
        mAuth = FirebaseAuth.getInstance();

        //Igualar
        mDialog = new ProgressDialog(this);

        emailU = findViewById(R.id.emailUsuario);
        btnReset = findViewById(R.id.restablecerClave);

        btnReset.setOnClickListener(v -> {

            //Darle a la varible los valores ingresados en el campo de texto
            email = emailU.getText().toString().toLowerCase().trim();

            //Condición de verificación para saber si el campo esta vacio o no
            if (!email.isEmpty()){
                mDialog.setMessage("Espere un momento...");
                //No permite que el usuario le de click en otro lugar mientras el mensaje se muestra
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();
                //Utilizar metodo de enviar correo
                enviarCorreo();
            }else{
                Toast.makeText(RestablecerClaveActivity.this, "El campo es obligatorio", Toast.LENGTH_SHORT).show();
            }

        });
    }

    //Metodo para enviar correo electronico del cambio de contraseña
    private void enviarCorreo(){
        //Definir el lenguaje del mensaje que se enviara al correo
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(RestablecerClaveActivity.this, "Correo enviado con exito", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(RestablecerClaveActivity.this, "No se pudo enviar el correo", Toast.LENGTH_SHORT).show();
            }
            //Cerrar mensaje
            mDialog.dismiss();
        });
    }

}
