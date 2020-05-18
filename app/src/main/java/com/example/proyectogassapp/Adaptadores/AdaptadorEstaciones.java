package com.example.proyectogassapp.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectogassapp.R;
import com.example.proyectogassapp.entidades.Estaciones;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdaptadorEstaciones extends FirestoreRecyclerAdapter<Estaciones, AdaptadorEstaciones.EstacionesViewHolder> {

    private OnListItemClick onListItemClick;

    public AdaptadorEstaciones(@NonNull FirestoreRecyclerOptions<Estaciones> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull EstacionesViewHolder holder, int position, @NonNull Estaciones model) {
        holder.nombreEstacion.setText(model.getNombreEstacion());
        holder.direccionEstacion.setText(model.getDireccionEstacion());
    }

    @NonNull
    @Override
    public EstacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_estaciones,parent, false);
        return new EstacionesViewHolder(view);
    }

    public class EstacionesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombreEstacion,direccionEstacion;
        EstacionesViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreEstacion = itemView.findViewById(R.id.nameEstation);
            direccionEstacion = itemView.findViewById(R.id.direccionEstacion);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }
    public interface OnListItemClick{
        void onItemClick(Estaciones snapshot, int position);
    }
}
