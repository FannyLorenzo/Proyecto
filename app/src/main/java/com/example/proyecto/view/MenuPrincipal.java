package com.example.proyecto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.example.proyecto.persistence.SessionManager;

public class MenuPrincipal extends AppCompatActivity {

    // Shared preferences para saber persistir la sesión
    SessionManager session;

    // variables de entorno
    private Button btn_toLogin;
    private ImageButton imgbtn_atras;
    private TextView txt_entrenamiento;
    private TextView txt_musica;
    private TextView txt_programar;
    private TextView txt_ranking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        session = new SessionManager(this);

        // boton - Iniciar sesión
        btn_toLogin = findViewById(R.id.btn_toLogin);
        btn_toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.loginStatus();
            }
        });

        // icono - volver atrás
        imgbtn_atras = findViewById(R.id.imgbtn_atras);
        imgbtn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, MainActivity.class)); // PONER ACTIVITY DE DONDE VINO
            }
        });

        // MODULO ENTRENAMIENTO (entrenamiento en ejecución)
        txt_entrenamiento = findViewById(R.id.txt_entrenamiento);
        txt_entrenamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, EntrenamientoActivity.class)); //
            }
        });

        // MODULO MUSICA (lista de musicas)
        txt_musica = findViewById(R.id.txt_musica);
        txt_musica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, AudioActivity.class)); //
            }
        });

        // MODULO PROGRAMAR
        txt_programar = findViewById(R.id.txt_programar);
        txt_programar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MenuPrincipal.this, LALALLALA.class)); // COMPLETAR CUANDO SE TENGA EL ACTIVITY
            }
        });

        // MODULO RANKING (POSIBLEMENTE A CAMBIAR POR HISTORIAL DE ENTRENAMIENTOS)
        txt_ranking = findViewById(R.id.txt_ranking);
        txt_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, UbicacionActivity.class)); // Por ahora se está probando MAPA aqui
            }
        });
    }

}