package com.example.proyecto.interfaces;

import com.example.proyecto.model.Ubicacion;

import java.text.ParseException;

public interface IUbicacion {

    interface view{
        void showRegisterError(String error_en_el_registro);
        void showRegisterSuccess(String s, Ubicacion item);
        void showRequiredUbicacion();
    }
    interface presenter{
        void showRegisterError(String error_en_el_registro);
        void showRegisterSuccess(String s, Ubicacion item);
        void showRequiredUbicacion();
        void registerUbicacion(Ubicacion item) throws ParseException;
        void solicitarPermisosUbicacion();
    }
    interface model{
        void registerUbicacion(Ubicacion item) throws ParseException;
        void solicitarPermisosUbicacion();

    }

}