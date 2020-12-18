package com.example.proyecto.model;

public class MusicFiles {
    private String path;
    private String tittle;
    private String artist;
    private String album;
    private String duration;
    private String id;

    public MusicFiles(String path, String tittle, String artist, String album, String duration, String id) {
        this.path = path;
        this.tittle = tittle;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.id = id;
    }

    public MusicFiles() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId() { return id;}

    public void setId(String id) { this.id = id; }
}
