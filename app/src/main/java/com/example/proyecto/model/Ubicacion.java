package com.example.proyecto.model;
import android.annotation.SuppressLint;
import android.nfc.FormatException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ubicacion {
    private long latitud;
    private  long longitud;
    private Date tiempoDeRegistro;
    @SuppressLint("SimpleDateFormat")
    private DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
    @SuppressLint("SimpleDateFormat")
    private DateFormat hora = new SimpleDateFormat("HH:mm:ss");

    public Ubicacion(){}
    public Ubicacion(long latitud, long longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.tiempoDeRegistro = new Date();
    }

    public long getLatitud() {
        return latitud;
    }

    public void setLatitud(long latitud) {
        this.latitud = latitud;
    }

    public long getLongitud() {
        return longitud;
    }

    public void setLongitud(long longitud) {
        this.longitud = longitud;
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

    public String getFecha( Date tiempoDeRegistro) {
        return fecha.format(tiempoDeRegistro);
    }

    public String getHora(Date tiempoDeRegistro) {
        return hora.format(tiempoDeRegistro);
    }

}