package com.example.proyecto.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto.R;
import com.example.proyecto.interfaces.IUsuario;
import com.example.proyecto.persistence.SessionManager;
import com.example.proyecto.persistence.DatabaseOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class UsuarioModel implements IUsuario.model {

    private IUsuario.presenter presenter;
    private String resultadoUser;
    private SQLiteDatabase database;
    private SessionManager session;
    private Context _context;

    public UsuarioModel (IUsuario.presenter presenter, IUsuario.view view) {
        this.presenter = presenter;
        this._context = (Context) view;
        database = new DatabaseOpenHelper(this._context).getWritableDatabase();
        session = new SessionManager(this._context); // Para el share preferences
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
        ArrayList<Usuario> lista = getUsuarios();

        for (Usuario u : lista) {
            if (u.getEmail() == usuario.getEmail()) {
                presenter.showRegisterError("El email ya existe.");
                return;
            }
            if (u.getContraseña() == usuario.getContraseña()) {
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

        if (returnValue == -1) {
            presenter.showRegisterError("Error en el registro");
        } else {
            presenter.showRegisterSuccess("", usuario);
        }
    }

    @Override
    public void loginUser(String email, String password) {
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
    public void loginUserWithGoogle() {

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
    public void signOutUser() {
        session.logOut();
    }
}
