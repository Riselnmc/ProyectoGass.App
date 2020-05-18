package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //Variables para los componentes de la vista
    EditText correo,pass;
    Button btnIniciar,registrar;
    TextView recuperar;

    //Variables para la base de datos
    private String email = "";
    private String password = "";

    //Creación de la variable
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instanciar autentificador en la variable
        mAuth = FirebaseAuth.getInstance();

        //Referenciar las variables con la vista
        correo = findViewById(R.id.correoE);
        pass = findViewById(R.id.password);
        registrar = findViewById(R.id.register);
        recuperar = findViewById(R.id.recuperarClave);

        //@setOnClickListener evento para cuando el usuario presione el componente
        recuperar.setOnClickListener(v -> {
            Intent moveRestablecer = new Intent(LoginActivity.this, RestablecerClaveActivity.class);
            startActivity(moveRestablecer);
        });

        registrar.setOnClickListener(v -> {
            Intent moveRegistrar = new Intent(LoginActivity.this, RegistrarActivity.class);
            startActivity(moveRegistrar);
        });

        btnIniciar = findViewById(R.id.btnLogin);
        btnIniciar.setOnClickListener(v -> {
            /*
                * Darle valor a las variables
                * @trim sirve para borrar los espacios en blanco
                * @toLoweCase convierte el texto en minuscula
             */
            email = correo.getText().toString().trim().toLowerCase();
            password = pass.getText().toString().trim();

            /*
                * Condicional para verificar que los campos no esten vacios
                * De lo contrario mostrará un error y no dejará seguir
             */
            if (!email.isEmpty() && !password.isEmpty()){

                loginUser();

            }else if (email.isEmpty()){
                correo.setError("El correo no puede estar vacio");
            }else if (password.isEmpty()){
                pass.setError("La contraseña no puede estar vacia");
            }

        });
    }

    //Metodo para verificar si los datos del usuario se encuentran en la base de datos
    private void loginUser(){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                startActivity(new Intent(LoginActivity.this, MapaActivity.class));
                finish();
            }else {
                Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Se usa cuando el activity vuelve a iniciarse
    @Override
    protected void onStart() {
        super.onStart();
        //Condición para verificar si el usuario ya inicio sesión
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MapaActivity.class));
            finish();
        }
    }

}
