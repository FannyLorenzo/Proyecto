package com.example.proyecto.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import com.example.proyecto.interfaces.IPermisos;
import androidx.annotation.RequiresApi;
import static androidx.core.app.ActivityCompat.*;

public class PermisosModel implements IPermisos.model {
    private IPermisos.presenter presenter;
    private Context _context;
    public final int REQUEST_PERMISSION_STORE_AND_CAMERA = 111;
    public final int REQUEST_PERMISSION_UBICACION = 111;

    public PermisosModel(IPermisos.presenter presenter, IPermisos.view view) {
        this.presenter = presenter;
        this._context = (Context) view;
    }

    public void solicitarPermisosAlmacenamiento(){
        int permisoStorageR = checkSelfPermission(_context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permisoStorageW = checkSelfPermission(_context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permisoCamara = checkSelfPermission(_context, Manifest.permission.CAMERA);

// Store y camara
        if(permisoStorageR != PackageManager.PERMISSION_GRANTED|| permisoStorageW != PackageManager.PERMISSION_GRANTED || permisoCamara != PackageManager.PERMISSION_GRANTED )
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                presenter.showResultSuccessAlmacenamiento();
            }else
                System.out.println(" **** SDK_INT < VERSION CODES");
        else
            System.out.println(" **** OK YA CONTABA CON PERMISO - STORAGE");
}
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void solicitarPermisosUbicacion(){
        int permisoUbicacion = checkSelfPermission(_context, Manifest.permission.ACCESS_FINE_LOCATION);
       int permisoUbicacionB = checkSelfPermission(_context, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
// ubicacion
        if(permisoUbicacion != PackageManager.PERMISSION_GRANTED || permisoUbicacionB != PackageManager.PERMISSION_GRANTED)
            presenter.showResultSuccessUbicacion();
        else
            System.out.println(" **** OK YA CONTABA CON PERMISO - UBICACION");
}


}
