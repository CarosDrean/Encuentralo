package proyectos.carosdrean.xyz.encuentralo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import proyectos.carosdrean.xyz.encuentralo.Adaptador.DetalleCategoriaViewHolder;
import proyectos.carosdrean.xyz.encuentralo.Pojo.Servicio;

public class DetallesCategorias extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalles_categorias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDc);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getIntent().getStringExtra("categoria"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_close);
        }

        bajarDatos(getIntent().getStringExtra("categoria"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void bajarDatos(String categoria){
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        DatabaseReference dbCategorias =
                FirebaseDatabase.getInstance().getReference()
                        .child("Empresas")
                        .child(categoria);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.lista_detale_categoria);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerAdapter adaptador = new FirebaseRecyclerAdapter<Servicio, DetalleCategoriaViewHolder>
                (Servicio.class, R.layout.item_detalle_categoria, DetalleCategoriaViewHolder.class, dbCategorias) {
            @Override
            protected void populateViewHolder(final DetalleCategoriaViewHolder viewHolder, final Servicio model, int position) {
                viewHolder.setProducto(model.getProductos());
                viewHolder.setNombreServicio(model.getNombreServicio());
                viewHolder.setDescripcion(model.getDescripcion());
                StorageReference portada = storageReference.child("images").child(model.getPortada());
                try {
                    final File localFile = File.createTempFile("images", "jpg");
                    portada.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Glide.with(getApplicationContext()).load(localFile).into(viewHolder.portada);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }

                viewHolder.contenedor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), PerfilEmpresa.class);
                        i.putExtra("id", model.getId());
                        i.putExtra("portada", model.getPortada());
                        i.putExtra("nombre", model.getNombreServicio());
                        i.putExtra("descripcion", model.getDescripcion());
                        i.putExtra("direccion", model.getDireccion());
                        i.putExtra("productos", model.getProductos());
                        i.putExtra("facebook", model.getFacebook());
                        i.putExtra("web", model.getWeb());
                        i.putExtra("telefono", model.getTelefono());
                        startActivity(i);
                    }
                });
            }
        };

        recycler.setAdapter(adaptador);
    }

}
