package com.example.proyecto.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import  com.example.proyecto.R;
import com.example.proyecto.interfaces.IEntrenamiento;
import com.example.proyecto.interfaces.IUsuario;
import com.example.proyecto.view.fragements.*;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EntrenamientoActivity extends AppCompatActivity implements IEntrenamiento.view{
    Actividad_Fragment actividadFragment;
    Estadisticas_Fragment estadisticasFragment;
    //Music_Fragment music_fragment;
    FragmentTransaction fragmentTransaction;
    Button actividad, mapa, estadisticas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento);
        actividad = (Button)findViewById(R.id.actividad_fragmentButton);
        mapa = (Button)findViewById(R.id.mapa_fragmentButton);
        estadisticas = (Button)findViewById(R.id.estadisticas_fragmentButton);
        actividadFragment = new Actividad_Fragment();
        estadisticasFragment = new Estadisticas_Fragment();
        //music_fragment = new Music_Fragment();
        //fragmentTransaction = getSupportFragmentManager().beginTransaction();

        getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment,actividadFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment,estadisticasFragment);
        //getSupportFragmentManager().beginTransaction().add(R.id.music_fragment,music_fragment).commit();

    }
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