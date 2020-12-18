package com.example.proyecto.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.R;
import com.example.proyecto.model.Entrenamiento;

import java.util.ArrayList;

public class EntrenamientoAdapter extends RecyclerView.Adapter<EntrenamientoAdapter.ViewHolder>{
    private ArrayList<Entrenamiento> recordatoriosList;
    private Context context;
    private int resource;

    public EntrenamientoAdapter(ArrayList<Entrenamiento> recordatoriosList, Context resource) {

        this.recordatoriosList = recordatoriosList;
        this.context = resource;
    }

    @NonNull
    @Override // enlaza el adaptador con la list
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View  view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.entrenamiento_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entrenamiento recordatorio = recordatoriosList.get(position);
        holder.txt_fecha.setText(recordatorio.getFecha());
        holder.txt_tiempo.setText(recordatorio.getTiempo());
        // holder.txt_tipo_cuidado.setText(recordatorio.getTipoCat());
        //holder.txt_cuidado.setText(recordatorio.getCodCuidado());
        holder.txt_distancia.setText(recordatorio.getDistancia());
        holder.txt_velociada.setText(recordatorio.getVelocidad());
        holder.txt_calorias.setText(recordatorio.getCalorias());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_fecha;
        private TextView txt_tiempo;
        private TextView txt_distancia;
        private TextView txt_velociada;
        private TextView txt_calorias;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_fecha = (TextView) itemView.findViewById(R.id.fecha);
            txt_tiempo = (TextView) itemView.findViewById(R.id.tiempo);
            txt_distancia = (TextView) itemView.findViewById(R.id.distancia);
            txt_velociada = (TextView) itemView.findViewById(R.id.velocidad);
            txt_calorias = (TextView) itemView.findViewById(R.id.calorias);
        }
    }
}
