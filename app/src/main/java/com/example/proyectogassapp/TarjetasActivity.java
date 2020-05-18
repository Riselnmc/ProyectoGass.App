package com.example.proyectogassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.proyectogassapp.Adaptadores.AdaptadorTarjetas;
import com.example.proyectogassapp.entidades.Tarjetas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TarjetasActivity extends AppCompatActivity {

    RecyclerView listaTarjetas;
    TextView tvTarjeta, tvTitular,tvFechaV;
    ArrayList<Tarjetas>mListaTarjetas = new ArrayList<>();

    private AdaptadorTarjetas adaptador;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjetas);
        //Instanciar la base de datos
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Referencia y metodo click para volver al mapa
        findViewById(R.id.Imgvolver).setOnClickListener(v -> {
            Intent atras = new Intent(TarjetasActivity.this, MapaActivity.class);
            startActivity(atras);
            finish();
        });
        //Referencia de las variables
        tvTarjeta = findViewById(R.id.tv_Tarjeta);
        tvTitular = findViewById(R.id.tv_Titular);
        tvFechaV = findViewById(R.id.tv_Fecha);
        listaTarjetas = findViewById(R.id.recyclerView);
        listaTarjetas.setLayoutManager(new LinearLayoutManager(this));
        consultarDatos();
        findViewById(R.id.btn_agregarT).setOnClickListener(v ->
                startActivity(new Intent(TarjetasActivity.this, AgregarTarjetaActivity.class)));
    }

    private void consultarDatos() {
        //Le dara a la varible @id el id del usuario que se encuentra logueado
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Tarjetas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Tarjetas tarjetas = snapshot.getValue(Tarjetas.class);
                        tarjetas.getNumeroTarjeta();
                        tarjetas.getTitular();
                        tarjetas.getFechaVencimiento();
                        mListaTarjetas.add(tarjetas);
                    }
                    adaptador = new AdaptadorTarjetas(mListaTarjetas);
                    listaTarjetas.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
