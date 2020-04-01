package com.example.proyectogassapp.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogassapp.R;
import com.example.proyectogassapp.entidades.Estaciones;

import java.util.ArrayList;
import java.util.List;

public class ListEstacionesAdapter extends RecyclerView.Adapter<ListEstacionesAdapter.TarjetasViewHolder>
        implements View.OnClickListener{

    private List<Estaciones> listaEstaciones;
    private View.OnClickListener listener;

    public ListEstacionesAdapter(List<Estaciones> listaEstaciones){
        this.listaEstaciones = listaEstaciones;
    }

    @NonNull
    @Override
    public TarjetasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_estaciones,parent,false);
        view.setOnClickListener(this);
        return new TarjetasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarjetasViewHolder holder, int position) {
        holder.nameEstacion.setText(listaEstaciones.get(position).getNombreEstacion());
        holder.direccion.setText(listaEstaciones.get(position).getDireccionEstacion());
    }

    @Override
    public int getItemCount() {
        return listaEstaciones.size();
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
        TextView nameEstacion, direccion;
        public TarjetasViewHolder(@NonNull View itemView) {
            super(itemView);
            nameEstacion = itemView.findViewById(R.id.nameEstation);
            direccion = itemView.findViewById(R.id.direccionEstacion);
        }
    }

    public void filtrar(ArrayList<Estaciones>filtrarEstaciones){
        this.listaEstaciones = filtrarEstaciones;
        notifyDataSetChanged();
    }

}
