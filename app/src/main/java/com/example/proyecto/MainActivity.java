package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.proyecto.interfaces.IPermisos;
import com.example.proyecto.presenter.PermisosPresenter;
import com.example.proyecto.view.MenuPrincipal;


public class MainActivity extends AppCompatActivity implements IPermisos.view {

    public final int REQUEST_PERMISSION_STORE = 111;
    public final int REQUEST_PERMISSION_CAMERA = 111;
    public final int REQUEST_PERMISSION_UBICACION = 111;
private Button btn_next;

    public PermisosPresenter presenter = new PermisosPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_next = findViewById(R.id.btn_toPermissions);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.solicitarPermisosUbicacion();
                presenter.solicitarPermisosAlmacenamiento();
                toMenuPrincipal();
            }
        });
 }

    protected void toMenuPrincipal(){
        startActivity(new Intent(MainActivity.this, MenuPrincipal.class));
    }
    public void showResultSuccessAlmacenamiento() {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORE);
        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        System.out.println("Se ejecutó showResultSuccessAlmacenamiento ***");

    }
    @Override
    public void showResultSuccessUbicacion() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_UBICACION);
       // requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_PERMISSION_UBICACION);
        System.out.println("Se ejecutó showResultSuccessUbicacion ***");
    }
}