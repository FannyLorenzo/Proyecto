package com.example.proyecto.model;
import android.annotation.SuppressLint;
import android.nfc.FormatException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ubicacion {
    private double latitud;
    private  double longitud;
    private Date tiempoDeRegistro;
    @SuppressLint("SimpleDateFormat")
    private DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
    @SuppressLint("SimpleDateFormat")
    private DateFormat hora = new SimpleDateFormat("HH:mm:ss");

    public Ubicacion(){}
    public Ubicacion(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.tiempoDeRegistro = new Date();
    }



    public Date getTiempoDeRegistro() {
        return tiempoDeRegistro;
    }

    public void setTiempoDeRegistro(Date tiempoDeRegistro) {
        this.tiempoDeRegistro = tiempoDeRegistro;
    }

    public void toStringtoDate(String dt) throws ParseException {
        try{
        tiempoDeRegistro =  fechaHora.parse(dt);
        } catch (ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getFecha(Date tiempoDeRegistro) {
        return fecha.format(tiempoDeRegistro);
    }

    public String getHora(Date tiempoDeRegistro) {
        return hora.format(tiempoDeRegistro);
    }

}
