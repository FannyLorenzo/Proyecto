package com.example.proyecto.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import static android.widget.Toast.LENGTH_SHORT;


public class EntrenamientoActivity extends AppCompatActivity implements IEntrenamiento.view,NumberPicker.OnValueChangeListener {


    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
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
    boolean siempre = true, isOn = false, isStop = false,detener = false;
    Thread thread;

    int seg=0,minuts=0,hour=0,milis=0;
    int seg2,minuts2,hour2;

    Handler h = new Handler();
    TextView segs, minutos, hours, estadist_minuts;
    String s = ":00", m = ":00", ho = "00";
    Double distanciaTotal = 0.0;
    // ubicacion
    int nrofragment = 0;
    LocationService myLocationService;
    boolean isBindLocation = false;
    Double latitud = 1.0;
    Double longitud = 1.0;
    TextView txt_latitud, txt_longitud;
    boolean isSecure =true;

    ArrayList<Ubicacion> recorrido = new ArrayList<>();

    Double caloriasTotal = 0.0;

    static Dialog d ;
    private TextView tv;
    int peso;
    TextView cals,dist;

    Context mContext=this;
    FirebaseAuth usuario;
    DatabaseReference dataBase;

    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento);

        // Inicialización de botones a fragments
        actividad = (Button) findViewById(R.id.actividad_fragmentButton);
        mapa = (Button) findViewById(R.id.mapa_fragmentButton);
        estadisticas = (Button) findViewById(R.id.estadisticas_fragmentButton);

        //
        usuario = FirebaseAuth.getInstance();
        dataBase = FirebaseDatabase.getInstance().getReference();

        // Inicialización de fragments
        actividadFragment = new Actividad_Fragment();
        estadisticasFragment = new Estadisticas_Fragment();
        mapaFragment = new Mapa_Fragment();
        ubicacionfragment = new Ubicacion_Fragment();
        //music_fragment = new Music_Fragment();

        // servicio de localización
        Intent serviceIntent = new Intent(this, LocationService.class);
        bindService(serviceIntent, MConnection, Context.BIND_AUTO_CREATE);
        // ubicacion inicial
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

       // if(nrofragment==1) {
         //   txt_latitud.setText(latitud + " (inicial)");
          //  txt_longitud.setText(longitud + " (inicial)");
        //} obtenerUbicacionInicial();

        //cronometro
        obtenerUbicacionInicial();
        cronometro();

        getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment,actividadFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment,estadisticasFragment);

        peso();



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
                bundle.putDouble("cals",caloriasTotal);
                bundle.putInt("minuto1",minuts);
                bundle.putDouble("dist",distanciaTotal);
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
                    isStop = actividadFragment.getIsStop();

                    if(isOn && !isStop){

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        /// Ubicacion
                        iniciarRecorrido();
                        latitud = myLocationService.getLatitud();
                        longitud = myLocationService.getLongitud();

                        seg = seg +1-(int) control;
                        if (seg == 60){
                            minuts++;
                            seg = 0;
                        }

                        if (minuts == 60){
                            hour++;
                            minuts = 0;
                        }


                        if(seg%5==0){ // para crear el arreglo de ubicaciones
                            recorrido.add(new Ubicacion(latitud,longitud));
                            calorias();
                            System.out.println("********************agregados "+recorrido.size()+ " --> "+ latitud +"/nLongitud "+longitud);
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

                                System.out.println(s);
                                segs = (TextView) actividadFragment.getView().findViewById(R.id.seg_TextView);

                                minutos = (TextView) actividadFragment.getView().findViewById(R.id.minut_TextView);
                                hours = (TextView) actividadFragment.getView().findViewById(R.id.hour_TextView);
                                stopView = (ImageView) actividadFragment.getView().findViewById(R.id.stop);
                                stopView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        detener = true;
                                        registerRecordatorio();
                                        System.out.println("*********P/PARAR");
                                        actividadFragment.parar(false);
                                        isStop = true;
                                        seg=0;minuts=0;hour=0;
                                        s = ":00";
                                        m = ":00";
                                        ho = "00";
                                        distanciaTotal = 0.0;
                                        caloriasTotal = 0.0;
                                        segs = (TextView) actividadFragment.getView().findViewById(R.id.seg_TextView);
                                        segs.setText(s);
                                        minutos = (TextView) actividadFragment.getView().findViewById(R.id.minut_TextView);
                                        hours = (TextView) actividadFragment.getView().findViewById(R.id.hour_TextView);

                                        minutos.setText(m);
                                        hours.setText(ho);
                                        //isOn = false;

                                        imageView = (ImageView) actividadFragment.getView().findViewById(R.id.play_pause);
                                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.avd_pause_to_play));
                                            Drawable drawable = imageView.getDrawable();
                                         if (actividadFragment.getNumber() == 1){
                                             if (drawable instanceof AnimatedVectorDrawableCompat){
                                                 avd = (AnimatedVectorDrawableCompat) drawable;
                                                 avd.start();
                                             }else if (drawable instanceof AnimatedVectorDrawable){
                                                 avd2 = (AnimatedVectorDrawable) drawable;
                                                 avd2.start();
                                             }
                                             actividadFragment.setNumber();
                                         }

                                         takePhoto();

                                    }
                                });
                                //detener = actividadFragment.getBool();
                                segs.setText(s);
                                minutos.setText(m);
                                hours.setText(ho);
                                if (detener){
                                    s = ":00";
                                    m = ":00";
                                    ho = "00";
                                    segs.setText(s);
                                    minutos.setText(m);
                                    hours.setText(ho);
                                    detener = false;
                                }

                                // ubicacion - actualización de valores en fragment
                                minutos.setText(m);
                                hours.setText(ho);
                                // ubicacion - actualización de valores en fragment Ubicacion

                                if(nrofragment==1) {
                                    System.out.println(" **** actualizando en fragment ubicacion");
                                    txt_latitud = (TextView) ubicacionfragment.getView().findViewById(R.id.txt_latitud);
                                    txt_longitud = (TextView) ubicacionfragment.getView().findViewById(R.id.txt_longitud);
                                    txt_latitud.setText(String.valueOf(latitud));
                                    txt_longitud.setText(String.valueOf(longitud));
                                    if(latitud!=0 && longitud!=00)
                                    mapaFragment.getPoints().add(new LatLng(latitud,longitud));
                                }

                                if(nrofragment==2) {
                                    System.out.println(" **** actualizando en fragment estadisticas");
                                    cals = (TextView)estadisticasFragment.getView().findViewById(R.id.cal_est);
                                    dist =  (TextView)estadisticasFragment.getView().findViewById(R.id.dist_est);
                                    cals.setText(""+caloriasTotal);
                                    dist.setText(""+distanciaTotal);
                                }

                            }
                        });
                    }else{//isOn es false / si está pausado etc, esto hay q ver
                        pararRecorrido(); // le puse para ver no mas
                        if(isStop){
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    seg=0;minuts=0;hour=0;
                                    s = ":00";
                                    m = ":00";
                                    ho = "00";
                                    segs = (TextView) actividadFragment.getView().findViewById(R.id.seg_TextView);
                                    segs.setText(s);
                                    minutos = (TextView) actividadFragment.getView().findViewById(R.id.minut_TextView);
                                    hours = (TextView) actividadFragment.getView().findViewById(R.id.hour_TextView);

                                    minutos.setText(m);
                                    hours.setText(ho);

                                }});
                            // reseteando array de recorrido una vez es STOP
                            recorrido = new ArrayList<Ubicacion>();
                            isOn = false;
                        }

                    }
                }
            }
        });
        thread.start();
    }


    public void iniciarRecorrido() {
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

    public void pararRecorrido(){
        stopLocationService();
    }

    // Añadidos para la ubicacion
    public boolean isLocationServiceRunning(){
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

    public void startLocationService(){
        if(!isLocationServiceRunning()) {
            Intent intent = new Intent(this.getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);

            //Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show();


        }else {
            //Toast.makeText(this,"Location service is already  started", Toast.LENGTH_SHORT).show();
        } }
    public void stopLocationService(){
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
        double startLat = recorrido.get(recorrido.size()-1).getLatitud();
        double startLong = recorrido.get(recorrido.size()-1).getLongitud();
        double endLat = recorrido.get(recorrido.size()-1).getLatitud();
        double endLong = recorrido.get(recorrido.size()-1).getLongitud();

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        distanciaTotal = distanciaTotal+EARTH_RADIUS * c;
        distanciaTotal = Math.round(distanciaTotal*100.0)/100.0;
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
                pararRecorrido();
                finish();
                 // agregado por FL
            }
        });

    }

    public void takePhoto(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Sonrie :D")
                .setMessage("¿Quisieras tomarte una foto?")
                .setPositiveButton("Si", null)
                .setNegativeButton("No", null)
                .show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EntrenamientoActivity.this, CamaraActivity.class));
            }
        });
    }

    public void calorias(){

        double dist = distancia();
        dist = dist*(double) 1000;
        double MET = 0;

        //dist = distanciaTotal*1000;
        //dist = distanciaTotal/(double)(seg+minuts*60+hour*3600);
        //double velocidad = dist/(double)(seg+minuts*60+hour*3600);
        double velocidad = dist/(double)5;
        velocidad = velocidad*(double)3600/(double)1000;
        if (velocidad >=0 && velocidad <= 1.5){
            MET = 1;
        }else if(velocidad >1.5 && velocidad <=2.5){
            MET = 1.5;
        }else if (velocidad > 2.5 && velocidad < 3){
            MET = 2;
        }else if(velocidad >= 3 && velocidad <= 4.5){
            MET = 3.3;
        }else if(velocidad > 4.5 && velocidad <= 5.3){
            MET = 3.8;
        }else if (velocidad > 5.3 && velocidad <6.4){
            MET = 4;
        }else if (velocidad >= 6.4 && velocidad < 8.4){
            MET = 5;
        }else if(velocidad >= 8.4 && velocidad <9.6){
            MET = 9;
        }else if (velocidad >= 9.6 && velocidad <10.8){
            MET = 10;
        }else if (velocidad >= 10.8 && velocidad < 11.3){
            MET = 11;
        }else if (velocidad >= 11.3 && velocidad < 12.1){
            MET = 11.5;
        }else if (velocidad >= 12.1 && velocidad < 12.9){
            MET = 12.5;
        }else if (velocidad >= 12.9 && velocidad < 13.8){
            MET = 13.5;
        }else if (velocidad >= 13.8 && velocidad < 14.5){
            MET = 14;
        }else{
            MET = 16.5;
        }
        double cal = MET*peso*0.0175;
        cal = cal/(double)12;
        cal = Math.round(cal*100.0)/100.0;
        caloriasTotal = cal+caloriasTotal;
        caloriasTotal = Math.round(caloriasTotal*100.0)/100.0;
    }

    public void peso(){
        final Dialog d = new Dialog(EntrenamientoActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(150); // max value 100
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //tv.setText(String.valueOf(np.getValue())); //set the value to textview
                peso = np.getValue();
                System.out.println("**************/"+peso);
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                finish();
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        Log.i("value is",""+i1);
    }

    private void registerRecordatorio() {
        String date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        double velocidad = distanciaTotal/(double)(seg+minuts*60+hour*3600);
        velocidad = velocidad*(double)3600/(double)1000;
        velocidad = Math.round(velocidad*100.0)/100.0;
        String id = "VQf0aFsW0ib8TMD7qGPnbEE7oII2";//Objects.requireNonNull(usuario.getCurrentUser()).getUid();
        System.out.println(" cual es el id "+ id);
        //map
        Map<String, Object> map = new HashMap<>();
        map.put("fecha", date);
        map.put("tiempo", ""+(seg+minuts*60+hour*3600));
        map.put("distancia", distanciaTotal);
        map.put("velocidad", velocidad);
        map.put("calorias", caloriasTotal);
        map.put("ubicacion",recorrido);
        //map.put("estado_registro", EstRegistro);

        // creacion de usuario en la base de datos
        dataBase.child("Usuario").child(id).child("Entrenamiento").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EntrenamientoActivity.this, "El recordatorio se ha creado correctamente", LENGTH_SHORT).show();
                //startActivity(new Intent(RecordatorioActivity.this, AllRecordatorioActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EntrenamientoActivity.this, "Hubo un error al tratar de guardar los datos", LENGTH_SHORT).show();

            }
        });


    }
    public void obtenerUbicacionInicial() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.REQUEST_PERMISSION_UBICACION);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();
                            System.out.println(" LATITUDES INICIALES " + latitud + " - " + longitud);
                            // txt_latitud = (TextView) ubicacionfragment.getView().findViewById(R.id.txt_latitud);
                            //txt_longitud = (TextView) ubicacionfragment.getView().findViewById(R.id.txt_longitud);
                            isSecure=true;

                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Aqui hay un problema, no funcionó el fusedlocation");
            }
        });

    }

}
