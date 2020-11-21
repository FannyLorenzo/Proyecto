package com.example.proyecto.presenter;

import com.example.proyecto.interfaces.IPermisos;
import com.example.proyecto.model.PermisosModel;

public class PermisosPresenter implements IPermisos.presenter {

    private IPermisos.view view;
    private IPermisos.model model;

    public PermisosPresenter (IPermisos.view view) {
        this.view = view;
        model = new PermisosModel(this, this.view);
    }

    @Override
    public void solicitarPermisosAlmacenamiento() {
    model.solicitarPermisosAlmacenamiento();
    }
    @Override
    public void solicitarPermisosUbicacion() {
    model.solicitarPermisosUbicacion();
    }
    @Override
    public void showResultSuccessAlmacenamiento() {
    view.showResultSuccessAlmacenamiento();
    }
    @Override
    public void showResultSuccessUbicacion()  {
    view.showResultSuccessUbicacion();
    }
}
