package com.example.proyecto.view.fragements;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class Mapa_Fragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener{


    double lat, lon;
    View rootView;
    Polyline polyline1;
    public static ArrayList<LatLng> points = new ArrayList<LatLng>();
    public Mapa_Fragment() { }


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        rootView = super.onCreateView(layoutInflater, viewGroup, bundle);

        if(getArguments() != null) {
            this.lat = getArguments().getDouble("lat");
            this.lon = getArguments().getDouble("lon");
        }
        getMapAsync(this);

        return rootView;
    }
public void onMapReady(GoogleMap googleMap) {
        // Add a polyline to the map.

        LatLng latLng = new LatLng(lat, lon);
        float zoom = 17;

    polyline1 = googleMap.addPolyline((new PolylineOptions())
            .clickable(true)
            .addAll(points)
            .width(2)
            .color(Color.BLUE)
    );

        // Set listeners for click events.
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(latLng));


}

    public View getView(){
        return rootView;
    }

        @Override
        public void onPolygonClick(Polygon polygon) {

        }

        @Override
        public void onPolylineClick(Polyline polyline) {

        }


        public ArrayList<LatLng> getPoints() {
            return points;
        }


    public void setPoints(ArrayList<LatLng> puntos) {
        points = new ArrayList<LatLng>();
        points.addAll(puntos);
        //Mapa_Fragment.points = points;
    }

    public void actualizarPuntos(double latitud, double longitud){
        points.add(new LatLng(latitud,longitud));

        }
}

