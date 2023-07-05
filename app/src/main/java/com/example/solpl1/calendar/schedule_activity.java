package com.example.solpl1.calendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class schedule_activity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap gMap;
    private List<LatLng> inputLatLngList = new ArrayList<>();
    private List<String> placeNameList = new ArrayList<>();
    private static final String TAG = "MapActivity";

    private List<PlaceData> placeList;
    private RecyclerView recyclerView;
    private schedule_adapter adapter;
    private Button edit_btn;
    private SupportMapFragment mapFragment;
    private PlacesClient placesClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        recyclerView = findViewById(R.id.place_recycler);

        placeList = new ArrayList<>();
        adapter = new schedule_adapter(placeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        edit_btn = findViewById(R.id.edit_loc);
        //place 초기화
        Places.initialize(getApplicationContext(), "AIzaSyDd8DVD7LzbkEm3esLHEOWgUwIdruqONIA");
        placesClient = Places.createClient(this);
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
                    LatLng inputLatLng = place.getLatLng();
                    Log.d(TAG, "Selected Place: " + place.getName());
                    Log.d(TAG, "Input_LatLng: " + inputLatLng);
                    String placeName = place.getName();
                    // 선택한 장소의 위치와 이름을 리스트에 추가
                    inputLatLngList.add(inputLatLng);
                    placeNameList.add(placeName); // 타이틀에 번호 추가

                    fetchPlacePhoto(place, placeName);
                    // 마커 추가 후 지도 새로고침
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(schedule_activity.this);
                    }
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        // google map Fragment
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.schedule_map);
        mapFragment.getMapAsync(this);

    }
    private void fetchPlacePhoto(Place place, String placeName) {
        // 장소의 사진 메타데이터 가져오기
        List<PhotoMetadata> photoMetadataList = place.getPhotoMetadatas();
        if (photoMetadataList == null || photoMetadataList.isEmpty()) {
            Log.w(TAG, "No photo metadata.");
            return;
        }

        // 첫 번째 사진 메타데이터 가져오기
        PhotoMetadata photoMetadata = photoMetadataList.get(0);

        // FetchPhotoRequest 생성
        FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .setMaxWidth(500)
                .setMaxHeight(500)
                .build();

        // 장소의 사진 가져오기
        placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
            Bitmap bitmap = fetchPhotoResponse.getBitmap();
            // PlaceData 객체를 생성하여 어댑터에 추가
            PlaceData placeData = new PlaceData(placeName, bitmap);
            placeList.add(placeData);
            adapter.setData(placeList); // 어댑터에 데이터 설정 및 화면 업데이트
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place photo not found: " + apiException.getStatusCode());
            }
        });
    }

        @Override
    public void onMapReady(@Nullable GoogleMap googleMap) {
        if (googleMap == null) {
            return;
        }

        googleMap.clear(); // 이전 마커 제거

        // 모든 장소의 마커 추가
        for (int i = 0; i < inputLatLngList.size(); i++) {
            LatLng inputLatLng = inputLatLngList.get(i);
            String placeName = placeNameList.get(i);
            String markerNumber = (i + 1) + "."; // 번호를 설정합니다.

            // 마커 옵션을 설정합니다.
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(inputLatLng)
                    .title(markerNumber) // 마커의 타이틀에 숫자를 포함시킵니다.
                    .snippet(placeName); // 마커의 설명에 장소 이름을 표시합니다.

            googleMap.addMarker(markerOptions);
        }

        // 마지막 장소로 카메라 이동
        if (!inputLatLngList.isEmpty()) {
            LatLng lastLatLng = inputLatLngList.get(inputLatLngList.size() - 1);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng,10));
        }

        // 마커 간의 점선 연결
        if (inputLatLngList.size() > 1) {
            PolylineOptions polylineOptions = new PolylineOptions()
                    .color(Color.RED)
                    .width(5f)
                    .pattern(Arrays.asList(new Dot(), new Gap(10f)));

            for (int i = 0; i < inputLatLngList.size() - 1; i++) {
                LatLng startPoint = inputLatLngList.get(i);
                LatLng endPoint = inputLatLngList.get(i + 1);

                polylineOptions.add(startPoint, endPoint);
            }

            googleMap.addPolyline(polylineOptions);
        }

        // 마커 간의 거리 계산 및 출력
//        for (int i = 0; i < inputLatLngList.size() - 1; i++) {
//            LatLng startPoint = inputLatLngList.get(i);
//            LatLng endPoint = inputLatLngList.get(i + 1);
//
//            double distance = SphericalUtil.computeDistanceBetween(startPoint, endPoint);
//            Log.d(TAG, "Distance between markers " + i + " and " + (i + 1) + ": " + distance + " meters");
//        }
    }


}
