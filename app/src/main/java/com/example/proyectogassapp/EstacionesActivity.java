package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.proyectogassapp.adaptadores.ListEstacionesAdapter;
import com.example.proyectogassapp.entidades.Estaciones;
import com.example.proyectogassapp.entidades.Tarjetas;
import com.example.proyectogassapp.utilidades.Utilidades;

import java.util.ArrayList;

public class EstacionesActivity extends AppCompatActivity {

    ConexionDB db;
    RecyclerView recyclerView;
    ArrayList<Estaciones>listEstaciones;
    ListEstacionesAdapter adapter;
    EditText edtBuscador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estaciones);
        findViewById(R.id.Imgvolver).setOnClickListener(v -> {
            Intent atras = new Intent(EstacionesActivity.this, MapaActivity.class);
            startActivity(atras);
        });
        db = new ConexionDB(this);
        recyclerView = findViewById(R.id.recyclerViewE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mostrarEstaciones();
        adapter = new ListEstacionesAdapter(listEstaciones);
        adapter.setOnClickListener(v ->{
            Estaciones estacion = listEstaciones.get(recyclerView.getChildAdapterPosition(v));
            Intent intent = new Intent(EstacionesActivity.this, DetalleEstacionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Estacion",estacion);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        edtBuscador = findViewById(R.id.EdtBuscador);
        edtBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString());
            }
        });
    }

    private void mostrarEstaciones() {
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        Estaciones estaciones;
        listEstaciones = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ Utilidades.TABLA_ESTACIONES,null);
        while (cursor.moveToNext()){
            estaciones = new Estaciones();
            estaciones.setId_E(cursor.getInt(0));
            estaciones.setNombreEstacion(cursor.getString(1));
            estaciones.setDireccionEstacion(cursor.getString(2));
            listEstaciones.add(estaciones);
        }
    }

    public void filtrar(String texto){
        ArrayList<Estaciones>filtrarLista = new ArrayList<>();
        for (Estaciones estacion : listEstaciones){
            if (estacion.getNombreEstacion().toLowerCase().contains(texto.toLowerCase())){
                filtrarLista.add(estacion);
            }
        }
        adapter.filtrar(filtrarLista);
    }
}
