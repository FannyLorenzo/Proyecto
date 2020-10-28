package com.example.proyecto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.proyecto.R;
import com.example.proyecto.interfaces.IUbicacion;

public class UbicacionActivity extends AppCompatActivity implements IUbicacion.view {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
    }
}