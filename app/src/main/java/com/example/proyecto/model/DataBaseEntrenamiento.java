package com.example.proyecto.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseEntrenamiento extends SQLiteOpenHelper {

    String tabla = "CREATE TABLE Entrenamiento(ID INTEGER PRIMARY KEY AUTOINCREMENT, entrenamiento Text, fecha Text, recorrido Text)";
    public DataBaseEntrenamiento(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
