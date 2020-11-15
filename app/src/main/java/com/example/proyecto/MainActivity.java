package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.proyecto.view.MenuPrincipal;


public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_STORE = 111;
    private final int REQUEST_PERMISSION_CAMERA = 111;
    private final int REQUEST_PERMISSION_UBICACION = 111;
private Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_next = findViewById(R.id.btn_toPermissions);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarPermisosAlmacenamiento();
                solicitarPermisosUbicacion();
                startActivity(new Intent(MainActivity.this, MenuPrincipal.class));
            }
        });


    }

private void solicitarPermisosAlmacenamiento(){
int permisoStorageR = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
int permisoStorageW = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
int permisoCamara = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);

// Store
if(permisoStorageR != PackageManager.PERMISSION_GRANTED || permisoStorageW != PackageManager.PERMISSION_GRANTED )
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORE);
    }
// camera
    if(permisoCamara != PackageManager.PERMISSION_GRANTED )
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        }
}

    private void solicitarPermisosUbicacion(){
        int permisoUbicacion = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
//int permisoUbicacionB = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION);


// ubicacion
        if(permisoUbicacion != PackageManager.PERMISSION_GRANTED )
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_UBICACION);


}
}