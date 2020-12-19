package com.example.proyecto.model;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto.R;
import com.example.proyecto.interfaces.IUsuario;
import com.example.proyecto.persistence.SessionManager;
import com.example.proyecto.persistence.DatabaseOpenHelper;
import com.example.proyecto.view.LoginActivity;
import com.example.proyecto.view.UsuarioActivity;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UsuarioModel implements IUsuario.model {

    private IUsuario.presenter presenter;
    private String resultadoUser;
    private SQLiteDatabase database;
    private SessionManager session;
    private Context _context;

    // Constante
    String TAG = "GoogleSignIn";

    // Variable para gestionar FirebaseAuth
    private FirebaseAuth mAuth;
    private DatabaseReference dataBase;

    public UsuarioModel (IUsuario.presenter presenter, IUsuario.view view) {
        this.presenter = presenter;
        this._context = (Context) view;
        database = new DatabaseOpenHelper(this._context).getWritableDatabase();
        session = new SessionManager(this._context); // Para el share preferences

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        dataBase = FirebaseDatabase.getInstance().getReference();
    }

    private ArrayList<Usuario> getUsuarios () {
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        lista.clear();

        Cursor cr = database.rawQuery("select * from usuario", null);

        if (cr != null && cr.moveToFirst()) {
            do {
                Usuario usuario = new Usuario();

                usuario.setId(cr.getInt(0));
                usuario.setNombre(cr.getString(1));
                usuario.setEmail((cr.getString(2)));
                usuario.setContraseña((cr.getString(3)));
                usuario.setGenero(cr.getString(4));

                lista.add(usuario);
            } while(cr.moveToNext());
        }
        return lista;
    }

    @Override
    public void registerUser(Usuario usuario) {
        if (usuario.getNombre().equals("") || usuario.getEmail().equals("") || usuario.getContraseña().equals("")) {
            presenter.showRegisterError("Hay campos vacíos.");
            return;
        }

        ArrayList<Usuario> lista = getUsuarios();

        for (Usuario u : lista) {
            if (u.getEmail().equals(usuario.getEmail())) {
                presenter.showRegisterError("El email ya existe.");
                return;
            }
            if (u.getContraseña().equals(usuario.getContraseña())) {
                presenter.showRegisterError("La contraseña ya existe.");
                return;
            }
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put("nombre", usuario.getNombre());
        contentValues.put("email", usuario.getEmail());
        contentValues.put("contraseña", usuario.getContraseña());
        contentValues.put("genero", usuario.getGenero());

        long returnValue = database.insert("usuario", null, contentValues);
        this.registerWithUserPassword(usuario);

        if (returnValue == -1) {
            presenter.showRegisterError("Error en el registro");
        } else {
            presenter.showRegisterSuccess("", usuario);
        }
    }

    @Override
    public void loginUser(String email, String password) {
        if (email.equals("") || password.equals("")) {
            presenter.showRegisterError("Hay campos vacíos.");
            return;
        }

        ArrayList<Usuario> lista = getUsuarios();

        for (Usuario usuario : lista) {
            if (usuario.getEmail().equals(email)) {
                if (usuario.getContraseña().equals(password)) {
                    session.createLoginSession(usuario.getEmail()); // Se guarda el email en share preferences
                    presenter.showRegisterSuccess("", usuario);
                    return;
                } else {
                    presenter.showRegisterError("Contraseña incorrecta.");
                    return;
                }
            }
        }
        presenter.showRegisterError("Usuario no existente.");
    }

    @Override
    public void getUserAuth() {
        ArrayList<Usuario> lista = getUsuarios();
        HashMap<String, String> user = session.getUserPref();

        for (Usuario usuario : lista) {
            if (usuario.getEmail().equals(user.get(SessionManager.KEY_EMAIL))) {
                presenter.showRegisterSuccess("", usuario);
                return;
            }
        }
        presenter.showRegisterError("Error al cargar el usuario.");
    }

    @Override
    public void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) this._context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // validación
                        if (task.isSuccessful()) {

                            // Guardar el proveedor "Google" en shared preferences
                            session.setProvider("Facebook");

                            // Map
                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            Map<String, Object> map = new HashMap<>();
                            map.put("name", currentUser.getDisplayName());
                            map.put("email", currentUser.getEmail());

                            // variables de id
                            String id = Objects.requireNonNull(currentUser).getUid();

                            // creacion de usuario en la base de datos
                            dataBase.child("Usuario").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if(task2.isSuccessful()){}
                                    else {}
                                }
                            });

                            Usuario usuario = new Usuario();
                            usuario.setNombre(currentUser.getDisplayName());
                            usuario.setEmail(currentUser.getEmail());

                            presenter.showRegisterSuccess("Ingresó correctamente con Facebook.", usuario);
                        } else {
                            presenter.showRegisterError("No se puede ingresar con esta cuenta.");
                        }
                    }
                });
    }

    @Override
    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) this._context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in exitoso
                            Log.d(TAG, "signInWithCredential:success");

                            // Guardar el proveedor "Google" en shared preferences
                            session.setProvider("Google");

                            // Registrar en la base de datos de Firebase
                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            Map<String, Object> map = new HashMap<>();
                            map.put("name", currentUser.getDisplayName());
                            map.put("email", currentUser.getEmail());

                            // variables de id
                            String id = Objects.requireNonNull(currentUser).getUid();

                            // creacion de usuario en la base de datos
                            dataBase.child("Usuario").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if(task2.isSuccessful()){}
                                    else {}
                                }
                            });

                            Usuario usuario = new Usuario();
                            usuario.setNombre(currentUser.getDisplayName());
                            usuario.setEmail(currentUser.getEmail());

                            presenter.showRegisterSuccess("Ingresó correctamente con Google.", usuario);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public void registerUserFirebase(FirebaseUser user){
        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getDisplayName());
        map.put("email", user.getEmail());

        // variables de id
        String id = Objects.requireNonNull(user).getUid();

        // creacion de usuario en la base de datos
        dataBase.child("Usuario").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if(task2.isSuccessful()){}
                else {}
            }
        });
    }

    public void registerWithUserPassword(Usuario user) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getNombre());
        map.put("email", user.getEmail());

        Log.w(TAG, "asd "+mAuth.getCurrentUser());
        // variables de id
        String key = dataBase.push().getKey();

        // Creacion de usuario en la base de datos
        dataBase.child("Usuario").child(key).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if(task2.isSuccessful()){}
                else{}
            }
        });
    }

    @Override
    public void signOutUser() {
        session.logOut();
    }
}
