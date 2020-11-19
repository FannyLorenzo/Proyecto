package com.example.proyecto.view;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proyecto.R;
import com.example.proyecto.interfaces.IAudio;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class AudioActivity extends AppCompatActivity implements IAudio.view {
    private static final int MY_PERMISSION_REQUEST =1;
    ArrayList<String> arrayList;
    ListView listView;
    String[] items;

    ArrayAdapter<String> adapter;
    static MediaPlayer mediaPlayer;
    Uri u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        listView = (ListView) findViewById(R.id.music);

        runtimePermission();
    }
    public void runtimePermission(){
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findSong(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        for (File singleFile: files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findSong(singleFile));
            }else {
                if (singleFile.getName().endsWith(".mp3") ||
                        singleFile.getName().endsWith(".wsv")){
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }

    public void display(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());

        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++){
            items[i] = mySongs.get(i).getName().toString().replace(".mp3","").replace(".wsv","");

        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                }
                String songName = listView.getItemAtPosition(position).toString();
                u = Uri.parse(mySongs.get(position));
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });*/

                /*startActivity(new Intent(getApplicationContext(),PlayerActivity.class)
                .putExtra("songs", mySongs).putExtra("songname",songName));*/

            }
        });
    }
}