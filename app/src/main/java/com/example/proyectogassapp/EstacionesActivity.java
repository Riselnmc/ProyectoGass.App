package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.proyectogassapp.Adaptadores.AdaptadorEstaciones;
import com.example.proyectogassapp.entidades.Estaciones;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class EstacionesActivity extends AppCompatActivity {

    FirestoreRecyclerAdapter adaptador;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estaciones);

        db = FirebaseFirestore.getInstance();
        RecyclerView listEstaciones = findViewById(R.id.EstacionesS);

        Query query = db.collection("Estaciones");

        FirestoreRecyclerOptions<Estaciones> options = new FirestoreRecyclerOptions.Builder<Estaciones>()
                .setQuery(query, Estaciones.class)
                .build();

        adaptador = new AdaptadorEstaciones(options,this);

        listEstaciones.setHasFixedSize(true);
        listEstaciones.setLayoutManager(new LinearLayoutManager(this));
        listEstaciones.setAdapter(adaptador);

        findViewById(R.id.Imgvolver).setOnClickListener(v ->
                startActivity(new Intent(EstacionesActivity.this, MapaActivity.class)));
    }

    @Override
    protected void onStop() {
        super.onStop();
        adaptador.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adaptador.startListening();
    }

}

