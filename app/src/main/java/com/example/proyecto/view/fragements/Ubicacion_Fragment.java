package com.example.proyecto.view.fragements;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
    private  static final int REQUEST_CODE_LOCATION_PERMISSION=1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button start;
    private Button stop;
    double lati, longi;
    TextView latitud, longitud;

    public Ubicacion_Fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
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
            lati = getArguments().getDouble("latitud",0.0);
            longi = getArguments().getDouble("longitud",0.0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ubicacion_, container, false);
        latitud = (TextView) view.findViewById(R.id.txt_latitud);
        longitud = (TextView) view.findViewById(R.id.txt_longitud);

        latitud.setText(String.valueOf(lati));
        longitud.setText(String.valueOf(longi));

        // paso de datos
        Bundle datos = getArguments();
        if(datos != null){
            lati = datos.getDouble("latitud");
            longi = datos.getDouble("longitud");
        }

        start = view.findViewById(R.id.btn_startLocation);
        stop = view.findViewById(R.id.btn_EndLocation);

       /// ESTO NO, TENGO Q ARREGLARRR
            // Aqui maso para q funcione yeah
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopLocationService();
                }
            });
        
        
        
        return view;
    }

    // Añadidos para la ubicacion
    private boolean isLocationServiceRunning(){
        ActivityManager activityManager =
                (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
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

            LocationService local = new LocationService();
            local.setMainActivity((EntrenamientoActivity) getActivity(), latitud, longitud);

            Toast.makeText(getActivity(), "Location service started", Toast.LENGTH_SHORT).show();
        // aqui añadi para que modifique haber si funciona
            //latitud.setText(" "+getLati());
            //longitud.setText(" "+getLongi());
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
    // esto está mal
    public void mapa(double lat, double lon) {
        // Fragment del Mapa
        Ubicacion_Fragment fragment = new Ubicacion_Fragment();

        Bundle bundle = new Bundle();
        bundle.putDouble("latitud", lat);
        bundle.putDouble("longitud", lon);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.map, fragment, null);
        fragmentTransaction.commit();
    }

    public double getLati() {
    return lati;
    }

    public double getLongi() {
    return  longi;
    }
}