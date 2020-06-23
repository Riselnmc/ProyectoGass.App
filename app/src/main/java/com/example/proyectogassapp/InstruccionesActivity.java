package com.example.proyectogassapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.proyectogassapp.Adaptadores.OnBoardingAdapter;
import com.example.proyectogassapp.entidades.OnBoardingItem;
import java.util.ArrayList;
import java.util.List;

public class InstruccionesActivity extends AppCompatActivity {

    //Variable para el componente de la vista
    private LinearLayout indicadores;
    private Button btnSaltar;

    //Variable para instanciar la clase
    private OnBoardingAdapter onBoardingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);

        //Usar metodo para mostrar las instrucciones
        mostrarInstrucciones();

        //Referenciar la parte lógica con la vista
        indicadores = findViewById(R.id.indicador);
        btnSaltar = findViewById(R.id.btn_Siguiente);
        ViewPager2 vistasInstrucciones = findViewById(R.id.contenedorInstrucciones);

        //Dar valor del adaptador
        vistasInstrucciones.setAdapter(onBoardingAdapter);

        //Usar metodo para agregar el indicador
        agregarIndicador();
        //Iniciar en la primera página
        mostrarIndicadorActivado(0);

        //Interfaz de devolución de llamada para responder al estado cambiante de la página seleccionada.
        vistasInstrucciones.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //Mostrar el indicador activado dependiendo la posición en la que se encuentre
                mostrarIndicadorActivado(position);
            }
        });

        //Evento click para el botón
        btnSaltar.setOnClickListener(v -> {
            //Verificar si la página en la que se encuentra es menor a la cantidad de páginas
            if (vistasInstrucciones.getCurrentItem() + 1 < onBoardingAdapter.getItemCount()){
                //Enviar al siguiente item
                vistasInstrucciones.setCurrentItem(vistasInstrucciones.getCurrentItem() + 1);
            } else {
                //Enviar a la activity del mapa
                startActivity(new Intent(InstruccionesActivity.this,MapaActivity.class));
                //No dejar volver a esta activity
                finish();
            }
        });
    }

    //Metodo para mostrar las instrucciones
    private void mostrarInstrucciones(){
        //Objeto tipo lista
        List<OnBoardingItem> onBoardingItems = new ArrayList<>();

        //Instanciar item para la página de bienvenida
        OnBoardingItem itemBienvenido = new OnBoardingItem();
        //Titulo de la página
        itemBienvenido.setTitle("Bienvenido");
        //Imagen de la página
        itemBienvenido.setImagen(R.drawable.ic_registro_exitoso);

        //Instanciar item para la página del buscador
        OnBoardingItem itemBuscador = new OnBoardingItem();
        itemBuscador.setTitle("Buscar lugares");
        //Descripción de la página
        itemBuscador.setDescription("Puedes encontrar cualquier lugar que desees.");
        itemBuscador.setImagen(R.drawable.ic_buscador);

        //Instanciar item para la página de las rutas
        OnBoardingItem itemRutas = new OnBoardingItem();
        itemRutas.setTitle("Rutas");
        itemRutas.setDescription("Genera rutas para llegar más rápido a tu destino");
        itemRutas.setImagen(R.drawable.ic_ruta);

        //Instanciar item para la página de eliminar
        OnBoardingItem itemEliminar = new OnBoardingItem();
        itemEliminar.setTitle("Eliminar cuenta");
        itemEliminar.setDescription("Si no te sientes conforme con nuestros servicios puedes eliminar tu cuenta");
        itemEliminar.setImagen(R.drawable.ic_eliminar);

        //Agregar páginas en la lista
        onBoardingItems.add(itemBienvenido);
        onBoardingItems.add(itemBuscador);
        onBoardingItems.add(itemRutas);
        onBoardingItems.add(itemEliminar);

        //Instanciar el adaptador
        onBoardingAdapter = new OnBoardingAdapter(onBoardingItems);
    }

    //Metodo para agregar indicador
    private void agregarIndicador(){
        //Arreglo de imagenes, obtiene la cantidad de paginas que hay en el adaptador
        ImageView[] indicador = new ImageView[onBoardingAdapter.getItemCount()];

        //Adaptar el contenedor dependiendo la cantidad de imagenes que hayan en el grupo
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        //Definir margenes
        layoutParams.setMargins(8,0,8,0);
        //Ciclo para agregar los indicadores
        for (int i = 0; i < indicador.length; i++){
            //Instanciar objeto
            indicador[i] = new ImageView(InstruccionesActivity.this);
            //Definir imagen
            indicador[i].setImageDrawable(ContextCompat.getDrawable(InstruccionesActivity.this, R.drawable.indicador_desactivado));
            indicador[i].setLayoutParams(layoutParams);
            //Añadir vista con la cantidad de imagenes
            indicadores.addView(indicador[i]);
        }
    }

    @SuppressLint("SetTextI18n")
    //Metodo para mostrar indicador activo, @param index es la página en la que se ubica
    private void mostrarIndicadorActivado(int index){
        //Obtener cantidad de indicadores que hay en el grupo
        int contador = indicadores.getChildCount();
        for (int i = 0; i < contador; i++){
            //Devuelve la vista en la posición especifica del grupo
            ImageView imageView = (ImageView) indicadores.getChildAt(i);

            //Verificar que el indicador es igual al índice
            if (i == index){
                //Mostrar indicador activado
                imageView.setImageDrawable(ContextCompat.getDrawable(InstruccionesActivity.this, R.drawable.indicador_activado));
            }else {
                //Mostrar indicador desactivado
                imageView.setImageDrawable(ContextCompat.getDrawable(InstruccionesActivity.this, R.drawable.indicador_desactivado));
            }
        }
        //Verificar que el indice es igual a la cantidad de items
        if (index == onBoardingAdapter.getItemCount() -1 ){
            //Texto para el botón
            btnSaltar.setText("Comenzar");
        }else {
            //Texto para el botón
            btnSaltar.setText("Siguiente");
        }
    }
}
