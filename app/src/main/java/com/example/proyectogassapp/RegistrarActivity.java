package com.example.proyectogassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrarActivity extends AppCompatActivity {

    EditText nombres,apellidos,correo, passw,confirmPass, tele;
    TextView problemaNom,problemaApe,problemaC,problemaP,problemaPC,problemaTele;
    Button btnRegistrarte;
    ImageView volver;

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

        //Referenciar las variables con la vista para los mensajes de error
        problemaNom = findViewById(R.id.problemaNombre);
        problemaApe = findViewById(R.id.problemaApellido);
        problemaC = findViewById(R.id.problemaCorreo);
        problemaP = findViewById(R.id.problemaPass);
        problemaPC = findViewById(R.id.problemaPassC);
        problemaTele = findViewById(R.id.problemaTele);

        nombres = findViewById(R.id.txtNombres);

        /*
            *Evento para cuando el campo tenga algún cambio
            * @beforeTextChanged antes de escribir
            * @onTextChanged mientras se escribe
            * @afterTextChanged después de escribir
         */
        nombres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                problemaNom.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        apellidos = findViewById(R.id.txtApellidos);
        apellidos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                problemaApe.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        correo = findViewById(R.id.txtCorreo);
        correo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                problemaC.setText("");
                String correoU = correo.getText().toString().trim();

                //Condición para verificar si el campo no esta vacio y cumple con los requisitos de un correo electronico
                if (!correoU.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(correoU).matches()){
                    problemaC.setText("");
                }else{
                    problemaC.setText("Su dirección de correo electronico no es valida");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passw = findViewById(R.id.txtPassword);
        passw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                problemaP.setText("");
                String passwU = passw.getText().toString();
                //Condición para mostrar mensaje de error cuando la contraseña es menor a 8
                if (passwU.length()<8){
                    problemaP.setText("Contraseña debe ser mayor o igual a 8 caracteres");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPass = findViewById(R.id.txtPasswordConfirm);
        confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                problemaPC.setText("");
                String passwU = passw.getText().toString().trim();
                String passwConfirm = confirmPass.getText().toString().trim();

                //Condición para comparar las contraseñas
                if (passwConfirm.equals(passwU)){
                    problemaPC.setText("");
                }else{
                    problemaPC.setText("Las contraseñas no coinciden");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tele = findViewById(R.id.txtTel);
        tele.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                problemaTele.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        volver = findViewById(R.id.volver);

        //Evento click para volver al login
        volver.setOnClickListener(v -> {
            Intent moveLogin = new Intent(RegistrarActivity.this, LoginActivity.class);
            startActivity(moveLogin);
        });

        btnRegistrarte= findViewById(R.id.btnRegistrar);

        //Evento click en el botón para registrarse
        btnRegistrarte.setOnClickListener(v -> {
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
                problemaNom.setText("El nombre es obligatorio");
            }else if (lastName.isEmpty()){
                problemaApe.setText("El apellido es obligatorio");
            }else if (email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                problemaC.setText("El correo es obligatorio ");
            }else if (password.isEmpty()){
                problemaP.setText("La contraseña es obligatoria");
            }else if (passwordC.isEmpty()){
                problemaPC.setText("Este campo es obligatorio");
            }else if (phone.isEmpty()){
                problemaTele.setText("El teléfono es obligario");
            }else if (!name.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !passwordC.isEmpty() && !phone.isEmpty()){
               //Validar correo
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //Verificar que la contraseña cumpla los requisitos
                    if (password.length()>=8){
                        //Verificar que las contraseñas sean iguales
                        if (passwordC.equals(password)){
                            registrarUsuario();
                        }else {
                            problemaPC.setText("Las contraseñas no coinciden");
                        }
                    }else {
                        problemaP.setText("La contraseña debe ser mayor o igual a 8 caracteres");
                    }
                }else {
                    problemaC.setText("Dirección de correo invalida");
                }
            }
        });
    }

    //Metodo para registrar un nuevo usuario
    private void registrarUsuario(){

        //Titulo del mensaje que se va a mostrar
        pd.setTitle("Registrando usuario");

        //Mostrar
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
                String id = mAuth.getCurrentUser().getUid();

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
