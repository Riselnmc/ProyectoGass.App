package com.example.proyectogassapp.Adaptadores;

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

public class AdaptadorEstaciones extends FirestoreRecyclerAdapter<Estaciones, AdaptadorEstaciones.EstacionesViewHolder> {

    Activity activity;

    public AdaptadorEstaciones(@NonNull FirestoreRecyclerOptions<Estaciones> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull EstacionesViewHolder holder, int position, @NonNull Estaciones model) {

        DocumentSnapshot collectionEstacion = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String idEstacion = collectionEstacion.getId();

        holder.nombreEstacion.setText(model.getNombreEstacion());
        holder.direccionEstacion.setText(model.getDireccionEstacion());
        holder.mostrarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveDetalleEstacion = new Intent(activity, DetalleEstacionActivity.class);
                moveDetalleEstacion.putExtra("idEstacion",idEstacion);
                activity.startActivity(moveDetalleEstacion);
            }
        });
    }

    @NonNull
    @Override
    public EstacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_estaciones,parent,false);
        return new EstacionesViewHolder(view);
    }

    public class EstacionesViewHolder extends RecyclerView.ViewHolder {
        TextView nombreEstacion,direccionEstacion,mostrarInfo;
        public EstacionesViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreEstacion = itemView.findViewById(R.id.nameEstation);
            direccionEstacion = itemView.findViewById(R.id.direccionEstacion);
            mostrarInfo = itemView.findViewById(R.id.mostrarInfo);
        }

    }

}
