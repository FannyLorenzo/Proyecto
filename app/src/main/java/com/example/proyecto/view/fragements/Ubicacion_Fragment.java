package com.example.proyecto.view.fragements;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.R;
import com.example.proyecto.model.Constants;
import com.example.proyecto.model.LocationService;
import com.example.proyecto.view.EntrenamientoActivity;


public class Ubicacion_Fragment extends Fragment {
    View view;
    //TextView tvMensaje;
    Double latitud = 1.0;
    Double longitud = 1.0;
   @SuppressLint("StaticFieldLeak")
   public static TextView txt_latitud;
    @SuppressLint("StaticFieldLeak")
    public static TextView txt_longitud;

    private  static final int REQUEST_CODE_LOCATION_PERMISSION=1;

    //  Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button start;
    private Button stop;

    public Ubicacion_Fragment() {
        // Required empty public constructor
    }

    public static Ubicacion_Fragment newInstance(String param1, String param2) {
        Ubicacion_Fragment fragment = new Ubicacion_Fragment();
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
            latitud = getArguments().getDouble("latitud",0.0);
            longitud = getArguments().getDouble("longitud",0.0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ubicacion_, container, false);
        txt_latitud = view.findViewById(R.id.txt_latitud);
        txt_latitud.setText(String.valueOf(latitud));

        txt_longitud = view.findViewById(R.id.txt_longitud);
        txt_longitud.setText(String.valueOf(longitud));

        mapa(latitud,longitud);

        start = view.findViewById(R.id.btn_startLocation);
        stop = view.findViewById(R.id.btn_EndLocation);

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   iniciarRecorrido();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pararRecorrido();
                }
            });

        return view;
    }

    private void iniciarRecorrido() {
        if (ContextCompat.checkSelfPermission(
                getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
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
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null ){
            for(ActivityManager.RunningServiceInfo service:
                    activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(LocationService.class.getName().equals(service.service.getClassName())){
                    if(service.foreground){

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
            Intent intent = new Intent(getActivity().getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            getActivity().startService(intent);

            Toast.makeText(getActivity(), "Location service started", Toast.LENGTH_SHORT).show();


        }else
            Toast.makeText(getActivity(),"Location service is already  started", Toast.LENGTH_SHORT).show();
    }
    private void stopLocationService(){
        if(isLocationServiceRunning()){
            Intent intent = new Intent(getActivity().getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            getActivity().startService(intent);
            Toast.makeText(getActivity(),"Location service stopped", Toast.LENGTH_SHORT).show();

        }else
            Toast.makeText(getActivity(),"Location service is already stopped", Toast.LENGTH_SHORT).show();
    }

    public double getLati() {
    return latitud;
    }

    public double getLongi() {
    return  longitud;
    }

    public TextView getTxt_latitud() {
        return txt_latitud;
    }

    public TextView getTxt_longitud() {
        return txt_longitud;
    }
    @SuppressLint("SetTextI18n")


    public View getView(){
        return view;
    }

    public void mapa(double lat, double lon) {
        Mapa_Fragment mapaFragment = new Mapa_Fragment();

        Bundle bundle = new Bundle();
        bundle.putDouble("lat", lat);
        bundle.putDouble("lon", lon);
        mapaFragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.map, mapaFragment, null);
        fragmentTransaction.commit();
    }

    // Ubicacionn
    LocationService myLocationService;
    boolean isBindLocation = false;
    private ServiceConnection MConnection = new ServiceConnection( ) {
        public void onServiceConnected(ComponentName name, IBinder service) {

            LocationService.LocalService localService = (LocationService.LocalService) service;
            myLocationService = localService.getService();
            isBindLocation = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            isBindLocation = false;
        }
    };


}