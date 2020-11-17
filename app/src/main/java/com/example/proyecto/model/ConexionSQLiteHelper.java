package com.example.proyecto.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {
   final String CREAR_TABLA_UBICACION = "CREATE TABLE ubicacion (fecha DATE, latitud LONG, longitud LONG)";

    //creamos la base de datos
    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    // tablas y metodos de nuestra BD
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREAR_TABLA_UBICACION);
    }

    @Override
    // verifica si existe una versión de BD anterior
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS ubicacion");
    onCreate(db);
    }
}
