package com.example.proyecto.model;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.proyecto.interfaces.IPermisos;
import com.example.proyecto.interfaces.IUbicacion;
import com.example.proyecto.persistence.DatabaseOpenHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class UbicacionModel implements IUbicacion.model{
    private IUbicacion.presenter presenter;
    private IPermisos.presenter permisos;
    private SQLiteDatabase database;
    private Context _context;

    public UbicacionModel (IUbicacion.presenter presenter, IUbicacion.view view) {
        this.presenter = presenter;
        this._context = (Context) view;
        database = new DatabaseOpenHelper(this._context).getWritableDatabase();
    }

    public ArrayList<Ubicacion> getUbicaciones () throws ParseException {
        ArrayList<Ubicacion> lista = new ArrayList<Ubicacion>();
        lista.clear();

        Cursor cr = database.rawQuery("select * from ubicacion", null);

        if (cr != null && cr.moveToFirst()) {
            do {
                Ubicacion item = new Ubicacion();

                item.setLatitud(cr.getLong(0));
                item.setLongitud(cr.getLong(1));
                item.toStringtoDate((cr.getString(2)));

                lista.add(item);
            } while(cr.moveToNext());
        }
        return lista;
    }

        public void registerUbicacion(Ubicacion item) throws ParseException {

        ContentValues contentValues = new ContentValues();

        contentValues.put("latitud", item.getLatitud());
        contentValues.put("longitud", item.getLongitud());
        contentValues.put("fecha", item.getFecha(new Date())); // ojo aqui

        long returnValue = database.insert("ubicacion", null, contentValues);

        if (returnValue == -1) {
            presenter.showRegisterError("Error en el registro");
        } else {
            presenter.showRegisterSuccess("", item);
        }
    }
    public void solicitarPermisosUbicacion(){
        int permisoUbicacion = checkSelfPermission(_context, Manifest.permission.ACCESS_FINE_LOCATION);
     //   int permisoUbicacionB = checkSelfPermission(_context, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
// ubicacion
        if(permisoUbicacion != PackageManager.PERMISSION_GRANTED)// || permisoUbicacionB != PackageManager.PERMISSION_GRANTED)
            presenter.showRequiredUbicacion();
        else
            System.out.println(" **** OK YA CONTABA CON PERMISO - UBICACION");
    }


}
