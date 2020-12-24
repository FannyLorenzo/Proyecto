package com.example.proyecto.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyecto.R;

import com.example.proyecto.model.Recorrido;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecorridoAdapter extends BaseAdapter {
    private ArrayList<Recorrido> listEntidad = new ArrayList<Recorrido>();
    private Context context;
    private LayoutInflater inflater;

    public RecorridoAdapter (Context context, ArrayList<Recorrido> listEntidad) {
        this.context = context;
        this.listEntidad = listEntidad;
    }

    @Override
    public int getCount() {
        return listEntidad.size();
    }

    @Override
    public Object getItem(int position) {
        return listEntidad.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // OBTENER EL OBJETO POR CADA ITEM A MOSTRAR
        final Recorrido entidad = (Recorrido) getItem(position);

        // CREAMOS E INICIALIZAMOS LOS ELEMENTOS DEL ITEM DE LA LISTA
        convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
        ImageView imgFoto = (ImageView) convertView.findViewById(R.id.imgFoto);
        TextView tvTitulo = (TextView) convertView.findViewById(R.id.tvTitulo);
        TextView tvContenido = (TextView) convertView.findViewById(R.id.tvContenido);

        // LLENAMOS LOS ELEMENTOS CON LOS VALORES DE CADA ITEM
        imgFoto.setImageResource(R.drawable.caminata);
        tvTitulo.setText(entidad.getFecha());
        tvContenido.setText(entidad.getTiempo());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent i = new Intent(context, DetalleLista.class);
               // i.putExtra("item", entidad);
                //context.startActivity(i);
                System.out.println("Click !!");
            }
        });

        return convertView;
    }
}