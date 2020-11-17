package com.example.proyecto.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.proyecto.R;
import com.example.proyecto.interfaces.IUbicacion;

public class UbicacionActivity extends AppCompatActivity implements IUbicacion.view {
    private Button btn_GPS;
    private TextView txt_latitud;
    private TextView txt_longitud;
    private TextView txt_direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        btn_GPS = findViewById(R.id.btn_UbicacionActual);
        txt_latitud = findViewById(R.id.txt_latitud);
        txt_longitud = findViewById(R.id.txt_longitud);
        txt_direccion = findViewById(R.id.txt_direccion);

        btn_GPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) UbicacionActivity.this.getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationChanged = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        txt_latitud.setText("" + location.getLatitude());
                        txt_longitud.setText("" + location.getLongitude());
                        txt_direccion.setText("" + location.getProvider());
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };

              //  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, locationListener);

            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission(UbicacionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            //if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
        }else{
            //    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}

            }

            // ME QUEDE AQUIII
            /// https://www.youtube.com/watch?v=XOF8aFU03ew
            // otro


        }


    }
