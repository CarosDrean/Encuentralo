package proyectos.carosdrean.xyz.encuentralo.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import proyectos.carosdrean.xyz.encuentralo.BaseDatos.BaseDatos;
import proyectos.carosdrean.xyz.encuentralo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Inicio extends Fragment {

    private CheckBox accesorios;
    private CheckBox restaurantes;
    private CheckBox autos;
    private CheckBox ropas;
    private CheckBox ferreterias;
    private CheckBox hotel;
    private CheckBox supermercados;
    private CheckBox zapateria;
    private CheckBox panaderia;

    private boolean[] estados = {true, false, false, false, false, false, false, false, false, false};
    public Inicio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inicio, container, false);
        // Inflate the layout for this fragment

        ImageView filtros = (ImageView)v.findViewById(R.id.filtros);
        Glide.with(getActivity()).load(R.drawable.filtros).into(filtros);
        filtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflarFiltros();
            }
        });

        return v;
    }

    public AlertDialog inflarFiltros(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialogo_filtros, null);
        inicializarCheckboxs(v);

        marcar();
        marcados();

        builder.setView(v)
                .setTitle("Filtros de Marcadores")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        subirFiltros();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public void inicializarCheckboxs(View v){
        accesorios     = (CheckBox)v.findViewById(R.id.chAccesorio);
        restaurantes   = (CheckBox)v.findViewById(R.id.chRestaurantes);
        autos          = (CheckBox)v.findViewById(R.id.chAutos);
        ropas          = (CheckBox)v.findViewById(R.id.chRopas);
        ferreterias    = (CheckBox)v.findViewById(R.id.chFerreterias);
        hotel          = (CheckBox)v.findViewById(R.id.chHotel);
        supermercados  = (CheckBox)v.findViewById(R.id.chSupermercado);
        zapateria      = (CheckBox)v.findViewById(R.id.chZapaterias);
        panaderia      = (CheckBox)v.findViewById(R.id.chPanaderias);
    }

    public void marcar(){

        accesorios.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    estados[0] = true;
                }else{
                    estados[0] = false;
                }
            }
        });

        restaurantes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    estados[1] = true;
                }else{
                    estados[1] = false;
                }
            }
        });

        autos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    estados[2] = true;
                }else{
                    estados[2] = false;
                }
            }
        });

        ropas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    estados[3] = true;
                }else{
                    estados[3] = false;
                }
            }
        });

        ferreterias.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    estados[4] = true;
                }else{
                    estados[4] = false;
                }
            }
        });

        hotel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    estados[5] = true;
                }else{
                    estados[5] = false;
                }
            }
        });

        supermercados.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    estados[6] = true;
                }else{
                    estados[6] = false;
                }
            }
        });

        zapateria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    estados[7] = true;
                }else{
                    estados[7] = false;
                }
            }
        });

        panaderia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    estados[8] = true;
                }else{
                    estados[8] = false;
                }
            }
        });

    }

    public void subirFiltros(){
        BaseDatos db = new BaseDatos(getActivity());
        db.subirFiltros(estados[0], estados[1], estados[2], estados[3], estados[4], estados[5], estados[6], estados[7], estados[8]);
    }

    public void marcados(){
        accesorios.setChecked(estados[0]);
        restaurantes.setChecked(estados[1]);
        autos.setChecked(estados[2]);
        ropas.setChecked(estados[3]);
        ferreterias.setChecked(estados[4]);
        hotel.setChecked(estados[5]);
        supermercados.setChecked(estados[6]);
        zapateria.setChecked(estados[7]);
        panaderia.setChecked(estados[8]);
    }

}
