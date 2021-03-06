package com.example.proyecto.view.fragements;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.Thread;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.example.proyecto.R;
import com.example.proyecto.model.DataBaseEntrenamiento;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Actividad_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Actividad_Fragment extends Fragment {
    ImageView imageView, stopView;
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;
    int switchNumber = 0;
    View view;
    double control = 0;
    boolean isOn = false;
    boolean isStop = false;
    boolean detener = false;
    Thread thread;
    int seg=0,minuts=0,hour=0;
    String seg2=":00",minuts2=":00",hour2="00";
    String tiempo, distancia, velocidad, calorias;
    Handler h = new Handler();
    TextView segs,minutos,hours;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int x = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Actividad_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Actividad_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Actividad_Fragment newInstance(String param1, String param2) {
        Actividad_Fragment fragment = new Actividad_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            seg2 = getArguments().getString("segundo1", ":00");
            minuts2 = getArguments().getString("minuto1", ":00");
            hour2 = getArguments().getString("hora1", "00");
            //tiempo = getArguments().getString()
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_actividad_,container,false);
        segs = (TextView)view.findViewById(R.id.seg_TextView);
        minutos = (TextView)view.findViewById(R.id.minut_TextView);
        hours = (TextView)view.findViewById(R.id.hour_TextView);
        minutos.setText(minuts2);
        hours.setText(hour2);
        segs.setText(seg2);


        imageView = (ImageView) view.findViewById(R.id.play_pause);
        if (isOn){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.avd_play_to_pause));
            Drawable drawable = imageView.getDrawable();

            if (drawable instanceof AnimatedVectorDrawableCompat){
                avd = (AnimatedVectorDrawableCompat) drawable;
                avd.start();
            }else if (drawable instanceof AnimatedVectorDrawable){
                avd2 = (AnimatedVectorDrawable) drawable;
                avd2.start();
            }
            switchNumber++;
        }

        playPause();

        stopView = (ImageView) view.findViewById(R.id.stop);
        stopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStop) {
                    System.out.println("*********P/PARAR2");
                    isStop = true;
                    isOn = false;
                    //guardarDatos();
                }
            }
        });



        return view;
    }

    public void playPause(){
        //setContentView(R.layout.fragment_actividad_);
        //ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_actividad_,container,null);
        imageView = (ImageView) view.findViewById(R.id.play_pause);

        hours = (TextView) view.findViewById(R.id.hour_TextView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchNumber == 0){
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.avd_play_to_pause));
                    Drawable drawable = imageView.getDrawable();

                    if (drawable instanceof AnimatedVectorDrawableCompat){
                        avd = (AnimatedVectorDrawableCompat) drawable;
                        avd.start();
                    }else if (drawable instanceof AnimatedVectorDrawable){
                        avd2 = (AnimatedVectorDrawable) drawable;
                        avd2.start();
                    }
                    System.out.println("*****************/INICIO");
                    isOn = true;
                    isStop = false;

                    switchNumber++;
                }else {
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.avd_pause_to_play));
                    Drawable drawable = imageView.getDrawable();

                    if (drawable instanceof AnimatedVectorDrawableCompat){
                        avd = (AnimatedVectorDrawableCompat) drawable;
                        avd.start();
                    }else if (drawable instanceof AnimatedVectorDrawable){
                        avd2 = (AnimatedVectorDrawable) drawable;
                        avd2.start();
                    }
                    isOn = false;
                    switchNumber--;
                }
            }
        });
    }

    public void guardarDatos(){
        String carrera = "Carrera";
        String date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());

        DataBaseEntrenamiento baseDatos = new DataBaseEntrenamiento(getContext(), "DEMODB",null, 1);

        SQLiteDatabase db = baseDatos.getWritableDatabase();
        if (db != null){
            ContentValues registroNuevo = new ContentValues();
            registroNuevo.put("entrenamiento", carrera);
            registroNuevo.put("fecha", date);
            registroNuevo.put("recorrido", "5");

            db.insert("Entrenamiento", null, registroNuevo);
            Toast.makeText(getContext(), "Datos Almacenados", Toast.LENGTH_SHORT).show();
        }
    }
    public void actualizar(){
        //while (true) {


        segs = (TextView) view.findViewById(R.id.seg_TextView);
        minutos = (TextView) view.findViewById(R.id.minut_TextView);
        hours = (TextView) view.findViewById(R.id.hour_TextView);
        segs.setText(seg2);
        minutos.setText(minuts2);
        hours.setText(hour2);
        //}
    }

    public void parar(boolean f){
        isOn = f;
    }
    public int getSeg(){
        return seg;
    }
    public int getMinuts(){
        return minuts;
    }
    public int getHour(){
        return hour;
    }
    public void changeAnimation(){
        if (switchNumber == 1){
            switchNumber--;
        }
    }
    public void setNumber(){
        switchNumber--;
    }
    public int getNumber(){
        return  switchNumber;
    }
    public boolean getBool(){
        return isOn;
    }
    public boolean getIsStop(){
        return isStop;
    }
    public void setControl(){
        control=control+0.5;
    }

    public View getView(){
        return view;
    }

    public void setTiempo(String tiempo){
        this.tiempo = tiempo;
    }
    public void setDistancia(String distancia){
        this.distancia = distancia;
    }
    public void setVelocidad(String velocidad){
        this.velocidad = velocidad;
    }
    public void setCalorias(String calorias){
        this.calorias = calorias;
    }
}