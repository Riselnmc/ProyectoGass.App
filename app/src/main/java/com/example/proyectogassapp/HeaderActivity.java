package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HeaderActivity extends AppCompatActivity {

    TextView nombreU, CorreoU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);

        nombreU = findViewById(R.id.nombresUsuario);
        CorreoU = findViewById(R.id.correoU);


    }
}
