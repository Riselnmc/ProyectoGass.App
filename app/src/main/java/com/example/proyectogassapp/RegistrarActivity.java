//Nombre del paquete principal
package com.example.proyectogassapp;

//Importación de librerias
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//Creación de la clase con herencia del AppCompatActivity
public class RegistrarActivity extends AppCompatActivity {

    //Variables de los EditText, para la información que vamos a registrar
    private TextInputLayout nombres,apellidos,correo, passw,confirmPass, tele;

    //Variables para guardar los valores en la base de datos
    private String name = "";
    private String lastName = "";
    private String email = "";
    private String password = "";
    private String phone = "";

    //Variable para el autenticador
    private FirebaseAuth mAuth;
    //Variable para la base de datos
    private FirebaseFirestore db;

    //Variable para el cuadro de dialogo
    private ProgressDialog pd;

    /*
     * Ciclo de vida principal del activity
     * Se activa cuando el sistema crea la actividad por primera vez, se ejecuta la lógica básica
     * @SuppressLint("SetTextI18n") Permite utilizar cadenas de texto en el setText
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamar la super clase onCreate para completar la creación de la activity
        super.onCreate(savedInstanceState);
        //Establecer el diseño de la interfaz de usuario para esta activity
        setContentView(R.layout.activity_registrar);

        //Instanciar autenticador
        mAuth = FirebaseAuth.getInstance();
        //Instanciar base de datos
        db = FirebaseFirestore.getInstance();
        //Instanciar cuadro de dialogo
        pd = new ProgressDialog(this);

        //Referenciar la parte lógica con la vista
        nombres = findViewById(R.id.txtNombres1);
        apellidos = findViewById(R.id.txtApellidos1);
        correo = findViewById(R.id.txtCorreo1);
        passw = findViewById(R.id.txtPassword1);
        confirmPass = findViewById(R.id.txtPasswordConfirm1);
        tele = findViewById(R.id.txtTel1);
        ImageView volver = findViewById(R.id.volver);

        //Evento click para volver al login
        volver.setOnClickListener(v -> {
            startActivity(new Intent(RegistrarActivity.this, LoginActivity.class));
        });

    }

    //Metodo para el botón de registro, se debe crear con un atributo tipo @View para relacionarlo con la vista
    public void confirmarRegistro(View view){
        /*
         * Recibir valor del campo de texto
         * Dar valor a la variable
         */
        name = nombres.getEditText().getText().toString();
        lastName = apellidos.getEditText().getText().toString();
        // Y borrar espacios
        email = correo.getEditText().getText().toString().toLowerCase().trim();
        password = passw.getEditText().getText().toString().trim();
        String passwordC = confirmPass.getEditText().getText().toString().trim();
        phone = tele.getEditText().getText().toString().trim();

        //Verificar si el campo esta vacio
        if (name.isEmpty() ) {
            //Mostrar error
            nombres.setError("El nombre no puede estar vacio");
        }else {
            //No mostrar el error
            nombres.setError(null);
        }

        if (lastName.isEmpty()){
            apellidos.setError("El apellido no puede estar vacio");
        }else {
            apellidos.setError(null);
        }

        if (email.isEmpty()){
            correo.setError("El correo no puede estar vacio");
        } else {
            correo.setError(null);
        }

        if (password.isEmpty()){
            passw.setError("La contraseña no puede estar vacia");
        } else {
            passw.setError(null);
        }

        if (passwordC.isEmpty()){
            confirmPass.setError("Este campo es obligatiorio");
        }else {
            confirmPass.setError(null);
        }

        if (phone.isEmpty()){
            tele.setError("El teléfono no puede estar vacio");
        }else {
            tele.setError(null);
        }

        //Verificar que los campos no estén vacios
        if (!name.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !passwordC.isEmpty() && !phone.isEmpty()){
            //Validar correo
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                //Verificar que la contraseña sea mayor o igual a 8 caracteres
                if (password.length()>=8){
                    //Verificar que las contraseñas sean iguales
                    if (passwordC.equals(password)){
                        registrarUsuario();
                    }else {
                        //Si las contraseñas no son iguales muestra error
                        confirmPass.setError("Las contraseñas no coinciden");
                    }
                }else {
                    //Si la contraseña no cumple los requisitos muestra error
                    confirmPass.setError("La contraseña debe ser mayor o igual a 8 caracteres");
                }
            }else {
                //Si la dirección de correo es invalida muestra error
                correo.setError("La dirección de correo no es valida");
            }
        }
    }

    //Metodo para registrar un nuevo usuario
    private void registrarUsuario(){
        //Mensaje que queremos mostrar en el cuadro de dialogo
        pd.setMessage("Registrando usuario...");
        //No permite dar click en otro lugar de la pantalla mientras el cuadro se muestra
        pd.setCanceledOnTouchOutside(false);
        //Mostrar cuadro de dialogo
        pd.show();

        //Crear nuevo usuario con correo y contraseña
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            //Verificar que la tarea fue exitosa
            if (task.isSuccessful()){
                /*
                    * Objeto Map para guardar los datos
                    * El primer parametro es el nombre del campo
                    * El segundo es la variable que tiene el valor que queremos guardar
                 */
                Map<String, Object>map = new HashMap<>();
                map.put("nombres", name);
                map.put("apellidos", lastName);
                map.put("correo", email);
                map.put("clave", password);
                map.put("telefono", phone);

                //Obtener el id del usuario
                String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                /*
                    * Definir en que colección se guardarán los datos
                    * @param id Definir clave primaria del documento
                    * Se ingresa el nombre que le dimos a la variable tipo Map para guardar todos los datos
                 */
                db.collection("Usuarios").document(id).set(map).addOnCompleteListener(task1 -> {
                    //Si la tarea es exitosa enviara un correo de verificación
                    if (task1.isSuccessful()){
                        //Lenguaje del mensaje
                        mAuth.setLanguageCode("es");
                        //Traer información del usuario registrado
                        FirebaseUser user = mAuth.getCurrentUser();
                        //Metodo para enviar correo
                        user.sendEmailVerification()
                                .addOnCompleteListener(task2 -> {
                                    //Verificar que la tarea fue exitosa
                                    if (task2.isSuccessful()){
                                        //Cerrar cuadro de dialogo
                                        pd.dismiss();
                                        //Enviar a la activity de las instrucciones
                                        startActivity(new Intent(RegistrarActivity.this, InstruccionesActivity.class));
                                        //No dejar volver a la activity anterior
                                        finish();
                                    }else {
                                        //Cerrar cuadro de dialogo
                                        pd.dismiss();
                                        //Mostrar error
                                        Toast.makeText(this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });
    }

}
