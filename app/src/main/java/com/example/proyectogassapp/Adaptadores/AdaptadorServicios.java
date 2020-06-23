package com.example.proyectogassapp.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogassapp.R;
import com.example.proyectogassapp.entidades.Servicios;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdaptadorServicios extends FirestoreRecyclerAdapter<Servicios,AdaptadorServicios.ServiciosViewHolder> {

    public AdaptadorServicios(@NonNull FirestoreRecyclerOptions<Servicios> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ServiciosViewHolder holder, int position, @NonNull Servicios model) {
        holder.nombreServicio.setText(model.getNombre());
        holder.precioServicio.setText(String.valueOf(model.getValor()));
    }

    @NonNull
    @Override
    public ServiciosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_servicios,parent,false);
        return new ServiciosViewHolder(view);
    }

    public class ServiciosViewHolder extends RecyclerView.ViewHolder {
        TextView nombreServicio, precioServicio;
        public ServiciosViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreServicio = itemView.findViewById(R.id.nombreServicio);
            precioServicio = itemView.findViewById(R.id.precioServicio);
        }
    }
}
