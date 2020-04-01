package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectogassapp.utilidades.Utilidades;

public class RegistrarActivity extends AppCompatActivity {

    EditText nombres,apellidos,correo, passw,confirmPass, tele;
    TextView problemaNom,problemaApe,problemaC,problemaP,problemaPC,problemaTele;
    Button btnRegistrarte;
    ImageView volver;
    ConexionDB db;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        db = new ConexionDB(this);
        nombres = findViewById(R.id.txtNombres);
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
                String correoU = correo.getText().toString();
                if (!correoU.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(correoU).matches()){
                    problemaC.setText("");
                }else{
                    problemaC.setText("Su dirección de correo electronico no es valida");
                }
                if (db.validarcorreo(correoU)){
                    problemaC.setText("El correo ya se encuentra registrado");
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
                String passwU = passw.getText().toString();
                String passwConfirm = confirmPass.getText().toString();
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




        problemaNom = findViewById(R.id.problemaNombre);
        problemaApe = findViewById(R.id.problemaApellido);
        problemaC = findViewById(R.id.problemaCorreo);
        problemaP = findViewById(R.id.problemaPass);
        problemaPC = findViewById(R.id.problemaPassC);
        problemaTele = findViewById(R.id.problemaTele);
        volver = findViewById(R.id.volver);

        volver.setOnClickListener(v -> {
            Intent moveLogin = new Intent(RegistrarActivity.this, LoginActivity.class);
            startActivity(moveLogin);
        });

        btnRegistrarte= findViewById(R.id.btnRegistrar);
        btnRegistrarte.setOnClickListener(v -> {
            String nombresU = nombres.getText().toString();
            String apellidosU = apellidos.getText().toString();
            String correoU = correo.getText().toString();
            String passwU = passw.getText().toString();
            String passwConfirmU = confirmPass.getText().toString();
            String teleU = tele.getText().toString();

            if (nombresU.isEmpty()){
                problemaNom.setText("El nombre es obligatorio");
            }else if (apellidosU.isEmpty()){
                problemaApe.setText("El apellido es obligatorio");
            }else if (correoU.isEmpty()){
                problemaC.setText("El correo es obligatorio");
            }else if (passwU.isEmpty()){
                problemaP.setText("La contraseña es obligatoria");
            }else if (passwConfirmU.isEmpty()){
                problemaPC.setText("Este campo es obligatorio");
            }else if (teleU.isEmpty()){
                problemaTele.setText("El telefono es obligatorio");
            }else{
                if (passwU.length()>=8){
                    if (passwU.equals(passwConfirmU)){
                        if (!db.validarcorreo(correoU)){
                            registrarUsuarios();
                            Intent moveLogin = new Intent(this,LoginActivity.class);
                            startActivity(moveLogin);
                            finish();
                            Toast.makeText(this,"Registro exitoso",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

        });
    }

    private void registrarUsuarios(){

        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        ContentValues registros = new ContentValues();
        registros.put(Utilidades.CAMPO_NOMBRES,nombres.getText().toString());
        registros.put(Utilidades.CAMPO_APELLIDOS,apellidos.getText().toString());
        registros.put(Utilidades.CAMPO_CORREO,correo.getText().toString());
        registros.put(Utilidades.CAMPO_CLAVE,passw.getText().toString());
        registros.put(Utilidades.CAMPO_TELEFONO,tele.getText().toString());

        long res = sqLiteDatabase.insert(Utilidades.TABLA_USUARIO,null,registros);
        sqLiteDatabase.close();
    }
}
