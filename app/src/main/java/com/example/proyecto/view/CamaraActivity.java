package com.example.proyecto.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyecto.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
public class CamaraActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView;
    Button button,button2;
    String mAbsolutePath = "";
    private static final int REQUEST_PERMISION_CAMERA = 100;
    private static final int TAKE_PICTURE = 101;

    private static final int REQUEST_PERMISION_WRITE_STORAGE = 200;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);
        iniUI();
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    private void iniUI(){
        imageView = (ImageView) findViewById(R.id.image);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button){
            checkPermissionCamera();
        }else if (id == R.id.button2){
            checkPermissionStorage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == TAKE_PICTURE){
            if (resultCode == Activity.RESULT_OK && data!= null){
                bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISION_CAMERA){
            if (permissions.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePicture();
            }
        }else if (requestCode == REQUEST_PERMISION_WRITE_STORAGE){
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveImage();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkPermissionCamera(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                takePicture();
            }else{
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISION_CAMERA
                );
            }
        }else{
            takePicture();
        }
    }

    private void checkPermissionStorage(){
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    saveImage();
                }else{
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_PERMISION_WRITE_STORAGE
                    );
                }
            }else{
                saveImage();
            }
        }else {
            saveImage();
        }
    }

    private void takePicture(){
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, TAKE_PICTURE);
        }
    }

    private void saveImage(){
        OutputStream fos = null;
        File file = null;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
            ContentResolver resolver = getContentResolver();
            ContentValues values = new ContentValues();

            String fileName= System.currentTimeMillis() + "image_example";

            values.put(MediaStore.Images.Media.DISPLAY_NAME,fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            ///values.put(MediaStore.Images.Media.RELATIVE_PATH, "Picture/MyApp");
            //values.put(MediaStore.Images.Media.IS_PENDING,1);

            Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.getVersion(this));
            Uri imageUri = resolver.insert(collection,values);

            try {
                fos = resolver.openOutputStream(imageUri);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            values.clear();
            //values.put(MediaStore.Images.Media.IS_PENDING,0);
            resolver.update(imageUri,values,null, null);
        }else {
            String imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            String fileName = System.currentTimeMillis() + ".jpg";

            file = new File(imageDir,fileName);
            try {
                fos = new FileOutputStream(file);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }
        boolean saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fos);
        if (saved){
            Toast.makeText(this, "Guardado Satisfactoriamente", Toast.LENGTH_SHORT).show();
        }
        if (fos != null){
            try {
                fos.flush();
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        if (file!=null){
            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, null);
        }
    }
}