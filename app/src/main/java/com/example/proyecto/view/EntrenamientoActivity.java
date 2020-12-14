package com.example.proyecto.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.R;
import com.example.proyecto.interfaces.IEntrenamiento;
import com.example.proyecto.model.Constants;
import com.example.proyecto.model.LocationService;
import com.example.proyecto.model.Ubicacion;
import com.example.proyecto.view.fragements.Actividad_Fragment;
import com.example.proyecto.view.fragements.Estadisticas_Fragment;
import com.example.proyecto.view.fragements.Mapa_Fragment;
import com.example.proyecto.view.fragements.Ubicacion_Fragment;
import com.google.android.gms.common.util.concurrent.HandlerExecutor;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

public class EntrenamientoActivity extends AppCompatActivity implements IEntrenamiento.view{

    private  static final int REQUEST_CODE_LOCATION_PERMISSION=1;
    private static final int EARTH_RADIUS = 6371;
    Actividad_Fragment actividadFragment;
    Estadisticas_Fragment estadisticasFragment;
    Mapa_Fragment mapaFragment;  // aqui
    Ubicacion_Fragment ubicacionfragment;
    //Music_Fragment music_fragment;
    FragmentTransaction fragmentTransaction;
    Button actividad, mapa, estadisticas;

    ImageView imageView, stopView;
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;
    int switchNumber = 0;
    View view;
    double control = 0;
    boolean siempre = true,isOn = false;
    Thread thread;
    int seg=0,minuts=0,hour=0;
    int seg2,minuts2,hour2;
    Handler h = new Handler();
    TextView segs,minutos,hours,estadist_minuts;
    String s = ":00",m =":00", ho="00";
    Double distanciaTotal = 0.0;
    // ubicacion
    int nrofragment=0;
    LocationService myLocationService;
    boolean isBindLocation = false;
    Double latitud = 1.0;
    Double longitud = 1.0;
    TextView txt_latitud, txt_longitud;

    ArrayList<Ubicacion> recorrido = new ArrayList<>();


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

        // servicio de localización
        Intent serviceIntent = new Intent(this,LocationService.class);
        bindService(serviceIntent, MConnection, Context.BIND_AUTO_CREATE);
        iniciarRecorrido();

        //cronometro
        cronometro();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment,actividadFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment,estadisticasFragment);



    }

    // Cambios entre los fragments
    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (view.getId()){
            case R.id.actividad_fragmentButton:
                nrofragment=0;
                Bundle bundle2 = new Bundle();
                bundle2.putString("segundo1",s);
                bundle2.putString("minuto1",m);
                bundle2.putString("hora1",ho);
                actividadFragment.setArguments(bundle2);
                fragmentTransaction.replace(R.id.activity_fragment,actividadFragment);
                break;
            case R.id.mapa_fragmentButton:
                nrofragment=1;
                Bundle bundle3 = new Bundle();
                bundle3.putDouble("latitud", latitud);
                bundle3.putDouble("longitud",longitud);
                ubicacionfragment.setArguments(bundle3);
                fragmentTransaction.replace(R.id.activity_fragment,ubicacionfragment);

                break;
            case R.id.estadisticas_fragmentButton:
                nrofragment=2;
                Bundle bundle = new Bundle();
                bundle.putInt("segundo1",actividadFragment.getSeg());
                bundle.putInt("minuto1",minuts);
                bundle.putInt("hora1",actividadFragment.getHour());
                estadisticasFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.activity_fragment,estadisticasFragment);
                //actividadFragment.parar(false); /// aqui algo
                actividadFragment.changeAnimation();
                break;
        }
        fragmentTransaction.commit();
    }

    public void cronometro(){

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (siempre){
                    isOn = actividadFragment.getBool();

                    if(isOn){

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (seg == 1 && minuts == 0 && hour == 0){
                            System.out.println("*******************//"+distanciaTotal);
                            latitud = myLocationService.getLatitud();
                            longitud = myLocationService.getLongitud();
                            recorrido.add(new Ubicacion(latitud,longitud));
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

                        /// Ubicacion

                        latitud = myLocationService.getLatitud();
                        longitud = myLocationService.getLongitud();
                        System.out.println("********************aqui mero Latitud "+ latitud +"/nLongitud "+longitud);


                        if(seg%5==0){ // para crear el arreglo de ubicaciones
                            recorrido.add(new Ubicacion(latitud,longitud));
                            System.out.println("********************"+distancia());
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
                               // actualizar();
                                System.out.println(s);
                                segs = (TextView) actividadFragment.getView().findViewById(R.id.seg_TextView);
                                segs.setText(s);
                                minutos = (TextView) actividadFragment.getView().findViewById(R.id.minut_TextView);
                                hours = (TextView) actividadFragment.getView().findViewById(R.id.hour_TextView);
                                minutos.setText(m);
                                hours.setText(ho);
                                // ubicacion - actualización de valores en fragment
                                if(nrofragment==1) {
                                    System.out.println(" **** ENTROOOOOOOO");
                                    txt_latitud = (TextView) ubicacionfragment.getView().findViewById(R.id.txt_latitud);
                                    txt_longitud = (TextView) ubicacionfragment.getView().findViewById(R.id.txt_longitud);
                                    txt_latitud.setText(String.valueOf(latitud));
                                    txt_longitud.setText(String.valueOf(longitud));
                                }

                            }
                        });
                    }else{//isOn es false / si está pausado etc, esto hay q ver
                        //pararRecorrido(); // le puse para ver no mas

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


    private void iniciarRecorrido() {
        if (ContextCompat.checkSelfPermission(
                this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            startLocationService();
        }
    }

    private void pararRecorrido(){
        stopLocationService();
    }

    // Añadidos para la ubicacion
    private boolean isLocationServiceRunning(){
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null ){
            for(ActivityManager.RunningServiceInfo service:
                    activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(LocationService.class.getName().equals(service.service.getClassName())){
                    if(service.foreground){
                        //

                        return true;
                    }
                }
            }
            return false;
        }
        return false;

    }

    private void startLocationService(){
        if(!isLocationServiceRunning()) {
            Intent intent = new Intent(this.getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);

            //Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show();


        }else {
            //Toast.makeText(this,"Location service is already  started", Toast.LENGTH_SHORT).show();
        } }
    private void stopLocationService(){
        if(isLocationServiceRunning()){
            Intent intent = new Intent(this.getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            this.startService(intent);
         //   Toast.makeText(this,"Location service stopped", Toast.LENGTH_SHORT).show();

        }else {
            // Toast.makeText(this,"Location service is already stopped", Toast.LENGTH_SHORT).show();
        }
        }


    private ServiceConnection MConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {

            LocationService.LocalService localService = (LocationService.LocalService) service;
            myLocationService = localService.getService();
            isBindLocation = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            isBindLocation = false;
        }
    };
    /*protected void onStop() {
        super.onStop();
        ///**
        if(isBindLocation){
            unbindService(MConnection);
            isBindLocation = false;
        }
         /**/
    //}

    public double getLati() {
        return latitud;
    }

    public double getLongi() {
        return  longitud;
    }

    public double distancia() {
        double startLat = recorrido.get(recorrido.size()-2).getLatitud();
        double startLong = recorrido.get(recorrido.size()-2).getLongitud();
        double endLat = recorrido.get(recorrido.size()-1).getLatitud();
        double endLong = recorrido.get(recorrido.size()-1).getLongitud();

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        distanciaTotal = distanciaTotal+EARTH_RADIUS * c;
        System.out.println("********************///"+distanciaTotal);
        return EARTH_RADIUS * c; // <-- d
    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
    @Override protected void onStart() {
        super.onStart();
        //Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onBackPressed() {

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Saliendo...")
                .setMessage("¿Seguro que desea salir?")
                .setPositiveButton("Si", null)
                .setNegativeButton("No", null)
                .show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                siempre = false;
                unbindService(MConnection);
                isBindLocation = false;
                finish();
            }
        });
    }
}
