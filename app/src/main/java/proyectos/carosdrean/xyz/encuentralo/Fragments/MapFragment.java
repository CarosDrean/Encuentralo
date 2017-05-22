package proyectos.carosdrean.xyz.encuentralo.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import proyectos.carosdrean.xyz.encuentralo.Pojo.Servicio;
import proyectos.carosdrean.xyz.encuentralo.R;

/**
 * Created by josue on 3/05/2017.
 */

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private GoogleMap mMap;
    private GoogleApiClient apiClient;

    double lat = 0.0;
    double lng = 0.0;

    boolean map = false;

    final ArrayList<LatLng> marcadores = new ArrayList<>();
    final ArrayList<String> titulos = new ArrayList<>();

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        /*try{
            recibirMostrar();
        }catch (Exception e){

        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        agregarMarcador(-14.045027749484337, -75.75245138257742);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try{
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PETICION_PERMISO_LOCALIZACION);
            } else {

                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);
            }
        }catch (Exception e){

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);

            }
        }
    }

    private void updateUI(Location loc) {
        if (loc != null) {
            lat = loc.getLatitude();
            lng = loc.getLongitude();
            agregarMarcador(lat, lng);
        }
    }

    private void agregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 14);

        mMap.animateCamera(miUbicacion);
        mostrarDomi();
    }

    public void administrarMarcadores(boolean[] mostrar){
        String[] categorias = {"Accesorios", "Restaurantes", "Autos", "Ropas", "Ferreterias", "Hotel", "Supermercado", "Zapaterias", "Panaderias"};
        for (int i = 0; i < categorias.length; i++) {
            if(mostrar[i]){
                recibirMarcadores(categorias[i]);
            }
        }
    }

    public void recibirMostrar(){
        boolean ac = true;
        boolean res = false;
        boolean au = false;
        boolean rop = false;
        boolean ferr = false;
        boolean hot = false;
        boolean sup = false;
        boolean zap = false;
        boolean apn = false;

        final boolean[] mostrar = {ac, res, au, rop, ferr, hot, sup, zap, apn};
        final String[] pos = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

        DatabaseReference dbMostrar = FirebaseDatabase.getInstance().getReference()
                .child("Marcadores")
                .child("Mostrar");

        dbMostrar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 0; i < mostrar.length; i++) {
                    try{
                        mostrar[i] = (Boolean)dataSnapshot.child(pos[i]).getValue();
                    }catch (Exception e){

                    }
                }

                administrarMarcadores(mostrar);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void mostrarDomi(){
        DatabaseReference dbMarcador = FirebaseDatabase.getInstance().getReference()
                .child("Empresas")
                .child("Autos");

        dbMarcador.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String[] latlng = dataSnapshot.getValue(Servicio.class).getDireccion().split(",");
                double latitude = Double.parseDouble(latlng[0]);
                double longitude = Double.parseDouble(latlng[1]);
                marcadores.add(new LatLng(latitude, longitude));
                titulos.add(dataSnapshot.getValue(Servicio.class).getNombreServicio());
                agregarMarcadores();
                CameraUpdate acercar = CameraUpdateFactory.newLatLngZoom(marcadores.get(0), 13);
                mMap.animateCamera(acercar);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void recibirMarcadores(String categoria){
        mMap.clear();
        DatabaseReference dbMarcador = FirebaseDatabase.getInstance().getReference()
                .child("Empresas")
                .child(categoria);

        dbMarcador.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String[] latlng = dataSnapshot.getValue(Servicio.class).getDireccion().split(",");
                double latitude = Double.parseDouble(latlng[0]);
                double longitude = Double.parseDouble(latlng[1]);
                marcadores.add(new LatLng(latitude, longitude));
                titulos.add(dataSnapshot.getValue(Servicio.class).getNombreServicio());
                agregarMarcadores();
                CameraUpdate acercar = CameraUpdateFactory.newLatLngZoom(marcadores.get(0), 13);
                mMap.animateCamera(acercar);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void agregarMarcadores(){
        Marker marcador;
        for (int i = 0; i < marcadores.size(); i++) {
            marcador = mMap.addMarker(new MarkerOptions()
                    .position(marcadores.get(i))
                    .title(titulos.get(i))
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_casa))
            );
        }
    }
}