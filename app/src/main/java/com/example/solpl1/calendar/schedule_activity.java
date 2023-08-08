package com.example.solpl1.calendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class schedule_activity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap gMap;
    private List<LatLng> inputLatLngList = new ArrayList<>();
    private List<String> placeNameList = new ArrayList<>();

    private static final String TAG = "MapActivity";

    private List<PlaceData> placeList;
    private List<DayData> dayList;

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


        Places.initialize(getApplicationContext(), "AIzaSyDd8DVD7LzbkEm3esLHEOWgUwIdruqONIA");
        placesClient = Places.createClient(this);

        recyclerView = findViewById(R.id.place_recycler);

        placeList = new ArrayList<>();
        adapter = new schedule_adapter(placeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        edit_btn = findViewById(R.id.edit_loc);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
        autocompleteFragment.setCountries("KR");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // 원하는 방식으로 day와 tripDuration을 비교하여 검색 로직을 구현하세요.
        // 예시: 특정 일차를 클릭했을 때 검색을 수행하고 결과를 표시4

        // day에 해당하는 일차를 클릭했을 때의 검색 로직을 구현하세요.
        // 실제 장소 정보를 검색하여 RecyclerView와 지도에 추가하는 메서드 호출

        //검색 및 마커
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

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlaceToDatabase(placeList);
            }
        });


    }


    private void fetchPlacePhoto(Place place, String placeName) {

        // Define a Place ID.
        final String placeId = place.getId();

        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        final List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS, Place.Field.OPENING_HOURS );

        // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            Place detailedPlace = response.getPlace();

            // 장소의 사진 메타데이터 가져오기
            List<PhotoMetadata> photoMetadataList = detailedPlace.getPhotoMetadatas();
            if (photoMetadataList == null || photoMetadataList.isEmpty()) {
                Log.w(TAG, "No photo metadata.");
                return;
            }

            // 첫 번째 사진 메타데이터 가져오기
            PhotoMetadata photoMetadata = photoMetadataList.get(0);

            // FetchPhotoRequest 생성
            FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(50)
                    .setMaxHeight(50)
                    .build();
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                String openingHours = getOpeningHoursForToday(detailedPlace);
//                String imageUrl = detailedPlace.getIconUrl();
                PlaceData placeData = new PlaceData(placeName, bitmap, openingHours);
                placeList.add(placeData);
                adapter.setData(placeList);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place photo not found: " + apiException.getStatusCode());
                }
            });
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
    }

    private void savePlaceToDatabase(List<PlaceData> placeList) {
        // Firebase Realtime Database 인스턴스 가져오기
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // 'places' 노드에 대한 참조 가져오기
        DatabaseReference placesRef = database.getReference("places");
        for (int i = 0; i < placeList.size(); i++) {
            PlaceData placeData = placeList.get(i);


            //이미지를 STORAGE에 업로드 한 뒤 URL 받기
            Bitmap imageBitmap = placeData.getImageBitmap();
            String imageName = "place" + (i + 1) + ".jpg"; // 이미지 파일명 설정
            StorageReference imageRef = storageRef.child(imageName);


            // 이미지를 Firebase Storage에 업로드하는 과정 (이미지를 byte 배열로 변환하여 업로드)
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = imageRef.putBytes(data);
            final int index = i;
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // 업로드가 성공적으로 완료되었을 때 이미지의 URL 받아오기
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    // 이미지 URL을 포함하여 PlaceData 객체 생성
                    PlaceDatabase newPlaceData = new PlaceDatabase(placeData.getName(), imageUrl, placeData.getTime());

                    // Firebase Realtime Database에 PlaceData 객체 저장
                    placesRef.child("place" + (index + 1)).setValue(newPlaceData);
                }).addOnFailureListener(e -> {
                    // 이미지 URL을 받아오는데 실패한 경우
                    Log.e(TAG, "Failed to get image URL: " + e.getMessage());
                });
            }).addOnFailureListener(e -> {
                // 이미지 업로드에 실패한 경우
                Log.e(TAG, "Failed to upload image: " + e.getMessage());
            });


//            Map<String, Object> placeValues = new HashMap<>();
//            placeValues.put("name", placeData.getName());
//            placeValues.put("time", placeData.getTime());
//                placeValues.put("image",placeData.getImageBitmap());
            // 이미지를 Firebase에서 처리 가능한 형태로 변환하여 저장
//            // 예시: placeValues.put("image_url", placeData.getImageUrl());
//
//            placesRef.child("place" + (i + 1)).setValue(placeValues);
        }
    }

    // 오늘 날짜에 맞는 오픈 시간 보여주기
    private String getOpeningHoursForToday(Place place) {
        if (place.getOpeningHours() != null && place.getOpeningHours().getWeekdayText() != null) {
            Calendar calendar = Calendar.getInstance();
            int today = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            String[] weekdays = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
            String todayString = weekdays[today];


            for (String weekdayText : place.getOpeningHours().getWeekdayText()) {
                if (weekdayText.startsWith(todayString)) {
                    String openingHours = weekdayText.substring(todayString.length()+2).trim();
                    String todayTimeString = todayString + ":" + openingHours;
                    return todayTimeString;
                }
            }
        }
        return "";
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
                    .title(markerNumber)
                    .snippet(placeName);

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