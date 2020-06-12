package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.proyectogassapp.Adaptadores.AdaptadorTarjetas;
import com.example.proyectogassapp.entidades.Tarjetas;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class TarjetasActivity extends AppCompatActivity implements AdaptadorTarjetas.OnListItemClick {

    RecyclerView listaTarjetas;
    TextView tvTarjeta, tvTitular,tvFechaV;
    AdaptadorTarjetas adaptador;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjetas);
        //Instanciar la base de datos
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Referenciar las variables
        tvTarjeta = findViewById(R.id.tv_Tarjeta);
        tvTitular = findViewById(R.id.tv_Titular);
        tvFechaV = findViewById(R.id.tv_Fecha);
        listaTarjetas = findViewById(R.id.recyclerView);

        String idUsuario = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        //Consulta para traer las tarjetas registradas
        Query query = db.collection("Tarjetas").whereEqualTo("IdUsuario",idUsuario);

        FirestoreRecyclerOptions<Tarjetas> options = new FirestoreRecyclerOptions.Builder<Tarjetas>()
                .setQuery(query, Tarjetas.class)
                .build();

        adaptador = new AdaptadorTarjetas(options,this);

        listaTarjetas.setHasFixedSize(true);
        listaTarjetas.setLayoutManager(new LinearLayoutManager(this));
        listaTarjetas.setAdapter(adaptador);

        //Referencia y metodo click para volver al mapa
        findViewById(R.id.Imgvolver).setOnClickListener(v -> {
            startActivity(new Intent(TarjetasActivity.this, MapaActivity.class));
        });

        //Referencia y metodo click para ir a la activity de agregar tarjeta
        findViewById(R.id.btn_agregarT).setOnClickListener(v ->
                startActivity(new Intent(TarjetasActivity.this, AgregarTarjetaActivity.class)));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemClick(Tarjetas snapshot, int position) {
        tvTitular.setText("Titular: "+snapshot.getTitular());
        tvTarjeta.setText(snapshot.getNumeroTarjeta());
        tvFechaV.setText("Vence: "+snapshot.getFechaVencimiento());
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
