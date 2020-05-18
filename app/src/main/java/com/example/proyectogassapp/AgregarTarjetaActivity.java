package com.example.proyectogassapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class AgregarTarjetaActivity extends AppCompatActivity {

    //Constante de resultado
    private static final int Scan_result = 100;

    EditText nameT, numberC,mesV,yearV,claveS;
    TextView tvTitular, tvTarjeta, tvFecha;
    Button Scaner;

    String mes = "";
    String year = "";

    private String nombreT = "";
    private String numeroC = "";
    private String fechaV = "";
    private String claveSe = "";

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    //@SuppressLint("SetTextI18n") se utilizar para permitir texto en el @setText, normalmente se ingresan variables
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarjeta);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
                mes = mesV.getText().toString();
                year = yearV.getText().toString();
                tvFecha.setText("Vence: "+mes+"/"+year);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        claveS = findViewById(R.id.CodeSecurity);
        Scaner = findViewById(R.id.Scaner);

        /*
            *Metodo click para el botón escanear
            * En este @Intent tenemos algo diferente, nos envia a una activity creada por la libreria
            * La cual esta modificada por ellos y sirve para escanear la tarjeta trayendo los datos que le pedimos
            * @true hace referencia a que son obligatorios y
            * @false a que no son obligatorios
         */
        Scaner.setOnClickListener(v -> {
            Intent Scan = new Intent(AgregarTarjetaActivity.this, CardIOActivity.class)
                    .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY,true)
                    .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV,false)
                    .putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME,true);
            startActivityForResult(Scan, Scan_result);
        });
        findViewById(R.id.btn_agregar).setOnClickListener(v -> {
            nombreT = nameT.getText().toString();
            numeroC = numberC.getText().toString();
            fechaV = mes+"/"+year;
            claveSe = claveS.getText().toString().trim();
            registrarTarjetas();
        });

    }

    //Metodo para registrar las tarjetas
    private void registrarTarjetas() {

        //Traer el id del usuario para tenerlo como llave foranea
        String idUsuario = mAuth.getCurrentUser().getUid();

        /*
         *@Map para guardar los datos
         * El primer parametro es el nombre del campo
         * El segundo es la variable que tiene el valor que queremos
         */
        Map<String, Object>map = new HashMap<>();
        map.put("Titular", nombreT);
        map.put("NumeroTarjeta", numeroC);
        map.put("FechaVencimiento", fechaV);
        map.put("ClaveSeguridad", claveSe);
        map.put("IdUsuario", idUsuario);

        //Creación de la tabla con inserción de datos
        db.collection("Tarjetas").add(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(AgregarTarjetaActivity.this, "Tarjeta registrada exitosamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AgregarTarjetaActivity.this, TarjetasActivity.class));
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    //Este metodo se utiliza para traer datos de activitys diferentes
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Validamos que el valor que nos de sea igual a la constante
        if (requestCode == Scan_result){

            //Verificamos que se hayan enviado datos de la activity externa
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)){

                //Creamos objeto tipo @CreditCard para poder llamar los datos
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                //Darle valor al @TextView que tendrá el número de la tarjeta escaneada
                numberC.setText(scanResult.getRedactedCardNumber());

                //Darle el nombre del titular a la variable
                String name = String.valueOf(scanResult.cardholderName);

                //Ponerlo en el @TextView
                nameT.setText(name);

                //Verificar que la tarjeta no haya expirado según la fecha
                if (scanResult.isExpiryValid()){
                    mes = String.valueOf(scanResult.expiryMonth);
                    year = String.valueOf(scanResult.expiryYear);
                    mesV.setText(mes);
                    yearV.setText("/"+year);
                }
            }
        }

    }

}
