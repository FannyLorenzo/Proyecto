package com.example.proyecto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto.R;
import com.example.proyecto.interfaces.IUsuario;
import com.example.proyecto.model.Usuario;
import com.example.proyecto.persistence.SessionManager;
import com.example.proyecto.presenter.UsuarioPresentador;

public class LoginActivity extends AppCompatActivity implements IUsuario.view {

    private IUsuario.presenter presenter;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
        EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
        Button btnIngresar = (Button) findViewById(R.id.btnIngresar);
        Button btnRegistro = (Button) findViewById(R.id.btnRegistro);

        presenter = new UsuarioPresentador(this);
        session = new SessionManager(getApplicationContext());

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                presenter.loginUser(email , password);
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });
    }

    @Override
    public void showResultSuccess(String result, Usuario usuario) {
        Toast.makeText(getApplicationContext(), "Bienvenido " + usuario.getNombre() + "!", Toast.LENGTH_SHORT).show();

        Intent sentUser = new Intent(this, UsuarioActivity.class);
        sentUser.putExtra("nombre", usuario.getNombre());
        sentUser.putExtra("email", usuario.getEmail());
        startActivity(sentUser);
    }

    @Override
    public void showResultError(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }
}