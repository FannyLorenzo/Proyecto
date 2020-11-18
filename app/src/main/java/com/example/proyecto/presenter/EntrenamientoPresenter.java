package com.example.proyecto.presenter;

import com.example.proyecto.interfaces.IEntrenamiento;

public class EntrenamientoPresenter implements IEntrenamiento.presenter{
    private IEntrenamiento.view view;

    public EntrenamientoPresenter (IEntrenamiento.view view){
        this.view = view;
    }
}
