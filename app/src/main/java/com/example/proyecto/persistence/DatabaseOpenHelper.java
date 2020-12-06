package com.example.proyecto.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AppEntrenamiento.db";

    private Context mContext;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Gestión de usuarios
        String createTableSQL = "create table if not exists usuario (id integer primary key autoincrement, " +
                                "nombre text, email text, contraseña text, genero text)";
        db.execSQL(createTableSQL);

        //Gestión de ubicaciones
        String createTableSQL2 = "create table if not exists usuario (id integer primary key autoincrement, " +
                "nombre text, email text, contraseña text, genero text)";
        db.execSQL(createTableSQL2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
