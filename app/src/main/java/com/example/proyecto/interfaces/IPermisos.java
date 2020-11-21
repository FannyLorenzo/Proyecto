package com.example.proyecto.interfaces;

public interface IPermisos {

    interface view{
        void showResultSuccessAlmacenamiento();
        void showResultSuccessUbicacion();
    }
    interface presenter{
        void showResultSuccessAlmacenamiento();
        void showResultSuccessUbicacion();
        void solicitarPermisosAlmacenamiento();
        void solicitarPermisosUbicacion();
    }
    interface model{

        void solicitarPermisosAlmacenamiento();
        void solicitarPermisosUbicacion();
    }
}
