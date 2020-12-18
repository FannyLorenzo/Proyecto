package com.example.proyecto.interfaces;

public interface IEntrenamiento {

    interface view{
        void cronometro();
        void iniciarRecorrido();
        void pararRecorrido();
        boolean isLocationServiceRunning();
        void startLocationService();
        void stopLocationService();
        void takePhoto();
        void obtenerUbicacionInicial();
    }
    interface presenter{}
    interface model{}
}
