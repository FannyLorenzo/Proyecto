package com.example.proyecto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.example.proyecto.interfaces.IUsuario;
import com.example.proyecto.model.Usuario;
import com.example.proyecto.presenter.UsuarioPresentador;

public class RegistroActivity extends AppCompatActivity implements IUsuario.view {

    private IUsuario.presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        EditText txtNombre = (EditText) findViewById(R.id.txtName);
        EditText txtEmail = (EditText) findViewById(R.id.txtEmailRegistro);
        EditText txtContraseña = (EditText) findViewById(R.id.txtPasswordRegistro);
        RadioGroup radioGroupGenero = (RadioGroup) findViewById(R.id.radioGroupGenero);
        CheckBox checkBoxTerminos = (CheckBox) findViewById(R.id.checkBoxTerminos);
        Button btnRegistrate = (Button) findViewById(R.id.btnRegistrate);
        Button btnLoginFacebook = (Button) findViewById(R.id.btnLoginFacebook);
        Button btnLoginGoogle = (Button) findViewById(R.id.btnLoginGoogle);

        presenter = new UsuarioPresentador(this);

        btnRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = txtNombre.getText().toString();
                String email = txtEmail.getText().toString();
                String contraseña = txtContraseña.getText().toString();

                Usuario usuario = new Usuario(nombre, email, contraseña, "");

                presenter.registerUser(usuario);
            }
        });

        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Registrese desde el login.", Toast.LENGTH_LONG).show();
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Registrese desde el login.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void showResultSuccess(String result, Usuario usuario) {
        Toast.makeText(getApplicationContext(), usuario.getNombre() + " se ha registrado con éxito!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void showResultError(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }
}