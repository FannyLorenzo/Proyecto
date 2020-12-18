package com.example.proyecto.view;

import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.R;
import com.example.proyecto.model.Entrenamiento;
import com.example.proyecto.view.adapter.EntrenamientoAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class EntrenamientoHistorial extends AppCompatActivity {

    private EntrenamientoAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Entrenamiento> mRecordatoriosList = new ArrayList<Entrenamiento>();

    FirebaseAuth usuario;
    DatabaseReference dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_entrenamiento);

        usuario = FirebaseAuth.getInstance();
        dataBase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerId);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        // mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));


        getRecordatoriosFromFirebase();


    }
    private void getRecordatoriosFromFirebase(){
        String id = "VQf0aFsW0ib8TMD7qGPnbEE7oII2";//Objects.requireNonNull(usuario.getCurrentUser()).getUid();
        dataBase.child("Usuario").child(id).child("Entrenamiento").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        //String Codigo= ds.getKey().toString();
                        String fecha= ds.child("fecha").getValue().toString();
                        String tiempo= ds.child("tiempo").getValue().toString();
                        String distancia=ds.child("distancia").getValue().toString();
                        String velocidad =ds.child("velocidad").getValue().toString();
                        String calorias= ds.child("calorias").getValue().toString();
                        mRecordatoriosList.add(new Entrenamiento("VQf0aFsW0ib8TMD7qGPnbEE7oII2",fecha,tiempo,distancia,velocidad,calorias));
                    }
                    mAdapter = new EntrenamientoAdapter(mRecordatoriosList,EntrenamientoHistorial.this);
                    System.out.println("***********************/NADA"+mRecordatoriosList.size());
                    mRecyclerView.setAdapter(mAdapter);
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
