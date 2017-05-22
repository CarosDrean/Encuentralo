package proyectos.carosdrean.xyz.encuentralo.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import proyectos.carosdrean.xyz.encuentralo.Adaptador.AdaptadorCategoria;
import proyectos.carosdrean.xyz.encuentralo.Pojo.Categoria;
import proyectos.carosdrean.xyz.encuentralo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Categorias extends Fragment {

    ArrayList<Categoria> categorias;
    private RecyclerView listacategoria;

    public Categorias() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categorias, container, false);
        listacategoria = (RecyclerView)v.findViewById(R.id.lista_explorar);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        listacategoria.setLayoutManager(llm);

        inicializar();
        inicializarAdaptador();
        return v;
    }

    public void inicializarAdaptador(){
        AdaptadorCategoria adaptadorCategoria = new AdaptadorCategoria(categorias, getActivity());
        listacategoria.setAdapter(adaptadorCategoria);
    }

    public void inicializar(){
        categorias = new ArrayList<>();

        categorias.add(new Categoria("Accesorios", R.drawable.accesorioscell));
        categorias.add(new Categoria("Restaurantes", R.drawable.restaurante));
        categorias.add(new Categoria("Autos", R.drawable.auto));
        categorias.add(new Categoria("Ropas", R.drawable.ropas));
        categorias.add(new Categoria("Ferreterias", R.drawable.ferreteria));
        categorias.add(new Categoria("Hotel", R.drawable.hotel));
        categorias.add(new Categoria("Supermercado", R.drawable.supermecado));
        categorias.add(new Categoria("Zapaterias", R.drawable.zapatos));
        categorias.add(new Categoria("Panaderias", R.drawable.panaderia));
    }

}
