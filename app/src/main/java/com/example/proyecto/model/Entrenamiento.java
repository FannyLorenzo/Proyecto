package com.example.proyecto.model;

import java.util.ArrayList;

public class Entrenamiento {
    String Codigo;
    String Fecha;
    String Tiempo;
    String Distancia;
    String Velocidad;
    String Calorias;
    //Boolean rank;

    public Entrenamiento(String codigo,String fecha, String tiempo, String distancia, String velocidad, String calorias){
        this.Codigo = codigo;
        this.Fecha = fecha;
        this.Tiempo = tiempo;
        this.Distancia = distancia;
        this.Velocidad = velocidad;
        this.Calorias = calorias;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getTiempo() {
        return Tiempo;
    }

    public void setTiempo(String tiempo) {
        Tiempo = tiempo;
    }

    public String getDistancia() {
        return Distancia;
    }

    public void setDistancia(String distancia) {
        Distancia = distancia;
    }

    public String getVelocidad() {
        return Velocidad;
    }

    public void setVelocidad(String velocidad) {
        Velocidad = velocidad;
    }

    public String getCalorias() {
        return Calorias;
    }

    public void setCalorias(String calorias) {
        Calorias = calorias;
    }

    /*public Boolean getRank() {
        return rank;
    }

    public void setRank(Boolean rank) {
        this.rank = rank;
    }*/
}
