package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import com.example.proyectogassapp.Adaptadores.AdaptadorEstaciones;
import com.example.proyectogassapp.entidades.Estaciones;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class EstacionesActivity extends AppCompatActivity {

    //Variable para la clase adaptador
    private FirestoreRecyclerAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estaciones);

        //Instanciar base de datos
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
            * Variable para el RecyclerView
            * Referenciar parte lógica con la vista
         */
        RecyclerView listEstaciones = findViewById(R.id.EstacionesS);

        //Consulta
        Query query = db.collection("Estaciones");

        //Crear y dar valor al RecyclerView de Firebase
        FirestoreRecyclerOptions<Estaciones> options = new FirestoreRecyclerOptions.Builder<Estaciones>()
                //Establecer consulta
                .setQuery(query, Estaciones.class)
                //Construirlo
                .build();

        //Instanciar adaptador
        adaptador = new AdaptadorEstaciones(options,this);

        //Utilizar la máxima capacidad
        listEstaciones.setHasFixedSize(true);
        //Tipo de contenedor del RecyclerView
        listEstaciones.setLayoutManager(new LinearLayoutManager(this));
        //Establecer adaptador en la lista
        listEstaciones.setAdapter(adaptador);

        //Evento click para volver al mapa
        findViewById(R.id.Imgvolver).setOnClickListener(v ->
                startActivity(new Intent(EstacionesActivity.this, MapaActivity.class)));
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

