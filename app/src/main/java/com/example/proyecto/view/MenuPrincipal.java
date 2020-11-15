package com.example.proyecto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.proyecto.MainActivity;
import com.example.proyecto.R;

public class MenuPrincipal extends AppCompatActivity {
private Button btn_toLogin;
private ImageButton imgbtn_atras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        btn_toLogin = findViewById(R.id.btn_toLogin);
        imgbtn_atras = findViewById(R.id.imgbtn_atras);

        btn_toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, Prueba.class)); // REEMPLAZAR POR EL LOGIN FINAL
            }
        });

        imgbtn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, MainActivity.class)); // REEMPLAZAR POR EL LOGIN FINAL
            }
        });
    }

}