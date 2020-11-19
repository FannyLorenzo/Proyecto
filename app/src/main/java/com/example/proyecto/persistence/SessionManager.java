package com.example.proyecto.persistence;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.proyecto.view.LoginActivity;
import com.example.proyecto.view.MenuPrincipal;
import com.example.proyecto.view.UsuarioActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref; // Almacenamiento de las preferencias
    SharedPreferences.Editor editor; // Creamos el editor para realizar las operaciones
    Context _context; // Contexto
    int PRIVATE_MODE = 0; // Privacidad en las preferencias

    //claves
    private static final String PREF_NAME = "Usuario"; // Sharedpref nombre del archivo
    private static final String IS_LOGGED = "Identificado"; // Claves
    public static final String KEY_EMAIL = "Email"; // Email también en final para que sea accedida

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String email){
        // claves y valores
        editor.putBoolean(IS_LOGGED, true);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public HashMap<String, String> getUserPref() {
        HashMap<String, String> user = new HashMap<>();
        // null es el default si no hay KEY_EMAIL
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        return user;
    }

    public void loginStatus() {
        if (!this.isLogged()) {
            Intent intent = new Intent(_context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
        } else {
            _context.startActivity(new Intent(_context, UsuarioActivity.class));
        }
    }

    public boolean isLogged() {
        return pref.getBoolean(IS_LOGGED, false);
    }

    public void logOut() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(_context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }
}
