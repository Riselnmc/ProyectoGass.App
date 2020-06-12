package com.example.proyectogassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class RestablecerClaveActivity extends AppCompatActivity {

    //Variables para los componentes de la vista
    TextInputLayout emailU;

    //Variable para el correo
    private String email = "";

    //Variable para el autenticador
    FirebaseAuth mAuth;

    //Variable del cuadro de dialogo
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer_clave);

        //Instanciar autenticador
        mAuth = FirebaseAuth.getInstance();

        //Instanciar cuadro de dialogo
        pd = new ProgressDialog(this);

        //Referenciar la parte lógica con la vista
        emailU = findViewById(R.id.emailUsuario);
        Button btnReset = findViewById(R.id.restablecerClave);

        btnReset.setOnClickListener(v -> {

            //Darle a la varible los valores ingresados en el campo de texto
            email = emailU.getEditText().getText().toString().toLowerCase().trim();

            //Verificar que el campo sea diferente de vacio
            if (!email.isEmpty()){
                //Mensaje que queremos mostrar en el cuadro de dialogo
                pd.setMessage("Enviando correo...");
                //No permite que el usuario le de click en otro lugar de la pantalla mientras el mensaje se muestra
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                //Utilizar metodo de enviar correo
                enviarCorreo();
            }else{
                emailU.setError("El correo es obligatorio");
            }

        });
    }

    //Metodo para enviar correo electronico del cambio de contraseña
    private void enviarCorreo(){
        //Definir el lenguaje del mensaje que se enviara al correo
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                mostrarToastP("Correo enviado!");
            }else {
                mostrarToastP("Error al enviar correo");
            }
            //Cerrar mensaje
            pd.dismiss();
        });
    }

    private void mostrarToastP(String texto){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toast_personalizado, findViewById(R.id.toastPersonalizado));
        TextView textView = view.findViewById(R.id.tv_titulo1);
        textView.setText(texto);

        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

}
