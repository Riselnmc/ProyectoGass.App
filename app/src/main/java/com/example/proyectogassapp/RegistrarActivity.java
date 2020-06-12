package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistrarActivity extends AppCompatActivity {

    EditText nombres,apellidos,correo, passw,confirmPass, tele;
    ImageView volver;
    TextInputLayout problemaNombre,problemaApellido,problemaCorreo,problemaClave,problemaClaveC,problemaTelefono;

    //Variables que guardarán el valor que queremos registrar en la base de datos
    private String name = "";
    private String lastName = "";
    private String email = "";
    private String password = "";
    private String passwordC = "";
    private String phone = "";

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    //Variable para el cuadro de progreso
    ProgressDialog pd;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(this);

        nombres = findViewById(R.id.txtNombres);
        apellidos = findViewById(R.id.txtApellidos);
        correo = findViewById(R.id.txtCorreo);
        passw = findViewById(R.id.txtPassword);
        confirmPass = findViewById(R.id.txtPasswordConfirm);
        tele = findViewById(R.id.txtTel);


        problemaNombre = findViewById(R.id.txtNombres1);
        problemaApellido = findViewById(R.id.txtApellidos1);
        problemaCorreo = findViewById(R.id.txtCorreo1);
        problemaClave = findViewById(R.id.txtPassword1);
        problemaClaveC = findViewById(R.id.txtPasswordConfirm1);
        problemaTelefono = findViewById(R.id.txtTel1);

        volver = findViewById(R.id.volver);
        //Evento click para volver al login
        volver.setOnClickListener(v -> {
            startActivity(new Intent(RegistrarActivity.this, LoginActivity.class));
        });

    }

    //Metodo para el botón, se debe crear con un atributo tipo @View para relacionarlo con la vista
    public void confirmarRegistro(View view){
        name = nombres.getText().toString();
        lastName = apellidos.getText().toString();
        email = correo.getText().toString().toLowerCase().trim();
        password = passw.getText().toString().trim();
        passwordC = confirmPass.getText().toString().trim();
        phone = tele.getText().toString().trim();

        /*
         * Si los campos están vacios mostrará error y no dejará avanzar
         * Verificar que los campos no esten vacios
         */
        if (name.isEmpty() ) {
            problemaNombre.setError("El nombre no puede estar vacio");
        }else {
            problemaNombre.setError(null);
        }

        if (lastName.isEmpty()){
            problemaApellido.setError("El apellido no puede estar vacio");
        }else {
            problemaApellido.setError(null);
        }

        if (email.isEmpty()){
            problemaCorreo.setError("El correo no puede estar vacio o no es valido");
        } else {
            problemaCorreo.setError(null);
        }

        if (password.isEmpty()){
            problemaClave.setError("La contraseña no puede estar vacia");
        } else {
            problemaClave.setError(null);
        }

        if (passwordC.isEmpty()){
            problemaClaveC.setError("Este campo es obligatiorio");
        }else {
            problemaClaveC.setError(null);
        }

        if (phone.isEmpty()){
            problemaTelefono.setError("El teléfono no puede estar vacio");
        }else {
            problemaTelefono.setError(null);
        }

        //Validar que todos los campos no esten vacios
        if (!name.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !passwordC.isEmpty() && !phone.isEmpty()){
            //Validar correo
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                //Verificar que la contraseña cumpla los requisitos
                if (password.length()>=8){
                    //Verificar que las contraseñas sean iguales
                    if (passwordC.equals(password)){
                        registrarUsuario();
                    }else {
                        problemaClaveC.setError("Las contraseñas no coinciden");
                    }
                }else {
                    problemaClave.setError("La contraseña debe ser mayor o igual a 8 caracteres");
                }
            }else {
                problemaCorreo.setError("La dirección de correo no es valida");
            }
        }
    }

    //Metodo para registrar un nuevo usuario
    private void registrarUsuario(){

        pd.setMessage("Registrando usuario...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        //Función para crear usuario con correo y contraseña
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                /*
                    *@Map para guardar los datos
                    * El primero parametro es el nombre del campo
                    * El segundo es la variable que tiene el valor que queremos
                 */
                Map<String, Object>map = new HashMap<>();
                map.put("nombres", name);
                map.put("apellidos", lastName);
                map.put("correo", email);
                map.put("clave", password);
                map.put("telefono", phone);

                //Variable que tendrá el id del usuario
                String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                /*
                    *Creación de la tabla y darle el id al registro
                    * @setValue se ingresa el nombre que le dimos a la variable tipo @Map
                 */
                db.collection("Usuarios").document(id).set(map).addOnCompleteListener(task1 -> {
                    //Si los datos se registraron exitosamente se detendrá el cuadro de carga, enviara a las instrucciones
                    //Y mostrará un @Toast
                    if (task1.isSuccessful()){
                        pd.dismiss();
                        Toast.makeText(RegistrarActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrarActivity.this, MapaActivity.class));
                        finish();
                    }
                });
            }
        });
    }



}
