package proyectos.carosdrean.xyz.encuentralo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import proyectos.carosdrean.xyz.encuentralo.BaseDatos.BaseDatos;
import proyectos.carosdrean.xyz.encuentralo.Fragments.SeleccionarDireccion;
import proyectos.carosdrean.xyz.encuentralo.Pojo.Servicio;

public class AgregarServicio extends AppCompatActivity {

    private static final int SELECT_PICTURE = 200;

    private int id;
    private Spinner categoria;
    private EditText nombreServicio;
    private TextView direccion;
    private EditText descripcion;
    private ImageView logo;
    private EditText facebook;
    private EditText web;
    private EditText productos;
    private String portada;
    private EditText telefono;
    private int fotos;

    private String idE;
    private String categoriaE;

    private boolean ubicacion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_servicio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAS);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Servicio Nuevo");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_close);
        }

        inicializar();

        direccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubicacion = true;
                try{
                    inflarMapaUbicacion();
                }catch (Exception e){
                    Toast.makeText(AgregarServicio.this, "¡Ubicación ya seleccionada!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarGalerias();
            }
        });

        obtenerPosicion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agregar_servicio, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BaseDatos db = new BaseDatos(this);
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                if(verificarCampos()){
                    idE = UUID.randomUUID().toString();
                    categoriaE = categoria.getSelectedItem().toString();
                    db.ingresarDatos(idE, obtenerDatos(), categoriaE);
                    Toast.makeText(this, "¡Se registro con exito!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else {
                    Toast.makeText(this, "¡Por favor llene todos los campos!", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        BaseDatos db = new BaseDatos(this);
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == SELECT_PICTURE){
            Uri path = data.getData();
            Glide.with(this).load(path).into(logo);
            db.subirLogo(path);
            portada = path.getLastPathSegment();
        }
    }

    public AlertDialog inflarMapaUbicacion(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_select_ubicacion, null);

        builder.setView(v)
                .setTitle("Seleccionar Ubicación")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

    public void lanzarGalerias(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Seleccionar Imagen"), SELECT_PICTURE);
    }

    public void inicializar(){
        categoria = (Spinner) findViewById(R.id.spinner_categoria);
        nombreServicio = (EditText)findViewById(R.id.nombreE);
        direccion = (TextView)findViewById(R.id.direccionE);
        descripcion = (EditText)findViewById(R.id.descripcionE);
        logo = (CircleImageView) findViewById(R.id.logoE);
        facebook = (EditText)findViewById(R.id.facebookE);
        web = (EditText)findViewById(R.id.webE);
        productos = (EditText)findViewById(R.id.productosE);
        telefono = (EditText)findViewById(R.id.telefonoE);
    }

    public boolean verificarCampos(){
        boolean verificar = true;
        if(descripcion.getText().toString().equals("")
                && facebook.getText().toString().equals("")
                && telefono.getText().toString().equals("")
                && web.getText().toString().equals("")
                && nombreServicio.getText().toString().equals("")
                && productos.getText().toString().equals("")){
            verificar = false;
        }
        return verificar;
    }

    public Servicio obtenerDatos(){
        Servicio servicio = new Servicio();

        servicio.setId(idE);
        servicio.setCategoria(categoria.getSelectedItem().toString());
        servicio.setDescripcion(descripcion.getText().toString());
        servicio.setDireccion(direccion.getText().toString());
        servicio.setFacebook(facebook.getText().toString());
        servicio.setNombreServicio(nombreServicio.getText().toString());
        servicio.setProductos(productos.getText().toString());
        servicio.setPortada(portada);
        servicio.setTelefono(telefono.getText().toString());
        servicio.setWeb(web.getText().toString());

        return servicio;
    }

    public void obtenerPosicion(){
        DatabaseReference dbMarcador = FirebaseDatabase.getInstance().getReference()
                .child("Marcador")
                .child("posicion");

        final String[] pos = {""};

        dbMarcador.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    pos[0] = String.valueOf(dataSnapshot.child("latitude").getValue()) + "," + String.valueOf(dataSnapshot.child("longitude").getValue());
                    if(ubicacion) direccion.setText(pos[0]);
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}