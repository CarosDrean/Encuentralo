package proyectos.carosdrean.xyz.encuentralo.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import proyectos.carosdrean.xyz.encuentralo.BaseDatos.BaseDatos;
import proyectos.carosdrean.xyz.encuentralo.Interfaces.DirectionFinderListener;
import proyectos.carosdrean.xyz.encuentralo.Modulos.DirectionFinder;
import proyectos.carosdrean.xyz.encuentralo.Modulos.Route;
import proyectos.carosdrean.xyz.encuentralo.R;

/**
 * Created by josue on 9/05/2017.
 */

public class ComoLlegar extends SupportMapFragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, DirectionFinderListener{
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private GoogleMap mMap;
    private GoogleApiClient apiClient;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;

    boolean map = false;

    LatLng coordenadas = null;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    private String llegada;
    private String titulo;

    public ComoLlegar() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if(!map){
            apiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(getActivity(), this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
            map = true;
        }

        recibirLlegada();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= super.onCreateView(inflater, container, savedInstanceState);
        getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        apiClient.stopAutoManage(getActivity());
        apiClient.disconnect();
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
        coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        mMap.animateCamera(miUbicacion);
        verificarDatos();
    }

    public void verificarDatos(){
        if(lat != 0.0 && lng != 0.0 && llegada != null){
            sendRequest();
        }
    }

    public void recibirLlegada(){
        DatabaseReference dbMarcador = FirebaseDatabase.getInstance().getReference()
                .child("Llegada");

        dbMarcador.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                llegada = String.valueOf(dataSnapshot.child("posicion").getValue());
                titulo = String.valueOf(dataSnapshot.child("titulo").getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendRequest() {
        String origin = String.valueOf(lat) + "," + String.valueOf(lng);
        String destination = llegada;

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(getActivity(), "Buscando...",
                "¡Puede usar rutas alternativas!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        BaseDatos db = new BaseDatos(getActivity());
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));

            db.subirDetallesComolegar(route.distance.text, route.duration.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_casa))
                    .title(route.startAddress)
                    .position(route.startLocation)));

            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_casa))
                    .title(titulo)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
