package proyectos.carosdrean.xyz.encuentralo.BaseDatos;

import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import proyectos.carosdrean.xyz.encuentralo.Pojo.Servicio;

/**
 * Created by josue on 3/05/2017.
 */

public class BaseDatos {

    private Activity activity;

    public BaseDatos(Activity activity) {
        this.activity = activity;
    }

    public void ingresarDatos(String id, Servicio servicio, String categoria){
        DatabaseReference dbEmpresa = FirebaseDatabase.getInstance().getReference()
                .child("Empresas")
                .child(categoria);

        dbEmpresa.child(id).setValue(servicio);
    }

    public void subirPosicionMarcador(LatLng posicion){
        DatabaseReference dbmarcador = FirebaseDatabase.getInstance().getReference()
                .child("Marcador");
        dbmarcador.child("posicion").setValue(posicion);
    }

    public void subirLlegada(String llegada, String titulo){
        DatabaseReference dbmarcador = FirebaseDatabase.getInstance().getReference()
                .child("Llegada");
        dbmarcador.child("posicion").setValue(llegada);
        dbmarcador.child("titulo").setValue(titulo);
    }

    public void subirDetallesComolegar(String distancia, String tiempo){
        DatabaseReference dbComoLlegar = FirebaseDatabase.getInstance().getReference()
                .child("DatosComoLlegar");
        dbComoLlegar.child("Distancia").setValue(distancia);
        dbComoLlegar.child("Tiempo").setValue(tiempo);
    }

    public void subirLogo(Uri uri){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = storageReference.child("images").child(uri.getLastPathSegment());

        riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(activity, "¡Se subio con exito!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void subirFiltros(boolean u, boolean d, boolean t, boolean c, boolean ci, boolean s, boolean si, boolean o, boolean n){
        DatabaseReference dbMostrar = FirebaseDatabase.getInstance().getReference()
                .child("Marcadores")
                .child("Mostrar");

        dbMostrar.child("1").setValue(u);
        dbMostrar.child("2").setValue(d);
        dbMostrar.child("3").setValue(t);
        dbMostrar.child("4").setValue(c);
        dbMostrar.child("5").setValue(ci);
        dbMostrar.child("6").setValue(s);
        dbMostrar.child("7").setValue(si);
        dbMostrar.child("8").setValue(o);
        dbMostrar.child("9").setValue(n);
    }
}
