package com.example.pasantias.appcamaleon;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class UbicacionCliente extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    Double latitudCliente;
    Double longitudCliente;
    LatLng ubicacionDelCliente;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_ubicacion_cliente, container,false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            latitudCliente = bundle.getDouble("LatCliente");
            longitudCliente = bundle.getDouble("LngCliente");
            ubicacionDelCliente = new LatLng(latitudCliente,longitudCliente);
        } else {
            Toast.makeText(getContext(), "No hay datos que presentar", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(15);

        mMap.addMarker(new MarkerOptions().position(ubicacionDelCliente).title("Cliente"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacionDelCliente));
    }
}
