//Nombre del paquete principal
package com.example.proyectogassapp;

//Importación de librerias
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

//Creación de la clase con herencia del AppCompatActivity
public class RestablecerClaveActivity extends AppCompatActivity {

    //Variables del EditText para el correo
    TextInputLayout emailU;

    //Variable para el correo
    private String email = "";

    //Variable para el autenticador
    private FirebaseAuth mAuth;

    //Variable del cuadro de dialogo
    private ProgressDialog pd;

    /*
     * Ciclo de vida principal del activity
     * Se activa cuando el sistema crea la actividad por primera vez, se ejecuta la lógica básica
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamar la super clase onCreate para completar la creación de la activity
        super.onCreate(savedInstanceState);
        //Establecer el diseño de la interfaz de usuario para esta activity
        setContentView(R.layout.activity_restablecer_clave);

        //Instanciar autenticador
        mAuth = FirebaseAuth.getInstance();
        //Instanciar cuadro de dialogo
        pd = new ProgressDialog(this);

        //Referenciar la parte lógica con la vista
        emailU = findViewById(R.id.emailUsuario);
        Button btnReset = findViewById(R.id.restablecerClave);

        //Evento click para volver al login
        findViewById(R.id.Imgvolver).setOnClickListener(v -> {
            startActivity(new Intent(RestablecerClaveActivity.this, LoginActivity.class));
        });

        //Evento click para enviar correo
        btnReset.setOnClickListener(v -> {
            /*
             * Recibir valor del campo de texto
             * Dar valor a la variable y borrar los espacios
             */
            email = emailU.getEditText().getText().toString().toLowerCase().trim();

            //Verificar que el campo no este vacio
            if (!email.isEmpty()){
                //No mostrar el error
                emailU.setError(null);
                //Mensaje que queremos mostrar en el cuadro de dialogo
                pd.setMessage("Enviando correo...");
                //No permite dar click en otro lugar de la pantalla mientras el cuadro se muestra
                pd.setCanceledOnTouchOutside(false);
                //Mostrar cuadro de dialogo
                pd.show();
                //Utilizar metodo de enviar correo
                enviarCorreo();
            }else{
                //Mostrar error
                emailU.setError("El correo no puede estar vacio");
            }

        });
    }

    //Metodo para enviar correo
    private void enviarCorreo(){
        //Lenguaje del mensaje
        mAuth.setLanguageCode("es");
        /*
            * Metodo para enviar correo
            * @param email correo del usuario con el que se registro en la aplicación
         */
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            //Si la tarea fue exitosa
            if (task.isSuccessful()){
                //Mostrar Toast
                mostrarToastP("Correo enviado!");
            }else {
                //Mostrar Toast
                mostrarToastP("Error al enviar correo");
            }
            //Cerrar cuadro de dialogo
            pd.dismiss();
        });
    }

    /*
        * Metodo para mostrar Toast personalizado
        * @texto variable para definir el mensaje que queremos mostrar
     */
    private void mostrarToastP(String texto){
        LayoutInflater inflater = getLayoutInflater();
        /*
            * Creación de variable para referenciar con la vista
            * Referenciar la vista y referenciar el id del contenedor
         */
        View view = inflater.inflate(R.layout.toast_personalizado, findViewById(R.id.toastPersonalizado));
        //Referenciar la parte lógica con la vista
        TextView textView = view.findViewById(R.id.tv_titulo1);
        //Dar valor al mensaje que se mostrará en el Toast
        textView.setText(texto);

        /*
            * Crear Toast
            * @context es la activity en la que se va a mostrar
         */
        Toast toast = new Toast(this);
        //Ubicación de la pantalla en la que se mostrará || Centrado
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        //Duración del Toast
        toast.setDuration(Toast.LENGTH_SHORT);
        //Obtener toast peronalizado
        toast.setView(view);
        //Mostrar Toast
        toast.show();
    }

}
