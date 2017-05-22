package proyectos.carosdrean.xyz.encuentralo;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import proyectos.carosdrean.xyz.encuentralo.BaseDatos.BaseDatos;
import proyectos.carosdrean.xyz.encuentralo.Fragments.ComoLlegar;

public class PerfilEmpresa extends AppCompatActivity {

    private TextView direccion;
    private TextView descripcion;
    private TextView facebook;
    private TextView web;
    private TextView productos;
    private TextView telefono;
    private ImageView portadaT;

    private boolean terminar = false;

    final String[] duracions = {""};
    final String[] distancias = {""};
    TextView distancia;
    TextView duracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_empresa);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPerfil);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("nombre"));

        inicializar();
        obtenerPortada();
        reemplazar();

        direccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(terminar){
                    subirLlegada();
                    inflarMapaLlegar();
                    recibirDatosComoLlegar();
                }else{
                    Toast.makeText(PerfilEmpresa.this, "Descargando datos...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public AlertDialog inflarMapaLlegar(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View v = inflater.inflate(R.layout.como_llegar, null);
        distancia = (TextView)v.findViewById(R.id.tvDistance);
        duracion = (TextView)v.findViewById(R.id.tvDuration);
        builder.setView(v)
                .setTitle("Ruta que debe Seguir")
                .setPositiveButton("Listo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getSupportFragmentManager().beginTransaction().remove(new ComoLlegar()).commit();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public void recibirDatosComoLlegar(){
        DatabaseReference dbComoLlegar= FirebaseDatabase.getInstance().getReference()
                .child("DatosComoLlegar");

        dbComoLlegar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                duracions[0] = String.valueOf(dataSnapshot.child("Distancia").getValue());
                distancias[0] = String.valueOf(dataSnapshot.child("Tiempo").getValue());
                if(!duracions[0].equals("")){
                    distancia.setText(distancias[0]);
                    duracion.setText(duracions[0]);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void inicializar(){
        direccion   = (TextView) findViewById(R.id.ubicacionPerfil);
        descripcion = (TextView) findViewById(R.id.descripcionPerfil);
        facebook    = (TextView) findViewById(R.id.facebookPerfil);
        web         = (TextView) findViewById(R.id.webperfil);
        productos   = (TextView) findViewById(R.id.productosPerfil);
        telefono    = (TextView) findViewById(R.id.telefonoPerfil);
        portadaT    = (ImageView)findViewById(R.id.image_paralax);
    }

    public void reemplazar(){
        direccion.setText(getIntent().getStringExtra("direccion"));
        descripcion.setText(getIntent().getStringExtra("descripcion"));
        facebook.setText(getIntent().getStringExtra("facebook"));
        web.setText(getIntent().getStringExtra("web"));
        productos.setText(getIntent().getStringExtra("productos"));
        telefono.setText(getIntent().getStringExtra("telefono"));

    }

    public void obtenerPortada(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference portada = storageReference.child("images").child(getIntent().getStringExtra("portada"));
        try {
            final File localFile = File.createTempFile("images", "jpg");
            portada.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Glide.with(getApplicationContext()).load(localFile).into(portadaT);
                    terminar = true;
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
    }

    public void subirLlegada(){
        //tiene que estar asociado al id del usuario
        BaseDatos db = new BaseDatos(this);
        db.subirLlegada(direccion.getText().toString(),getIntent().getStringExtra("nombre"));
    }
}
