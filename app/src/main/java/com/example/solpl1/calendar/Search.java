package com.example.solpl1.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.solpl1.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class Search extends AppCompatActivity {
    private static LatLng Input_LatLng = null;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "FinalTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_search);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Places.initialize(getApplicationContext(), "AIzaSyDd8DVD7LzbkEm3esLHEOWgUwIdruqONIA");
        // 자동완성 Fragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);

        autocompleteFragment.setCountries("KR");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if (!TextUtils.isEmpty(place.getName())) {
                    Input_LatLng = place.getLatLng();
                    Log.d(TAG, "Selected Place: " + place.getName());
                    Log.d(TAG, "Input_LatLng: " + Input_LatLng);
                    Intent intent = new Intent(Search.this, schedule_activity.class);
                    intent.putExtra("Input_LatLng", Input_LatLng);
                    intent.putExtra("PlaceName", place.getName());
                    startActivity(intent);
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }
}