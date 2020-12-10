package com.example.proyecto.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.example.proyecto.MainActivity;
import  com.example.proyecto.R;
import com.example.proyecto.interfaces.IEntrenamiento;
import com.example.proyecto.model.Constants;
import com.example.proyecto.model.LocationService;
import com.example.proyecto.view.fragements.*;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

    ImageView imageView, stopView;
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;
    int switchNumber = 0;
    View view;
    double control = 0;
    boolean isOn = false;
    Thread thread;
    int seg=0,minuts=0,hour=0;
    int seg2,minuts2,hour2;
    Handler h = new Handler();
    TextView segs,minutos,hours,estadist_minuts;


    String s = ":00",m =":00", ho="00";

    /******************************************/
    Double latitud = 0.0;
    Double longitud = 0.0;
    TextView latitud_F,longitu_F;
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
        LocationService local = new LocationService();

        cronometro();
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
                bundle2.putString("segundo1",s);
                bundle2.putString("minuto1",m);
                bundle2.putString("hora1",ho);
                actividadFragment.setArguments(bundle2);
                fragmentTransaction.replace(R.id.activity_fragment,actividadFragment);
                break;
            case R.id.mapa_fragmentButton:
                // LE PUSE ESTE BUNDLE como en los otros fragments , PEROO NO CREO Q LO UTILIZE
                Bundle bundle3 = new Bundle();
                bundle3.putDouble("latitud",latitud);
                bundle3.putDouble("longitud",longitud);
                actividadFragment.setArguments(bundle3);
                //
                fragmentTransaction.replace(R.id.activity_fragment,ubicacionfragment);
               break;
            case R.id.estadisticas_fragmentButton:
                Bundle bundle = new Bundle();
                bundle.putInt("segundo1",actividadFragment.getSeg());
                bundle.putInt("minuto1",minuts);
                bundle.putInt("hora1",actividadFragment.getHour());
                estadisticasFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.activity_fragment,estadisticasFragment);
                //actividadFragment.parar(false);
                actividadFragment.changeAnimation();
                break;
        }
        fragmentTransaction.commit();
    }

    public void cronometro(){

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    isOn = actividadFragment.getBool();
                    if(isOn){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        seg = seg +1-(int) control;
                        if (seg == 60){
                            minuts++;
                            seg = 0;
                        }

                        if (minuts == 60){
                            hour++;
                            minuts = 0;
                        }
                        h.post(new Runnable() {
                            @Override
                            public void run() {

                                if (seg < 10){
                                    s = ":0"+String.valueOf(seg);
                                }else{
                                    s = ":"+seg;
                                }
                                if (minuts < 10){
                                    m = ":0"+minuts;
                                }else{
                                    m = ":"+minuts;
                                }
                                if (hour < 10){
                                    ho = "0"+hour;
                                }else{
                                    ho = ""+hour;
                                }
                                actualizar();
                                System.out.println(s);
                                segs = (TextView) actividadFragment.getView().findViewById(R.id.seg_TextView);
                                segs.setText(s);
                                minutos = (TextView) actividadFragment.getView().findViewById(R.id.minut_TextView);
                                hours = (TextView) actividadFragment.getView().findViewById(R.id.hour_TextView);
                                minutos.setText(m);
                                hours.setText(ho);
                                /*segs.setText(s);
                                minutos.setText(m);
                                hours.setText(ho);*/
                            }
                        });
                    }
                }
            }
        });
        thread.start();
    }

    public void actualizar(){

        Bundle bundle = new Bundle();
        bundle.putString("segundo1",s);
        bundle.putString("minuto1",m);
        bundle.putString("hora1",ho);
        estadisticasFragment.setArguments(bundle);
    }

    public void ubicacion(){
        LocationCallback locationCallback = new LocationCallback() {

            @SuppressLint("SetTextI18n")
            public void onLocationResult(LocationResult locationResult) {

                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null){
                    latitud = locationResult.getLastLocation().getLatitude();
                    longitud = locationResult.getLastLocation().getLongitude();
                    Log.d("LOCATION_UPDATE", latitud+ ", "+longitud);
                    // txt_latitud.setText(""+latitud);
                    //txt_longitud.setText(""+longitud);
                    // ESTOOOOOOO QUIERO QUE  ACTUALIZE EN EL .XML DE UBICACION_FRAGMENT, PERO NO ME SALEEEEEEEEE :cccccc
                    //  mapa(-16.3944068, -71.5021534);
                    // mapa(latitud, longitud);
                }
            }
        };
    }

}