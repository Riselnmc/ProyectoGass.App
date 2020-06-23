//Nombre del paquete principal
package com.example.proyectogassapp;

//Importación de librerias
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

//Creación de la clase con herencia del AppCompatActivity
public class MainActivity extends AppCompatActivity {

    //Variables para las animaciones
    Animation animacionArriba,animacionAbajo;
    //Variables para los componentes de la vista
    ImageView logoSplash;
    TextView nombreAplicativo, version;

    /*
        * Ciclo de vida principal del activity
        * Se activa cuando el sistema crea la actividad por primera vez, se ejecuta la lógica básica
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamar la super clase onCreate para completar la creación de la activity
        super.onCreate(savedInstanceState);
        //Ocupar el 100% de la pantalla
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Establecer el diseño de la interfaz de usuario para esta activity
        setContentView(R.layout.activity_main);

        //Referenciar animaciones en esta activity
        animacionArriba = AnimationUtils.loadAnimation(this, R.anim.animacion_arriba);
        animacionAbajo = AnimationUtils.loadAnimation(this, R.anim.animacion_abajo);

        //Referenciar la parte lógica con la vista
        logoSplash = findViewById(R.id.logoSplash);
        nombreAplicativo = findViewById(R.id.nombreAplicativo);
        version = findViewById(R.id.version);

        //Aplicar animación en los componentes de la vista
        logoSplash.setAnimation(animacionArriba);
        nombreAplicativo.setAnimation(animacionAbajo);
        version.setAnimation(animacionAbajo);

        /*
            *Controlador de tiempo para iniciar la siguiente activity en 3 segundos
            *Intent es para vincular esta activity con la que enviaremos
            *startActivity es para enviar de un activity a otro
            *finish no deja regresar a la activity anterior
         */
        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }, 3000);
    }
}
