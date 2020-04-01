package com.example.proyectogassapp.adaptadores;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogassapp.R;
import com.example.proyectogassapp.entidades.Tarjetas;

import java.util.ArrayList;

public class ListTarjetasAdapter extends RecyclerView.Adapter<ListTarjetasAdapter.TarjetasViewHolder>
                implements View.OnClickListener{

    private ArrayList<Tarjetas> listaTarjeta;
    private View.OnClickListener listener;

    public ListTarjetasAdapter(ArrayList<Tarjetas>listaTarjeta){
        this.listaTarjeta = listaTarjeta;
    }

    @NonNull
    @Override
    public TarjetasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_tarjetas,null,false);
        view.setOnClickListener(this);
        return new TarjetasViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TarjetasViewHolder holder, int position) {
        holder.numberTarjeta.setText(listaTarjeta.get(position).getNumeroTarjeta());
        holder.titular.setText(listaTarjeta.get(position).getTitular());
        holder.fechaVe.setText(listaTarjeta.get(position).getMes()+"/"+listaTarjeta.get(position).getYear());
    }

    @Override
    public int getItemCount() {
        return listaTarjeta.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class TarjetasViewHolder extends RecyclerView.ViewHolder {
        TextView numberTarjeta,titular,fechaVe;
        public TarjetasViewHolder(@NonNull View itemView) {
            super(itemView);
            numberTarjeta = itemView.findViewById(R.id.NumberTarjeta);
            titular = itemView.findViewById(R.id.Titular);
            fechaVe = itemView.findViewById(R.id.fechaVencimiento);
        }
    }
}
