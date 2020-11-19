package com.example.proyecto.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto.R;
import com.example.proyecto.interfaces.IUsuario;
import com.example.proyecto.model.Usuario;
import com.example.proyecto.presenter.UsuarioPresentador;

public class UsuarioActivity extends AppCompatActivity implements IUsuario.view {

    private IUsuario.presenter presenter;

    TextView txtNameUsusario;
    TextView txtEmailUsuario;
    Button btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        txtNameUsusario = (TextView) findViewById(R.id.txtNameUsusario);
        txtEmailUsuario = (TextView) findViewById(R.id.textEmailUsuario);
        btnCerrarSesion = (Button) findViewById(R.id.btnCerrarSesion);

        // Trae al usuario logeado
        presenter = new UsuarioPresentador(this);
        presenter.getUserAuth();

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.signOutUser();
            }
        });
    }

    @Override
    public void showResultSuccess(String result, Usuario usuario) {
        txtEmailUsuario.setText(usuario.getEmail());
        txtNameUsusario.setText(usuario.getNombre());
    }

    @Override
    public void showResultError(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MenuPrincipal.class));
    }
}