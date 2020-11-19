package com.example.proyecto.model;

public class Usuario {
    // Datos de usuario
    private int id;
    private String nombre;
    private String email;
    private String contraseña;
    private String genero;

    public Usuario () {}

    public Usuario (String nombre,  String email, String contraseña, String genero) {
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.genero = genero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
