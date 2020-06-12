package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //Variables para los componentes de la vista
    EditText correo,pass;
    TextInputLayout problemaCorreo,problemaClave;
    TextView recuperar;

    //Variables para guardar los datos del usuario
    private String email = "";
    private String password = "";

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instanciar autentificador
        mAuth = FirebaseAuth.getInstance();

        //Referenciar la parte lógica con la vista
        correo = findViewById(R.id.correoE);
        pass = findViewById(R.id.password);
        recuperar = findViewById(R.id.recuperarClave);
        problemaCorreo = findViewById(R.id.correoE1);
        problemaClave = findViewById(R.id.password1);

        //Evento para cuando el usuario presione el @TextView
        recuperar.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this,RestablecerClaveActivity.class)));

    }

    //Metodo para ir a la activity registrar
    public void moveRegistrar(View view){
        startActivity(new Intent(LoginActivity.this, RegistrarActivity.class));
    }

    //Metodo para el botón de iniciar sesión
    public void confirmarInicio(View view){
        //Dar valor de los @EditText a las variables y borrar los espacios
        email = correo.getText().toString().toLowerCase().trim();
        password = pass.getText().toString().trim();

        //Verificar que el campo de correo esta vacio
        if (email.isEmpty()){
            problemaCorreo.setError("El correo no puede estar vacio");
        }else {
            //No mostrar el error
            problemaCorreo.setError(null);
        }

        //Verificar que el campo de contraseña este vacio
        if (password.isEmpty()){
            problemaClave.setError("La contraseña no puede estar vacia");
        }else {
            problemaClave.setError(null);
        }

        //Verificar que los campos no esten vacios
        if (!email.isEmpty() && !password.isEmpty()){
            inicioSesion();
        }
    }

    //Metodo para iniciar sesión
    private void inicioSesion(){
        //Metodo para iniciar con correo y contraseña
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            //Si la tarea fue exitosa me enviará al mapa y no dejará volver al login
            if (task.isSuccessful()){
                startActivity(new Intent(LoginActivity.this, MapaActivity.class));
                finish();
            }else {
                //Mensaje @Toast si los datos no están en la base de datos
                Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Ciclo de vida del activity, se usa cuando vuelve a iniciarse
    @Override
    protected void onStart() {
        super.onStart();
        //Verificar si el usuario ya inicio sesión
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MapaActivity.class));
            finish();
        }
    }
}
