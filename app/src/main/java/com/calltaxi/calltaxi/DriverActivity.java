package com.calltaxi.calltaxi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class DriverActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ContextCompat.checkSelfPermission(DriverActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        } else {
                ActivityCompat.requestPermissions(DriverActivity.this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        44);
        }

        ActivityCompat.requestPermissions(DriverActivity.this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                44);
    }

    private void enableUserLocation() {
        mMap.setMyLocationEnabled(true);
    }
}
