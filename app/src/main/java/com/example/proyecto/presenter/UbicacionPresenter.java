package com.example.proyecto.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import com.example.proyecto.MainActivity;
import com.example.proyecto.interfaces.IPermisos;
import com.example.proyecto.interfaces.IUbicacion;
import com.example.proyecto.model.Ubicacion;

import java.text.ParseException;
import java.util.ArrayList;

import androidx.core.app.ActivityCompat;

public class UbicacionPresenter implements IUbicacion.presenter {
    private IUbicacion.view view;
    private IUbicacion.model model;
    @Override
    public void showRegisterError(String error_en_el_registro) {
        view.showRegisterError(error_en_el_registro);
    }

    @Override
    public void showRegisterSuccess(String s, Ubicacion item) {
    view.showRegisterSuccess( s,  item);
    }

    @Override
    public void showRequiredUbicacion() {
    view.showRequiredUbicacion();    }

    ArrayList<Ubicacion> getUbicaciones () throws ParseException{
        return null;
    }
    @Override
    public void registerUbicacion(Ubicacion item) throws ParseException {
    model.registerUbicacion(item);
    }

    @Override
    public void solicitarPermisosUbicacion() {
    model.solicitarPermisosUbicacion();
    }
}
