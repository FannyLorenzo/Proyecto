package com.example.proyecto.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.proyecto.interfaces.IUsuario;
import com.example.proyecto.persistence.SessionManager;
import com.example.proyecto.view.DatabaseOpenHelper;

import java.util.ArrayList;

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
        session = new SessionManager(this._context);
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
                    session.createLoginSession("javier", "javier@gmail.com");
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
    public void signOutUser() {
        session.logOut();
    }
}
