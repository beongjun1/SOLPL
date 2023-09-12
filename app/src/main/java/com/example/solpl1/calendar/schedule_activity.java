package com.example.solpl1.calendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private Date selectedDate;

    String selectedDay;
    private String key;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        if (intent != null) {
            selectedDay = intent.getStringExtra("selected_day");
            key = intent.getStringExtra("key");
            Log.d("key", "key: " + key);

            // 이제 selectedDay를 사용하여 해당 일차에 맞는 일정 데이터를 표시하면 됨
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            selectedDate = sdf.parse(selectedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }



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
                uploadImagesAndSaveToDatabase(placeList);
            }
        });


    }


    private void fetchPlacePhoto(@NonNull Place place, String placeName) {


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
                    .setMaxWidth(300)
                    .setMaxHeight(300)
                    .build();
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                String openingHours = getOpeningHoursForSelectedDay(detailedPlace, selectedDate);
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
    private String getOpeningHoursForSelectedDay(Place place, Date selectedDate) {
        if (place.getOpeningHours() != null && place.getOpeningHours().getWeekdayText() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            String selectedDayString = new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime());

            for (String weekdayText : place.getOpeningHours().getWeekdayText()) {
                if (weekdayText.startsWith(selectedDayString)) {
                    // weekdayText는 예: "Monday: 8:00 AM – 6:00 PM" 형식일 수 있습니다.
                    String[] parts = weekdayText.split(": ");
                    if (parts.length == 2) {
                        String openingHours = parts[1].trim();
                        return openingHours;
                    }
                }
            }
        }
        return "";
    }
private void uploadImagesAndSaveToDatabase(List<PlaceData> placeList) {
    // Firebase 인스턴스 가져오기
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String currentUserId = user.getUid();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    DatabaseReference tripRef = database.getReference("trip");

    for (int i = 0; i < placeList.size(); i++) {
        PlaceData placeData = placeList.get(i);
        String filePath = user.getEmail()+"/place/" + key + "/" + selectedDay + "/";
        Bitmap imageBitmap = placeData.getImageBitmap();
        String imageName = "place_image_" + System.currentTimeMillis() + "_" + i + ".jpg";
        StorageReference imageRef = storageRef.child(filePath + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imageRef.putBytes(data);

        int finalI = i; // Effective final variable for the listener

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();



                Map<String, Object> placeMap = new HashMap<>();
                placeMap.put("name", placeData.getName());
                placeMap.put("time", placeData.getTime());
                placeMap.put("imageUrl", imageUrl);
                placeMap.put("user_id", currentUserId);
//                placeMap.put("place_key", place_key);
                placeMap.put("day", selectedDay);

                // 추가 작업: Firebase Realtime Database에 데이터 저장
                savePlaceDataToDatabase(tripRef, placeMap, finalI, placeList.size());
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Failed to get image URL: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to upload image: " + e.getMessage());
        });
    }
}

    private void savePlaceDataToDatabase(DatabaseReference tripRef, Map<String, Object> placeMap, int currentIndex, int totalSize) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userEmail = user.getEmail();
        tripRef.child(convertToValidPath(userEmail)).child(key).child("place").child(selectedDay).push().setValue(placeMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("DataInsertion", "Data inserted successfully: " + placeMap.toString());

                        if (currentIndex == totalSize - 1) {
                            // 마지막 장소 데이터 저장이 완료되면 호출
                            handleAllDataInserted();
                        }
                    } else {
                        Log.e("DataInsertion", "Failed to insert data: " + task.getException());
                    }
                });
    }

    private void handleAllDataInserted() {
        // 모든 장소 데이터 추가가 완료되면 호출되는 메서드
        // 추가 작업을 수행하거나 화면을 업데이트하는 등의 동작을 실행
        Intent intent = new Intent();
        intent.putExtra("selectedDay", selectedDay);
        setResult(RESULT_OK, intent);
        finish();
    }

    //문자열을 데이터베이스 경로에 사용 가능한 형식으로 변환
    public static String convertToValidPath(String input) {
        // 허용되는 문자: 알파벳 소문자, 숫자, 밑줄(_)
        String validCharacters = "abcdefghijklmnopqrstuvwxyz0123456789_";

        // 입력된 문자열을 소문자로 변환하고 허용되지 않는 문자를 밑줄로 대체
        String converted = input.toLowerCase().replaceAll("[^" + validCharacters + "]", "_");

        return converted;
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