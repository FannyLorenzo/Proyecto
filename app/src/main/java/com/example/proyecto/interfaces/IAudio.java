package com.example.proyecto.interfaces;

import java.io.File;
import java.util.ArrayList;

public interface IAudio {
    interface view{
        void runtimePermission();
        ArrayList<File> findSong(File file);
        void display();
    }
    interface presenter{}
    interface model{}
}
