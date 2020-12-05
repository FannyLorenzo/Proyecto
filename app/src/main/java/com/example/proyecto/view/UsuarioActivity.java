package com.example.proyecto.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyecto.R;
import com.example.proyecto.interfaces.IUsuario;
import com.example.proyecto.model.Usuario;
import com.example.proyecto.presenter.UsuarioPresentador;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioActivity extends AppCompatActivity implements IUsuario.view {

    private IUsuario.presenter presenter;
    private FirebaseAuth mAuth;

    TextView txtNameUsusario;
    TextView txtEmailUsuario;
    Button btnCerrarSesion;
    ImageView imageUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        txtNameUsusario = (TextView) findViewById(R.id.txtNameUsusario);
        txtEmailUsuario = (TextView) findViewById(R.id.textEmailUsuario);
        btnCerrarSesion = (Button) findViewById(R.id.btnCerrarSesion);
        imageUsuario = (ImageView) findViewById(R.id.imageUsuario);

        // Trae al usuario logeado
        presenter = new UsuarioPresentador(this);
        //presenter.getUserAuth(); Para el login con sqlite

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        txtNameUsusario.setText(currentUser.getDisplayName());
        txtEmailUsuario.setText(currentUser.getEmail());
        // cargar imagen con Glide
        Glide.with(this).load(currentUser.getPhotoUrl()).into(imageUsuario);

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