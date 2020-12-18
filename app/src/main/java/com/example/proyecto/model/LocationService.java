package com.example.proyecto.model;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.example.proyecto.R;
import com.example.proyecto.view.EntrenamientoActivity;
import com.example.proyecto.view.fragements.Mapa_Fragment;
import com.example.proyecto.view.fragements.Ubicacion_Fragment;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedInputStream;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class LocationService extends Service {

    EntrenamientoActivity activity;
    //TextView tvMensaje;
    Double latitud = 0.0;
    Double longitud = 0.0;

    private final IBinder mbinder = new LocalService();

    public IBinder onBind(Intent intent) {
       return mbinder;
    }
    /* Método de acceso */
    public class LocalService extends Binder {
        public LocationService getService() {

            return LocationService.this;
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {

        @SuppressLint("SetTextI18n")
        public void onLocationResult(LocationResult locationResult) {

            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null){
                latitud = locationResult.getLastLocation().getLatitude();
                longitud = locationResult.getLastLocation().getLongitude();
                Log.d("LOCATION_UPDATE", latitud+ ", "+longitud);
                //locationResult.getLocations();

            }
        }
    };

    @SuppressLint("MissingPermission")
    private void startLocationService(){
        String channeldId = "location_notification_channel";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(), channeldId
        );

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Location service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(notificationManager != null
            && notificationManager.getNotificationChannel(channeldId)==null){
                NotificationChannel notificationChannel = new NotificationChannel(
                        channeldId,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("Este canal es usado por Location Service");
                notificationManager.createNotificationChannel(notificationChannel);

            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
startForeground(Constants.LOCATION_SERVICE_ID, builder.build());

    }

    private void stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        if(intent != null ){
            String action = intent.getAction();
            if(action != null){
                if (action.equals(Constants.ACTION_START_LOCATION_SERVICE)){
                    startLocationService();
                }else if (action.equals(Constants.ACTION_STOP_LOCATION_SERVICE)){
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }




}
