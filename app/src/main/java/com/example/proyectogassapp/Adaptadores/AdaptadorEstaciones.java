//Nombre del paquete
package com.example.proyectogassapp.Adaptadores;

//Importanción de librerias
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogassapp.DetalleEstacionActivity;
import com.example.proyectogassapp.R;
import com.example.proyectogassapp.entidades.Estaciones;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

//Creación de la clase con herencia del adaptador de firebase
public class AdaptadorEstaciones extends FirestoreRecyclerAdapter<Estaciones, AdaptadorEstaciones.EstacionesViewHolder> {

    //Variable para utilizar una activity
    private Activity activity;

    //Constructor
    public AdaptadorEstaciones(@NonNull FirestoreRecyclerOptions<Estaciones> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    /*
        * Metodo para referenciar los datos con la vista
        * @param holder hacer referencia a un item del RecyclerView
        * @param position para obtener la posicion del item
        * @param model variable de la clase Estaciones
     */
    @Override
    protected void onBindViewHolder(@NonNull EstacionesViewHolder holder, int position, @NonNull Estaciones model) {

        // Traer la posición del item
        DocumentSnapshot collectionEstacion = getSnapshots().getSnapshot(holder.getAdapterPosition());
        // Id de la estación según el item
        final String idEstacion = collectionEstacion.getId();

        //Darle valor a los TextView con el metodo get
        holder.nombreEstacion.setText(model.getNombreEstacion());
        holder.direccionEstacion.setText(model.getDireccionEstacion());
        //Evento click en el TextView para ver más información de la estación
        holder.mostrarInfo.setOnClickListener(v -> {
            //Vincular esta activity con la de detalles de estación
            Intent moveDetalleEstacion = new Intent(activity, DetalleEstacionActivity.class);
            //Enviar el id de la estación
            moveDetalleEstacion.putExtra("idEstacion",idEstacion);
            //Enviar a la activity
            activity.startActivity(moveDetalleEstacion);
        });
    }

    @NonNull
    @Override
    public EstacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_estaciones,parent,false);
        return new EstacionesViewHolder(view);
    }

    //Constructor con herencia del RecyclerView
    static class EstacionesViewHolder extends RecyclerView.ViewHolder {
        //Variables para los componentes de la vista
        TextView nombreEstacion,direccionEstacion,mostrarInfo;
        /*
            * Referenciamos los componentes
         */
        EstacionesViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreEstacion = itemView.findViewById(R.id.nameEstation);
            direccionEstacion = itemView.findViewById(R.id.direccionEstacion);
            mostrarInfo = itemView.findViewById(R.id.mostrarInfo);
        }

    }

}
