package com.example.solpl1.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
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
                intent.putExtra("place_address",place_address.getText());
                intent.putExtra("place_tel",place_tel.getText());
                intent.putExtra("place_rating",place.getRating());
                intent.putExtra("resion",place.getResion());
                if(place_title.getText().equals("에버랜드"))intent.putExtra("place_image",R.drawable.everland);
                else if(place_title.getText().equals("순천만 습지"))intent.putExtra("place_image",R.drawable.suncheonman);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        //여행지
        LatLng SunCheonMan = new LatLng(34.523, 127.28);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SunCheonMan);
        markerOptions.title("순천만 습지");

        LatLng EverLand = new LatLng(37.29393, 127.2026);
        MarkerOptions markerEverLand = new MarkerOptions();
        markerEverLand.position(EverLand);
        markerEverLand.title("에버랜드");

        //오산시 여행지
        LatLng Mulhyanggi = new LatLng(37.168601, 127.059017);
        MarkerOptions markerMulhyanggi = new MarkerOptions();
        markerMulhyanggi.position(Mulhyanggi);
        markerMulhyanggi.title("물향기수목원");

        LatLng Gwollisa = new LatLng(37.16000, 127.06194);
        MarkerOptions markerGwollisa = new MarkerOptions();
        markerGwollisa.position(Gwollisa);
        markerGwollisa.title("궐리사");

        LatLng Doksanseong = new LatLng(37.183611, 127.019444);
        MarkerOptions markerDoksanseong = new MarkerOptions();
        markerDoksanseong.position(Doksanseong);
        markerDoksanseong.title("오산 독산성");

        LatLng MalgeumteoPark = new LatLng(37.137936, 127.064594);
        MarkerOptions markerMalgeumteoPark = new MarkerOptions();
        markerMalgeumteoPark.position(MalgeumteoPark);
        markerMalgeumteoPark.title("맑음터공원");

        LatLng OsanEcorium = new LatLng(37.138397, 127.063806);
        MarkerOptions markerOsanEcorium = new MarkerOptions();
        markerOsanEcorium.position(OsanEcorium);
        markerOsanEcorium.title("오산 에코리움");

        LatLng Jukmiryeong = new LatLng(37.184705, 127.049343);
        MarkerOptions markerJukmiryeong  = new MarkerOptions();
        markerJukmiryeong.position(Jukmiryeong);
        markerJukmiryeong.title("오산 죽미령 평화공원");

        LatLng SeorangCulturalVillage = new LatLng(37.173183, 127.005750);
        MarkerOptions markerSeorangCulturalVillage  = new MarkerOptions();
        markerSeorangCulturalVillage.position(SeorangCulturalVillage);
        markerSeorangCulturalVillage.title("서랑동 문화마을");



        //경기도 고양
        LatLng Seooreung = new LatLng(37.623596, 126.900767);
        MarkerOptions markerSeooreung  = new MarkerOptions();
        markerSeooreung.position(Seooreung);
        markerSeooreung.title("서오릉");

        LatLng lakePark = new LatLng(37.656568, 126.766229);
        MarkerOptions markerlakePark  = new MarkerOptions();
        markerlakePark.position(lakePark);
        markerlakePark.title("호수공원");

        LatLng AngokWetlandPark = new LatLng(37.684559, 126.783466);
        MarkerOptions markerAngokWetlandPark  = new MarkerOptions();
        markerAngokWetlandPark.position(AngokWetlandPark);
        markerAngokWetlandPark.title("안곡습지공원");

        LatLng Gongneungcheon = new LatLng(37.713228, 126.835623);
        MarkerOptions markerGongneungcheon  = new MarkerOptions();
        markerGongneungcheon.position(Gongneungcheon);
        markerGongneungcheon.title("공릉천");

        LatLng OneMount = new LatLng(37.665113, 126.754823);
        MarkerOptions markerOneMount  = new MarkerOptions();
        markerOneMount.position(OneMount);
        markerOneMount.title("원마운트");




        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String markerId = marker.getTitle();
                if(markerId.equals("순천만 습지")){
                    place_image.setImageResource(R.drawable.suncheonman);
                    place_title.setText("순천만 습지");
                }
                if(markerId.equals("에버랜드")){
                    place_image.setImageResource(R.drawable.everland);
                    place_title.setText("에버랜드");
                }

                databaseReference.child(markerId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        place = snapshot.getValue(PLACE.class);
                        place_address.setText("주소:"+place.getAddress());
                        place_tel.setText("번호:"+place.getTel());
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
        mMap.addMarker(markerEverLand);

        // 대한민국 전체 한눈에 들어오도록 초기 배율 및 화면 초기화
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.34,127.77), 7));
    }
}
