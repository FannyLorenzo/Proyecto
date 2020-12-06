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
import com.facebook.AccessToken;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements IUsuario.view {

    private IUsuario.presenter presenter;
    private SessionManager session;

    // constante
    int RC_SIGN_IN = 1;
    String TAG = "GoogleSignIn";

    // variable para gestionar FirebaseAuth
    private FirebaseAuth mAuth;
    // agregar cliente de inicio de sesión con Google
    private GoogleSignInClient mGoogleSignInClient;
    // Fuente: https://youtu.be/2CP54Jio0zE

    //Facebook
    // Fuente: https://youtu.be/mbIp--hQZDY
    private CallbackManager callbackManager;

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

        // configurar Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();

        // crear un GoogleSignInClient con las opciones especicadas por gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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

        // Facebook
        callbackManager = CallbackManager.Factory.create();

        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
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
                signIn();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // validación
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Ingresó correctamente con Facebook.", Toast.LENGTH_SHORT).show();

                            // Guardar el proveedor "Google" en shared preferences
                            session.setProvider("Facebook");

                            //Iniciar el perfil del usuario
                            Intent usuarioActivity = new Intent(LoginActivity.this, UsuarioActivity.class);
                            startActivity(usuarioActivity);
                            LoginActivity.this.finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "No se puede ingresar con esta cuenta.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
       FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){ //si no es null el usuario ya esta logueado
            //mover al usuario a la vista del perfil
            Intent usuarioActivity = new Intent(LoginActivity.this, UsuarioActivity.class);
            startActivity(usuarioActivity);
        }
        super.onStart();

        //mAuth.addAuthStateListener(mAuthStateListener);
        //super.onStart();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showResultSuccess(String result, Usuario usuario) {
        Toast.makeText(getApplicationContext(), "Bienvenido " + usuario.getNombre() + "!", Toast.LENGTH_SHORT).show();

        Intent sentUser = new Intent(this, UsuarioActivity.class);
        sentUser.putExtra("nombre", usuario.getNombre());
        sentUser.putExtra("email", usuario.getEmail());
        startActivity(sentUser);

        LoginActivity.this.finish();
    }

    @Override
    public void showResultError(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Google
        //Resultado devuelto al iniciar el Intent de GoogleSignInApi.getSignInIntent (...);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task.isSuccessful()){

                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In fallido, actualizar GUI
                    Log.w(TAG, "Google sign in failed", e);
                }

            }else{
                Log.d(TAG, "Error, login no exitoso:" + task.getException().toString());
                Toast.makeText(this, "Ocurrio un error. "+task.getException().toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            Toast.makeText(LoginActivity.this, "Ingresó correctamente con Google.", Toast.LENGTH_SHORT).show();

                            // Guardar el proveedor "Google" en shared preferences
                            session.setProvider("Google");

                            //Iniciar el perfil del usuario
                            Intent usuarioActivity = new Intent(LoginActivity.this, UsuarioActivity.class);
                            startActivity(usuarioActivity);
                            LoginActivity.this.finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}