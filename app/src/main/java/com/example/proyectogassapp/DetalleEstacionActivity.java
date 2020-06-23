package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.proyectogassapp.Adaptadores.AdaptadorServicios;
import com.example.proyectogassapp.entidades.Servicios;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DetalleEstacionActivity extends AppCompatActivity {

    //Variable para los componentes de la vista
    private TextView tvName,tvDireccion,tvTelefono;

    //Variable para el id de la estación
    private String idEstacion;

    //Variable para la base de datos
    FirebaseFirestore db;

    //Variable para la clase adaptador
    private AdaptadorServicios adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_estacion);

        RecyclerView listaServicios = findViewById(R.id.listaServicios);

        //Instanciar base de datos
        db = FirebaseFirestore.getInstance();

        //Evento click para volver a la activity de Estaciones
        findViewById(R.id.Imgvolver).setOnClickListener(v -> {
            startActivity(new Intent(DetalleEstacionActivity.this, EstacionesActivity.class));
        });

        //Obtener id de la estación enviado de la activity Estaciones
        idEstacion = getIntent().getStringExtra("idEstacion");

        //Referenciar la parte lógica con la vista
        tvName = findViewById(R.id.tvNameEstacion);
        tvDireccion = findViewById(R.id.tvDireccionEstacion);
        tvTelefono = findViewById(R.id.tvTelefonoEstacion);
        //Usar metodo para mostrar información de la estación
        mostrarInformacion();

        //Consulta para traer los servicios de la estación
        Query query = db.collection("Servicios").whereEqualTo("idEstacion",idEstacion);

        FirestoreRecyclerOptions<Servicios> options = new FirestoreRecyclerOptions.Builder<Servicios>()
                .setQuery(query, Servicios.class)
                .build();

        adaptador = new AdaptadorServicios(options);

        listaServicios.setHasFixedSize(true);
        listaServicios.setLayoutManager(new LinearLayoutManager(this));
        listaServicios.setAdapter(adaptador);
    }

    //Metodo para mostrar la información de la estación
    private void mostrarInformacion(){
        /*
            * Obtener la información de la estación
            * @param idEstacion para traer documento con este id
         */
        db.collection("Estaciones").document(idEstacion).get().addOnSuccessListener(documentSnapshot -> {
            //Verificar que existan datos
            if (documentSnapshot.exists()){
                //Variables que contienen la información de la estación
                String nombreEstacion = documentSnapshot.getString("NombreEstacion");
                String direccionEstacion = documentSnapshot.getString("DireccionEstacion");
                String telefonoEstacion = documentSnapshot.getString("TelefonoEstacion");
                //Dar valor a los TextView
                tvName.setText(nombreEstacion);
                tvDireccion.setText(direccionEstacion);
                tvTelefono.setText(telefonoEstacion);
            }
        });

    }

    /*
     * Garantiza que continúe el trabajo relacionado con la IU
     * Incluso cuando el usuario esté viendo tu activity en el modo multiventana.
     */
    @Override
    protected void onStop() {
        super.onStop();
        //Pausar la escucha de la lista
        adaptador.stopListening();
    }

    /*
     * Hace que el usuario pueda ver la activity mientras la app se prepara para que esta entre
     * en primer plano y se convierta en interactiva
     */
    @Override
    protected void onStart() {
        super.onStart();
        //Seguir con la escucha de la lista
        adaptador.startListening();
    }
}
