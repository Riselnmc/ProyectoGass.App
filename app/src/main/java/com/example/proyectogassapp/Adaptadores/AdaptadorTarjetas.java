package com.example.proyectogassapp.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectogassapp.R;
import com.example.proyectogassapp.entidades.Tarjetas;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdaptadorTarjetas extends FirestoreRecyclerAdapter<Tarjetas, AdaptadorTarjetas.TarjetasViewHolder> {

    private OnListItemClick onListItemClick;

    public AdaptadorTarjetas(@NonNull FirestoreRecyclerOptions<Tarjetas> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull TarjetasViewHolder holder, int position, @NonNull Tarjetas model) {
        holder.nombreTitular.setText(model.getTitular());
        holder.numeroTarjeta.setText(model.getNumeroTarjeta());
        holder.fechaVencimiento.setText(model.getFechaVencimiento());
    }

    @NonNull
    @Override
    public TarjetasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_tarjetas,parent,false);
        return new TarjetasViewHolder(view);
    }

    public class TarjetasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView numeroTarjeta, nombreTitular,fechaVencimiento;
        public TarjetasViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroTarjeta = itemView.findViewById(R.id.NumberTarjeta);
            nombreTitular = itemView.findViewById(R.id.Titular);
            fechaVencimiento = itemView.findViewById(R.id.FechaVencimiento);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }
    public interface OnListItemClick{
        void onItemClick(Tarjetas snapshot, int position);
    }
}
