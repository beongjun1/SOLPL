package com.example.solpl1.calendar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.solpl1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class schedule_activity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap gMap;
    private static LatLng Input_LatLng = null;
    private static String PlaceName;
    private static final String TAG = "MapActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Intent intent = getIntent();
        PlaceName = intent.getStringExtra("PlaceName");
        Input_LatLng=getIntent().getExtras().getParcelable("Input_LatLng");

        // google map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.schedule_map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@Nullable GoogleMap googleMap) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(PlaceName);
        markerOptions.snippet(PlaceName);
        markerOptions.position(Input_LatLng);
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Input_LatLng, 16));

    }
}
