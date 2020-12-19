package com.example.proyecto.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.example.proyecto.interfaces.IUsuario;
import com.example.proyecto.model.Usuario;
import com.example.proyecto.persistence.SessionManager;
import com.example.proyecto.presenter.UsuarioPresentador;
import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsuarioActivity extends AppCompatActivity implements IUsuario.view {

    private IUsuario.presenter presenter;
    private SessionManager session;
    private FirebaseAuth mAuth;

    //Variables opcionales para desloguear de google tambien
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    TextView txtNameUsusario;
    TextView txtNameUsuario2;
    TextView txtEmailUsuario;
    TextView txtEmailUsuario2;
    Button btnCerrarSesion;
    ImageView imageUsuario;
    CircleImageView imageUsuario2;
    ImageButton imgbtn_atras;
    Button btn_acerca_mi;
    Button btn_rutina;
    Button btn_fotografia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        txtNameUsusario = (TextView) findViewById(R.id.txtNameUsusario);
        txtNameUsuario2 = (TextView) findViewById(R.id.txtNameUsusario2);
        txtEmailUsuario = (TextView) findViewById(R.id.textEmailUsuario);
        txtEmailUsuario2 = (TextView) findViewById(R.id.textEmailUsuario2);
        btnCerrarSesion = (Button) findViewById(R.id.btnCerrarSesion);
        imageUsuario = (ImageView) findViewById(R.id.imageUsuario);
        imageUsuario2 = (CircleImageView) findViewById(R.id.imageUsuario2);
        imgbtn_atras = (ImageButton) findViewById(R.id.imgbtn_atras);
        btn_acerca_mi = (Button) findViewById(R.id.btn_acerca_mi);
        btn_rutina = (Button) findViewById(R.id.btn_rutina);
        btn_fotografia = (Button) findViewById(R.id.btn_fotografia);

        presenter = new UsuarioPresentador(this);
        session = new SessionManager(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Ícono - volver atrás
        imgbtn_atras = findViewById(R.id.imgbtn_atras);
        imgbtn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UsuarioActivity.this, MenuPrincipal.class));
            }
        });

        //Configurar las gso para google signIn con el fin de luego desloguear de google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Sólo si el usuario se ha logeado con Google o Facebook
        if (currentUser != null) {
            txtNameUsusario.setText(currentUser.getDisplayName());
            txtNameUsuario2.setText(currentUser.getDisplayName());
            txtEmailUsuario.setText(currentUser.getEmail());
            txtEmailUsuario2.setText(currentUser.getEmail());

            // cargar imagen con Glide
            if (session.getProvider().equals("Google")) {
                Glide.with(this).load(currentUser.getPhotoUrl()).into(imageUsuario);
                Glide.with(this).load(currentUser.getPhotoUrl()).into(imageUsuario2);
            } else if (session.getProvider().equals("Facebook")) {
                Glide.with(this).load(Profile.getCurrentProfile().getProfilePictureUri(500,500).toString()).into(imageUsuario);
                Glide.with(this).load(Profile.getCurrentProfile().getProfilePictureUri(500,500).toString()).into(imageUsuario2);
            }
        }
        else { // De lo contrario debe de haber inicio de sesión por sqlite
            // Trae al usuario logeado
            presenter.getUserAuth();  // Para el login con sqlite
        }

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentUser != null) {
                    //Cerrar sesión de firebase.
                    mAuth.signOut();

                    if (session.getProvider().equals("Facebook")) {
                        com.facebook.login.LoginManager.getInstance().logOut();
                    }
                }

                // Cerrar sesión en shared preferences
                presenter.signOutUser();

                //Cerrar sesión con google tambien: Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Abrir MainActivity con SigIn button
                        if(task.isSuccessful()){
                            //Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                            //startActivity(loginActivity);
                            UsuarioActivity.this.finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "No se pudo cerrar sesión con google", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        btn_acerca_mi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Acerca de mí.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_rutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Mis rutinas.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_fotografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Mis fotografías.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showResultSuccess(String result, Usuario usuario) {
        txtNameUsusario.setText(usuario.getNombre());
        txtNameUsuario2.setText(usuario.getNombre());
        txtEmailUsuario.setText(usuario.getEmail());
        txtEmailUsuario2.setText(usuario.getEmail());
    }

    @Override
    public void showResultError(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MenuPrincipal.class));
    }
}