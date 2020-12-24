package com.example.proyecto.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.proyecto.R;
import com.example.proyecto.interfaces.IUbicacion;
import com.example.proyecto.model.Recorrido;
import com.example.proyecto.model.Ubicacion;
import com.example.proyecto.presenter.PermisosPresenter;
import com.example.proyecto.presenter.UbicacionPresenter;
import com.example.proyecto.presenter.UsuarioPresentador;
import com.example.proyecto.view.adapter.RecorridoAdapter;
import com.example.proyecto.view.fragements.Mapa_Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class UbicacionActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_UBICACION = 111;
    private Button btn_GPS;
    double lat, lon;
    int wa = 0;
    private ListView lvItems;
    private RecorridoAdapter adaptador;
    private ArrayList<Recorrido> arrayEntidad = new ArrayList<>();
    FirebaseAuth usuario;
    DatabaseReference dataBase;
    Mapa_Fragment fragment;
    ArrayList<LatLng> ubi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        usuario = FirebaseAuth.getInstance();
        dataBase = FirebaseDatabase.getInstance().getReference();
        fragment = new Mapa_Fragment();

        btn_GPS = findViewById(R.id.btn_UbicacionActual);
        lvItems = (ListView) findViewById(R.id.lvItems);
        getDataBase(this);

        //llenarItems();

        btn_GPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               mapa(-16.462950012454, -71.51263554552334);
       }
        });


    }

    public void mapa(double lat, double lon) {
        // Fragment del Mapa
       // double [] otro = new double[30];


        Bundle bundle = new Bundle();
        bundle.putDouble("lat", lat);
        bundle.putDouble("lon", lon);
       // bundle.putDoubleArray("latitutes", otro);
        fragment.setArguments(bundle);
        ubi = new ArrayList<LatLng>();

        for(Ubicacion u: arrayEntidad.get(0).getRecorrido()){
            ubi.add(new LatLng(u.getLatitud(), u.getLongitud()));
        }
         //   ubi.
        //}
        //fragment.setPoints(ubi);
        //System.out.println(" mapaaa " + ubi.get(0).toString());
        //System.out.println(" mapaaa2 " + ubi.get(1).toString());
        fragment.setPoints(ubi);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.map, fragment, null);
        fragmentTransaction.commit();
    }

    private void llenarItems(){
        arrayEntidad.add(new Recorrido("00/00/0000", "00.00", new ArrayList<Ubicacion>()));
        arrayEntidad.add(new Recorrido("00/00/0000", "00.00", new ArrayList<Ubicacion>()));
        arrayEntidad.add(new Recorrido("00/00/0000", "00.00", new ArrayList<Ubicacion>()));
        arrayEntidad.add(new Recorrido("00/00/0000", "00.00", new ArrayList<Ubicacion>()));
        adaptador = new RecorridoAdapter(this, arrayEntidad);
        lvItems.setAdapter(adaptador);
    }
    private void getDataBase(Context context){
        arrayEntidad = new ArrayList<>();
        ArrayList<Ubicacion> ubicaciones = new ArrayList<Ubicacion>();
        String id = Objects.requireNonNull(usuario.getCurrentUser()).getUid(); // "VQf0aFsW0ib8TMD7qGPnbEE7oII2";//
        dataBase.child("Usuario").child(id).child("Entrenamiento").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String fecha = ds.child("fecha").getValue().toString();
                        String tiempo = ds.child("tiempo").getValue().toString();
                        String id2 = ds.getKey();
                       dataBase.child("Usuario").child(id).child("Entrenamiento").child(id2).child("ubicacion").addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               if(snapshot.exists()){
                                   for(DataSnapshot ds: snapshot.getChildren()) {
                                       double latitud = (double) ds.child("latitud").getValue();
                                       double longitud = (double) ds.child("longitud").getValue();
                                       String id = ds.getKey().toString();
                                       int i=0;
                                       //int na = (int) id;
                                       ubicaciones.add(new Ubicacion(latitud,longitud));
                                       //agrego a array
                                   }
                                   //adapter
                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });


                      //  System.out.println((new Cargo(code,name,status)).toString());
                        arrayEntidad.add(new Recorrido(fecha,tiempo, ubicaciones));
                    }
                    //adapterPosition = new AdapterPosition(cargoList, getContext());
                    //recyclerView.setAdapter(adapterPosition);
                    adaptador = new RecorridoAdapter(context, arrayEntidad);
                    lvItems.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





}
