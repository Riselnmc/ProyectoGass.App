package com.example.proyectogassapp.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogassapp.R;
import com.example.proyectogassapp.entidades.OnBoardingItem;

import java.util.List;

public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder> {

    private List<OnBoardingItem> onBoardingItems;

    public OnBoardingAdapter(List<OnBoardingItem> onBoardingItems) {
        this.onBoardingItems = onBoardingItems;
    }

    @NonNull
    @Override
    public OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instrucciones,parent,false);
        return new OnBoardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewHolder holder, int position) {
        holder.setOnBoardingData(onBoardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onBoardingItems.size();
    }

    class OnBoardingViewHolder extends RecyclerView.ViewHolder{
        private TextView titulo,descripcion;
        private ImageView imagen;
        OnBoardingViewHolder(@NonNull View itemView){
            super(itemView);
            titulo = itemView.findViewById(R.id.tv_titulo);
            descripcion = itemView.findViewById(R.id.tv_descripcion);
            imagen = itemView.findViewById(R.id.img_imagen);
        }
        void setOnBoardingData(OnBoardingItem onBoardingItem){
            titulo.setText(onBoardingItem.getTitle());
            descripcion.setText(onBoardingItem.getDescription());
            imagen.setImageResource(onBoardingItem.getImagen());
        }
    }
}
