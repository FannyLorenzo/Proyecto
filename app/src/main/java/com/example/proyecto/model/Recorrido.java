package com.example.proyecto.model;

import java.util.ArrayList;
import java.util.Map;

public class Recorrido {
    private String fecha;
    private String tiempo;
    ArrayList<Ubicacion> recorrido = new ArrayList<Ubicacion>();

    public Recorrido(String fecha, String tiempo, ArrayList<Ubicacion> ubicaciones) {
        this.fecha = fecha;
        this.tiempo = tiempo;
        //this.recorrido = recorrido;

            for (Ubicacion u: recorrido){
            recorrido.add(new Ubicacion(u.getLatitud(), u.getLongitud()));
            }
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public ArrayList<Ubicacion> getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(ArrayList<Ubicacion> recorrido) {
        this.recorrido = recorrido;
    }
}
