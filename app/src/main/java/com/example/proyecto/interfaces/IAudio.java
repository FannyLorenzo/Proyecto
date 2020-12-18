package com.example.proyecto.interfaces;

import java.io.File;
import java.util.ArrayList;

public interface IAudio {
    interface view{
        void btn_play_pauseClicked();
        void btn_prevClicked();
        void btn_nextClicked();
    }
    interface presenter{}
    interface model{
        void btn_play_pauseClicked();
        void btn_prevClicked();
        void btn_nextClicked();
    }
}
