package proyectos.carosdrean.xyz.encuentralo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import proyectos.carosdrean.xyz.encuentralo.BaseDatos.BaseDatos;
import proyectos.carosdrean.xyz.encuentralo.R;

/**
 * Created by josue on 3/05/2017.
 */

public class SeleccionarDireccion extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    public Marker marcador;

    public SeleccionarDireccion() {
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
        agregarMarcador(-14.063585,-75.729179);
        map.setOnMarkerDragListener(this);

    }

    private void agregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Mover para seleccionar")
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_casa))
        );
        mMap.animateCamera(miUbicacion);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        BaseDatos db = new BaseDatos(getActivity());
        db.subirPosicionMarcador(marker.getPosition());
    }

}
