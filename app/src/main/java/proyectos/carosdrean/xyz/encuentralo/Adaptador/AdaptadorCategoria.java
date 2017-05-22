package proyectos.carosdrean.xyz.encuentralo.Adaptador;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import proyectos.carosdrean.xyz.encuentralo.DetallesCategorias;
import proyectos.carosdrean.xyz.encuentralo.Pojo.Categoria;
import proyectos.carosdrean.xyz.encuentralo.R;

/**
 * Created by josue on 3/05/2017.
 */

public class AdaptadorCategoria extends RecyclerView.Adapter<AdaptadorCategoria.CategoriaViewHolder> {

    ArrayList<Categoria> categorias;
    Activity activity;

    public AdaptadorCategoria(ArrayList<Categoria> categorias, Activity activity) {
        this.categorias = categorias;
        this.activity = activity;
    }

    @Override
    public CategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categorias, parent, false);
        return new CategoriaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CategoriaViewHolder categoriaViewHolder, int position) {
        final Categoria categoria = categorias.get(position);
        categoriaViewHolder.categoria.setText(categoria.getCategoria());
        Glide.with(activity).load(categoria.getFondo()).into(categoriaViewHolder.fondo);

        categoriaViewHolder.categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, DetallesCategorias.class);
                i.putExtra("categoria", categoria.getCategoria());
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder{

        private TextView categoria;
        private ImageView fondo;

        public CategoriaViewHolder(View itemView) {
            super(itemView);
            categoria = (TextView)itemView.findViewById(R.id.categoria);
            fondo = (ImageView)itemView.findViewById(R.id.fondo_categoria);
        }
    }

}
