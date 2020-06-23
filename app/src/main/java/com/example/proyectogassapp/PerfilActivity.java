package com.example.proyectogassapp;

//Importación de librerias
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class PerfilActivity extends AppCompatActivity {

    //Variable para los componentes de la vista
    private TextView nameU,lastNameU,emailU;

    //Variable para el autenticador
    private FirebaseAuth mAuth;

    //Variable para la base de datos
    private FirebaseFirestore db;

    /*
     * Ciclo de vida principal del activity
     * Aquí se ponen todos las funciones que queremos ver al iniciar el activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //Instanciar autenticador
        mAuth = FirebaseAuth.getInstance();
        //Instanciar base de datos
        db = FirebaseFirestore.getInstance();

        //Referenciar la parte lógica con la vista
        nameU = findViewById(R.id.nombreUsuario);
        lastNameU = findViewById(R.id.apellidoUsuario);
        emailU = findViewById(R.id.correoUsuario);

        //Usar metodo para traer la información del usuario
        getUserInfo();

        //Evento click para volver a la activity del mapa
        findViewById(R.id.Imgvolver).setOnClickListener(v -> {
            startActivity(new Intent(PerfilActivity.this, MapaActivity.class));
        });

        //Evento click para ir a la activity del cambio de correo
        findViewById(R.id.moveCambiarCorreo).setOnClickListener(v -> {
            startActivity(new Intent(PerfilActivity.this, CambiarCorreoActivity.class));
        });

        //Evento click para ir a la activity del cambio de contraseña
        findViewById(R.id.moveCambiarClave).setOnClickListener(v ->{
            startActivity(new Intent(PerfilActivity.this, CambiarClaveActivity.class));
        });

        //Evento click para ir a la activity de eliminar la cuenta
        findViewById(R.id.moveEliminarCuenta).setOnClickListener(v -> {
            startActivity(new Intent(PerfilActivity.this,EliminarCuentaActivity.class));
        });
    }

    //Metodo para traer la información del usuario
    private void getUserInfo(){
        //Variable con el id del usuario
        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        /*
            * Definir colección
            * @Id del usuario
            * get se usa para traer los datos
         */
        db.collection("Usuarios").document(id).get().addOnSuccessListener(documentSnapshot -> {
            //Si los datos existen mostrará esos datos
            if (documentSnapshot.exists()){
                //Variables con los datos del usuario
                String nombre = documentSnapshot.getString("nombres");
                String apellido = documentSnapshot.getString("apellidos");
                String correo = documentSnapshot.getString("correo");

                //Dar valor a los @TextView
                nameU.setText(nombre);
                lastNameU.setText(apellido);
                emailU.setText(correo);
            }
        });
    }
}
