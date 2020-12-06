package com.example.proyecto.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyecto.MainActivity;
import  com.example.proyecto.R;
import com.example.proyecto.interfaces.IEntrenamiento;
import com.example.proyecto.model.Constants;
import com.example.proyecto.model.LocationService;
import com.example.proyecto.view.fragements.*;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EntrenamientoActivity extends AppCompatActivity implements IEntrenamiento.view{

    private  static final int REQUEST_CODE_LOCATION_PERMISSION=1;

    Actividad_Fragment actividadFragment;
    Estadisticas_Fragment estadisticasFragment;
    Mapa_Fragment mapaFragment;  // aqui
    Ubicacion_Fragment ubicacionfragment;
    //Music_Fragment music_fragment;
    FragmentTransaction fragmentTransaction;
    Button actividad, mapa, estadisticas;
    Button start, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento);

        // Inicialización de botones a fragments
        actividad = (Button)findViewById(R.id.actividad_fragmentButton);
        mapa = (Button)findViewById(R.id.mapa_fragmentButton);
        estadisticas = (Button)findViewById(R.id.estadisticas_fragmentButton);

        // Inicialización de fragments
        actividadFragment = new Actividad_Fragment();
        estadisticasFragment = new Estadisticas_Fragment();
        mapaFragment = new Mapa_Fragment();
        ubicacionfragment = new Ubicacion_Fragment();
        //music_fragment = new Music_Fragment();

        // el primer fragment con el que inicia
        getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment,actividadFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment,estadisticasFragment);

        // locaciones en fragment de Mapa



    }

    // Cambios entre los fragments
    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (view.getId()){
            case R.id.actividad_fragmentButton:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("segundo2",estadisticasFragment.getSeg());
                bundle2.putInt("minuto2",estadisticasFragment.getMinut());
                bundle2.putInt("hora2",estadisticasFragment.getHour());
                actividadFragment.setArguments(bundle2);
                fragmentTransaction.replace(R.id.activity_fragment,actividadFragment);
                break;
            case R.id.mapa_fragmentButton:
                // LE PUSE ESTE BUNDLE como en los otros fragments , PEROO NO CREO Q LO UTILIZE
                Bundle bundle3 = new Bundle();
                actividadFragment.setArguments(bundle3);
                fragmentTransaction.replace(R.id.activity_fragment,ubicacionfragment);


               break;
            case R.id.estadisticas_fragmentButton:
                Bundle bundle = new Bundle();
                bundle.putInt("segundo1",actividadFragment.getSeg());
                bundle.putInt("minuto1",actividadFragment.getMinuts());
                bundle.putInt("hora1",actividadFragment.getHour());
                estadisticasFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.activity_fragment,estadisticasFragment);
                actividadFragment.parar(false);
                actividadFragment.changeAnimation();
                break;
        }
        fragmentTransaction.commit();
    }



}