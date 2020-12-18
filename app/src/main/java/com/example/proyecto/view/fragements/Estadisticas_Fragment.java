package com.example.proyecto.view.fragements;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyecto.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Estadisticas_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Estadisticas_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int minut=0;
    double cals=0.0,dist=0.0;
    View view;
    TextView cal,minutos,dists;


    public Estadisticas_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Estadisticas_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Estadisticas_Fragment newInstance(String param1, String param2) {
        Estadisticas_Fragment fragment = new Estadisticas_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            cals = getArguments().getDouble("cals",0.0);
            minut = getArguments().getInt("minuto1",0);
            dist = getArguments().getDouble("dist",0.0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_estadisticas_,container,false);
        minutos = (TextView) view.findViewById(R.id.textView4);
        cal = (TextView) view.findViewById(R.id.cal_est);
        dists = (TextView) view.findViewById(R.id.dist_est);

        minutos.setText(String.valueOf(minut));
        cal.setText(String.valueOf(cals));
        dists.setText(String.valueOf(dist));
        return view;
    }

    public View getView(){
        return view;
    }

    public int getMinut(){
        return minut;
    }
}