package com.example.proyectogassapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectogassapp.utilidades.Utilidades;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class AgregarTarjetaActivity extends AppCompatActivity {

    private static final int Scan_result = 100;
    EditText nameT, numberC,mesV,yearV,claveS;
    TextView tvTitular, tvTarjeta, tvFecha;
    Button Scaner,addCard;
    ConexionDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarjeta);
        db = new ConexionDB(this);
        init();

    }
    @SuppressLint("SetTextI18n")
    private void init() {
        tvTitular = findViewById(R.id.tv_Titular);
        tvTarjeta = findViewById(R.id.tv_Tarjeta);
        tvFecha = findViewById(R.id.tv_Fecha);
        nameT = findViewById(R.id.NameTitular);
        nameT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String titular = nameT.getText().toString();
                tvTitular.setText("Titular: "+titular);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        numberC = findViewById(R.id.NumberCard);
        numberC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String numberT = numberC.getText().toString();
                if (numberT.length() == 0){
                    tvTarjeta.setText(R.string.numberCard);
                }else {
                    tvTarjeta.setText(numberT);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mesV = findViewById(R.id.Mes);
        mesV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mes = mesV.getText().toString();
                if (mes.length() == 0){
                    mesV.setText("Vence : 00/00");
                }else {
                    tvFecha.setText("Vence: "+mes);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        yearV = findViewById(R.id.Year);
        yearV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mes = mesV.getText().toString();
                String year = yearV.getText().toString();
                tvFecha.setText("Vence: "+mes+"/"+year);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        claveS = findViewById(R.id.CodeSecurity);
        Scaner = findViewById(R.id.Scaner);
        Scaner.setOnClickListener(v -> {
            Intent Scan = new Intent(AgregarTarjetaActivity.this, CardIOActivity.class)
                    .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY,true)
                    .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV,false)
                    .putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE,false)
                    .putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME,true);
            startActivityForResult(Scan, Scan_result);
        });
        findViewById(R.id.btn_agregar).setOnClickListener(v -> {
            String nameC = nameT.getText().toString();
            String numbert = numberC.getText().toString();
            String fechaC = mesV.getText().toString();
            String claveC = claveS.getText().toString();
            registrarTarjetas();
            Intent moveTarjetas = new Intent(AgregarTarjetaActivity.this,TarjetasActivity.class);
            startActivity(moveTarjetas);
            finish();
            Toast.makeText(AgregarTarjetaActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        });
    }

    private void registrarTarjetas() {

        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        ContentValues registros = new ContentValues();
        registros.put(Utilidades.TITULAR_TARJETA,nameT.getText().toString());
        registros.put(Utilidades.NUMERO_TARJETA,numberC.getText().toString());
        registros.put(Utilidades.MES_TARJETA,mesV.getText().toString());
        registros.put(Utilidades.YEAR_TARJETA,yearV.getText().toString());
        registros.put(Utilidades.CLAVE_TARJETA,claveS.getText().toString());

        long res = sqLiteDatabase.insert(Utilidades.TABLA_TARJETAS,null,registros);
        sqLiteDatabase.close();

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Scan_result){
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)){
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                numberC.setText(scanResult.getRedactedCardNumber());
                String name = String.valueOf(scanResult.cardholderName);
                nameT.setText(name);
                if (scanResult.isExpiryValid()){
                    String mes = String.valueOf(scanResult.expiryMonth);
                    String year = String.valueOf(scanResult.expiryYear);
                    mesV.setText(mes);
                    yearV.setText("/"+year);
                }
            }
        }

    }
}
