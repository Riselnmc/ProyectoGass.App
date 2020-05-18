package com.example.proyectogassapp.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectogassapp.R;
import com.example.proyectogassapp.entidades.Tarjetas;

import java.util.ArrayList;

public class AdaptadorTarjetas extends RecyclerView.Adapter<AdaptadorTarjetas.TarjetasViewHolder> {

    private ArrayList<Tarjetas>listaTarjetas;

    public AdaptadorTarjetas(ArrayList<Tarjetas>listaTarjetas){
        this.listaTarjetas = listaTarjetas;
    }

    @NonNull
    @Override
    public TarjetasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Definimos que estilo queremos utilizar y lo referenciamos en la vista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_tarjetas,parent,false);
        //Lo retornamos para que se muestre en la vista
        return new TarjetasViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TarjetasViewHolder holder, int position) {
        Tarjetas tarjetas = listaTarjetas.get(position);
        holder.numeroTarjeta.setText(tarjetas.getNumeroTarjeta());
        holder.titular.setText(tarjetas.getTitular());
        holder.fechaVencimiento.setText(tarjetas.getFechaVencimiento());
    }

    @Override
    public int getItemCount() {
        return listaTarjetas.size();
    }

    static class TarjetasViewHolder extends RecyclerView.ViewHolder{
        //Definimos variables
        private TextView numeroTarjeta,titular,fechaVencimiento;
        //Necesitamos referenciar la lógica con la vista, entonces creamos un elemento tipo @View para hacer el casting
        TarjetasViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroTarjeta = itemView.findViewById(R.id.NumberTarjeta);
            titular = itemView.findViewById(R.id.Titular);
            fechaVencimiento = itemView.findViewById(R.id.FechaVencimiento);
        }
    }
}
