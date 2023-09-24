package com.example.solpl1.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.solpl1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LinearLayout cardView;
    private ImageView place_image;
    private TextView place_title,place_address,place_tel,place_rating;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("places");
    PLACE place=new PLACE();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        cardView= (LinearLayout) findViewById(R.id.card_view);
        cardView.setVisibility(View.GONE);
        place_image=findViewById(R.id.place_image);
        place_title=findViewById(R.id.place_title);
        place_address=findViewById(R.id.place_address);
        place_tel=findViewById(R.id.place_tel);
        place_rating=findViewById(R.id.place_rating);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMap.this,Place_page.class);
                intent.putExtra("place_title",place_title.getText());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng SunCheonMan = new LatLng(34.523, 127.28);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SunCheonMan);
        markerOptions.title("순천만 습지");

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String markerId = marker.getTitle();
                if(markerId.equals("순천만 습지")){
                    place_image.setImageResource(R.drawable.suncheonman);
                    place_title.setText("순천만 습지");
                }
                databaseReference.child("순천만 습지").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        place = snapshot.getValue(PLACE.class);
                        place_address.setText("주소:"+place.getAddress());
                        place_tel.setText("연락처:"+place.getTel());
                        if(place.getRating()==0)place_rating.setText("평점:리뷰 수집중입니다.");
                        else{
                            place_rating.setText("평점:" + String.valueOf(place.getRating()));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                cardView.setVisibility(View.VISIBLE);
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                cardView.setVisibility(View.GONE);
            }
        });

        mMap.addMarker(markerOptions);

        // 대한민국 전체 한눈에 들어오도록 초기 배율 및 화면 초기화
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.34,127.77), 7));
    }
}
