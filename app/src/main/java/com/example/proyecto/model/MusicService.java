package com.example.proyecto.model;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.proyecto.interfaces.IAudio;
import com.facebook.internal.PlatformServiceClient;

import java.security.Provider;
import java.util.ArrayList;

import static com.example.proyecto.view.PlayerActivity.listSongs;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener{
    IBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    Uri uri;
    ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    int position =-1;
    IAudio.model actionPlaying;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Bind", "Method");
        return mBinder;
    }

    public void onCompletion() {
    }

    public class MyBinder extends Binder {
        public MusicService getService(){
            return MusicService.this;
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPosition = intent.getIntExtra("servicePosition", -1);
        String actionName = intent.getStringExtra("ActionName");
        if (myPosition != -1){
            playMedia(myPosition);
        }
        if (actionName != null){
            switch (actionName){
                case "playPause":
                    Toast.makeText(this, "Play/Pausar", Toast.LENGTH_SHORT).show();
                    if (actionPlaying != null){
                        Log.e("Inside", "Action");
                        actionPlaying.btn_play_pauseClicked();
                    }
                    break;
                case "next":
                    Toast.makeText(this, "Siguiente", Toast.LENGTH_SHORT).show();
                    if (actionPlaying != null){
                        Log.e("Inside", "Action");
                        actionPlaying.btn_nextClicked();
                    }
                    break;
                case "previous":
                    Toast.makeText(this, "Anterior", Toast.LENGTH_SHORT).show();
                    if (actionPlaying != null){
                        Log.e("Inside", "Action");
                        actionPlaying.btn_prevClicked();
                    }
                    break;
            }
        }
        return START_STICKY;
    }

    private void playMedia(int Startposition) {
        musicFiles = listSongs;
        position = Startposition;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (musicFiles != null){
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        }
        else {
            createMediaPlayer(position);
            mediaPlayer.start();
        }
    }

    public void start(){
        mediaPlayer.start();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void  stop(){
        mediaPlayer.stop();
    }

    public void release(){
        mediaPlayer.release();
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    public void createMediaPlayer(int position){
        uri = Uri.parse(musicFiles.get(position).getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public void OnCompleted(){
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (actionPlaying != null){
            actionPlaying.btn_nextClicked();
        }
        createMediaPlayer(position);
        mediaPlayer.start();
        OnCompleted();
    }

    public void setCallBack(IAudio.model actionPlaying){
        this.actionPlaying = actionPlaying;
    }
}
