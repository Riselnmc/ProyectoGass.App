//Nombre del paquete principal
package com.example.proyectogassapp;

//Importación de librerias
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

//Creación de la clase con herencia del AppCompatActivity
public class LoginActivity extends AppCompatActivity {

    //Variables para los componentes de la vista
    private TextInputLayout correo,pass;

    //Variables para iniciar sesión
    private String email = "";
    private String password = "";

    //Variable para el autenticador
    private FirebaseAuth mAuth;

    /*
     * Ciclo de vida principal del activity
     * Se activa cuando el sistema crea la actividad por primera vez, se ejecuta la lógica básica
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamar la super clase onCreate para completar la creación de la activity
        super.onCreate(savedInstanceState);
        //Establecer el diseño de la interfaz de usuario para esta activity
        setContentView(R.layout.activity_login);

        //Instanciar autentificador
        mAuth = FirebaseAuth.getInstance();

        //Referenciar la parte lógica con la vista
        correo = findViewById(R.id.correoE);
        pass = findViewById(R.id.password);
        TextView recuperar = findViewById(R.id.recuperarClave);

        //Evento click para ir a la activity de restablecer contraseña
        recuperar.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this,RestablecerClaveActivity.class)));

    }

    //Metodo para ir a la activity registrar
    public void moveRegistrar(View view){
        startActivity(new Intent(LoginActivity.this, RegistrarActivity.class));
    }

    /*
        * Metodo para el botón de iniciar sesión
        * Se debe crear con un atributo tipo View para relacionarlo con la vista
     */
    public void confirmarInicio(View view){
        // Obtener valor del campo de texto borrar los espacios
        email = correo.getEditText().getText().toString().toLowerCase().trim();
        password = pass.getEditText().getText().toString().trim();

        //Verificar que el campo de correo esta vacio
        if (email.isEmpty()){
            correo.setError("El correo no puede estar vacio");
        }else {
            //No mostrar el error
            correo.setError(null);
        }

        //Verificar que el campo de contraseña este vacio
        if (password.isEmpty()){
            pass.setError("La contraseña no puede estar vacia");
        }else {
            pass.setError(null);
        }

        //Verificar que los campos no esten vacios
        if (!email.isEmpty() && !password.isEmpty()){
            //Usar metodo
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

    /*
        * Hace que el usuario pueda ver la activity mientras la app se prepara para que esta entre
        * en primer plano y se convierta en interactiva
     */
    @Override
    protected void onStart() {
        super.onStart();
        //Verificar si el usuario ya inicio sesión
        if (mAuth.getCurrentUser() != null){
            //Enviar al mapa y no dejar volver al login
            startActivity(new Intent(LoginActivity.this, MapaActivity.class));
            finish();
        }
    }
}
