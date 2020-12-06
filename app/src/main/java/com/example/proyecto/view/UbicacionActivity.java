package com.example.proyecto.view;

import androidx.annotation.RequiresApi;
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
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.proyecto.R;
import com.example.proyecto.interfaces.IUbicacion;
import com.example.proyecto.model.Ubicacion;
import com.example.proyecto.presenter.PermisosPresenter;
import com.example.proyecto.presenter.UbicacionPresenter;
import com.example.proyecto.presenter.UsuarioPresentador;

public class UbicacionActivity extends AppCompatActivity implements IUbicacion.view {
    private static final int REQUEST_PERMISSION_UBICACION = 111;
    private Button btn_GPS;
    private TextView txt_latitud;
    private TextView txt_longitud;
    private TextView txt_direccion;
    private UbicacionPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        btn_GPS = findViewById(R.id.btn_UbicacionActual);
        txt_latitud = findViewById(R.id.txt_latitud);
        txt_longitud = findViewById(R.id.txt_longitud);
        txt_direccion = findViewById(R.id.txt_direccion);

       presenter= new UbicacionPresenter(this); /// AQUIIIIIII
        btn_GPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.solicitarPermisosUbicacion(); /// mmm

                LocationManager locationManager = (LocationManager) UbicacionActivity.this.getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = new LocationListener() {
                    @SuppressLint("SetTextI18n") // weno
                    public void onLocationChanged(Location location) {
                        txt_latitud.setText("" + location.getLatitude());
                        txt_longitud.setText("" + location.getLongitude());
                        txt_direccion.setText("speed: " + location.getSpeed() + "time: " + "/n" +
                                location.getTime() + "realtime: " +  "/n" +
                                location.getElapsedRealtimeNanos());

                        // Aqui guardar objeto UbicacionModel con SQLite

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

                presenter.solicitarPermisosUbicacion();
                int permissionCheck = ContextCompat.checkSelfPermission(UbicacionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission(UbicacionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);


    }

    @Override
    public void showRegisterError(String error_en_el_registro) {

    }

    @Override
    public void showRegisterSuccess(String s, Ubicacion item) {

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void showRequiredUbicacion() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_UBICACION);
        requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_PERMISSION_UBICACION);
        System.out.println("Se ejecutó showResultSuccessUbicacion ***");
    }
}
