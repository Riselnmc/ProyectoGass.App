package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;
import com.example.proyectogassapp.Adaptadores.AdaptadorEstaciones;
import com.example.proyectogassapp.entidades.Estaciones;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class EstacionesActivity extends AppCompatActivity implements AdaptadorEstaciones.OnListItemClick {

    private RecyclerView listEstaciones;

    private AdaptadorEstaciones adaptador;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estaciones);

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Estaciones");

        listEstaciones = findViewById(R.id.EstacionesS);

        FirestoreRecyclerOptions<Estaciones> options = new FirestoreRecyclerOptions.Builder<Estaciones>()
                .setQuery(query, Estaciones.class)
                .build();

        adaptador = new AdaptadorEstaciones(options,this);
        listEstaciones.setHasFixedSize(true);
        listEstaciones.setLayoutManager(new LinearLayoutManager(this));
        listEstaciones.setAdapter(adaptador);

    }

    @Override
    public void onItemClick(Estaciones snapshot, int position) {
        Toast.makeText(this, ""+snapshot.getNombreEstacion(), Toast.LENGTH_SHORT).show();
    }
}

