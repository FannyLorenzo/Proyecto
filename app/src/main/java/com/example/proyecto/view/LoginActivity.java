package com.example.proyecto.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto.R;
import com.example.proyecto.interfaces.IUsuario;
import com.example.proyecto.model.Usuario;
import com.example.proyecto.persistence.SessionManager;
import com.example.proyecto.presenter.UsuarioPresentador;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements IUsuario.view {

    private IUsuario.presenter presenter;
    private SessionManager session;

    // Constante
    int RC_SIGN_IN = 1;
    String TAG = "GoogleSignIn";

    // Variable para gestionar FirebaseAuth
    private FirebaseAuth mAuth;

    // Agregar cliente de inicio de sesión con Google
    private GoogleSignInClient mGoogleSignInClient;
    // Fuente: https://youtu.be/2CP54Jio0zE

    //Facebook
    private CallbackManager callbackManager = null;
    // Fuente: https://youtu.be/mbIp--hQZDY

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
        EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
        Button btnIngresar = (Button) findViewById(R.id.btnIngresar);
        Button btnRegistro = (Button) findViewById(R.id.btnRegistro);
        Button btnLoginGoogle = (Button) findViewById(R.id.btnLoginGoogle);
        Button btnLoginFacebook = (Button) findViewById(R.id.btnLoginFacebook);

        presenter = new UsuarioPresentador(this);
        session = new SessionManager(getApplicationContext());

        // inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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

        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Facebook
                callbackManager = CallbackManager.Factory.create();

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        presenter.handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {}

                    @Override
                    public void onError(FacebookException error) {}
                });
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // configurar Google Sign In
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestProfile()
                        .requestEmail()
                        .build();

                // crear un GoogleSignInClient con las opciones especicadas por gso
                mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onStart() {
       FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){ //si no es null el usuario ya esta logueado
            //mover al usuario a la vista del perfil
            Intent usuarioActivity = new Intent(LoginActivity.this, UsuarioActivity.class);
            startActivity(usuarioActivity);
        }
        super.onStart();

        //mAuth.addAuthStateListener(mAuthStateListener);
        //super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Facebook
        if(callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        // Google
        //Resultado devuelto al iniciar el Intent de GoogleSignInApi.getSignInIntent (...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task.isSuccessful()){

                try {
                    // Google Sign In fue exitoso, se ha autenticado con Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

                    presenter.firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In fallido
                    Log.w(TAG, "Google sign in failed", e);
                }

            }else{
                Log.d(TAG, "Error, login no exitoso:" + task.getException().toString());
                Toast.makeText(this, "Ocurrio un error. "+task.getException().toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void showResultSuccess(String result, Usuario usuario) {
        Toast.makeText(getApplicationContext(), "Bienvenido " + usuario.getNombre() + "!", Toast.LENGTH_SHORT).show();

        Intent userIntent = new Intent(this, UsuarioActivity.class);
        startActivity(userIntent);

        LoginActivity.this.finish();
    }

    @Override
    public void showResultError(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }
}