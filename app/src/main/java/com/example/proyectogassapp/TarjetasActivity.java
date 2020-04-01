package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.proyectogassapp.adaptadores.ListTarjetasAdapter;
import com.example.proyectogassapp.entidades.Tarjetas;
import com.example.proyectogassapp.utilidades.Utilidades;

import java.util.ArrayList;


public class TarjetasActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ConexionDB db;
    ArrayList<Tarjetas>listTarjetas;
    TextView tvTarjeta, tvTitular,tvFechaV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjetas);
        db = new ConexionDB(this);
        findViewById(R.id.Imgvolver).setOnClickListener(v -> {
            Intent atras = new Intent(TarjetasActivity.this, MapActivity.class);
            startActivity(atras);
            finish();
        });
        tvTarjeta = findViewById(R.id.tv_Tarjeta);
        tvTitular = findViewById(R.id.tv_Titular);
        tvFechaV = findViewById(R.id.tv_Fecha);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mostrarTarjetas();

        ListTarjetasAdapter adapter = new ListTarjetasAdapter(listTarjetas);
        adapter.setOnClickListener(v -> {
            String numeroTarjeta = listTarjetas.get(recyclerView.getChildAdapterPosition(v)).getNumeroTarjeta();
            String titular = listTarjetas.get(recyclerView.getChildAdapterPosition(v)).getTitular();
            String fechaV = listTarjetas.get(recyclerView.getChildAdapterPosition(v)).getMes()+"/"+listTarjetas.get(recyclerView.getChildAdapterPosition(v)).getYear();
            tvTarjeta.setText(numeroTarjeta);
            tvFechaV.setText("Vence: "+fechaV);
            tvTitular.setText("Titular: "+titular);
        });
        recyclerView.setAdapter(adapter);
        findViewById(R.id.btn_agregarT).setOnClickListener(v -> {
            Intent moveTarget = new Intent(TarjetasActivity.this, AgregarTarjetaActivity.class);
            startActivity(moveTarget);
        });
    }

    private void mostrarTarjetas() {
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        Tarjetas tarjeta;
        listTarjetas = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ Utilidades.TABLA_TARJETAS,null);
        while (cursor.moveToNext()){
            tarjeta = new Tarjetas();
            tarjeta.setId_T(cursor.getInt(0));
            tarjeta.setTitular(cursor.getString(1));
            tarjeta.setNumeroTarjeta(cursor.getString(2));
            tarjeta.setMes(cursor.getString(3));
            tarjeta.setYear(cursor.getString(4));
            listTarjetas.add(tarjeta);
        }
    }
}
