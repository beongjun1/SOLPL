package com.example.solpl1.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.solpl1.ClickableRecyclerAdapter;
import com.example.solpl1.R;
import com.example.solpl1.my_page_item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class my_page_writing_date_select_activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private my_page_selected_recycler adapter;
    private ArrayList<my_page_item> tripList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_day);

        recyclerView = findViewById(R.id.selectedRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // 어댑터 초기화
        adapter = new my_page_selected_recycler();
        recyclerView.setAdapter(adapter);

        // Firebase 인증 및 데이터베이스 참조
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            String convertedPath = convertToValidPath(userEmail);
            DatabaseReference userTripsRef = FirebaseDatabase.getInstance().getReference("trip").child(convertedPath);

            userTripsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tripList.clear(); // 기존 데이터 초기화

                    for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()) {
                        String tripTitle = tripSnapshot.child("title").getValue(String.class);
                        String tripStart = tripSnapshot.child("startDay").getValue(String.class);
                        String tripEnd = tripSnapshot.child("endDay").getValue(String.class);
                        String firstPlaceKey = tripSnapshot.child("place").child(tripStart).getChildren().iterator().next().getKey();
                        String key = tripSnapshot.child("key").getValue(String.class);
                        Log.d("tripImageUrl1", "key : " + key);
                        // 첫 번째 자식의 키로 이미지 URL 가져오기
                        String tripImageUrl = tripSnapshot.child("place").child(tripStart).child(firstPlaceKey).child("imageUrl").getValue(String.class);
                        Log.d("tripImageUrl1", "trip Url : " + tripImageUrl);

                        String tripDate = (tripStart + " ~ " + tripEnd);
                        // 가져온 값으로 my_page_item 생성
                        my_page_item tripItem = new my_page_item(tripDate, tripTitle, tripImageUrl,key);
                        tripList.add(tripItem);
                    }

                    adapter.setTripList(tripList); // 어댑터에 데이터 설정
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 에러 처리
                }
            });


            adapter.setOnItemClickListener(new my_page_selected_recycler.OnItemClickListener() {
                @Override
                public void onItemClick(my_page_item item) {
                    String selectedDate = item.getDate(); // 선택한 아이템의 date 값을 가져옴

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selectedDate", selectedDate); // 선택한 아이템의 date를 "selectedDate"라는 키로 전달
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });
        }
    }


    // 이메일 주소를 Firebase 경로로 변환하는 함수 (예시)
    //문자열을 데이터베이스 경로에 사용 가능한 형식으로 변환
    public static String convertToValidPath(String input) {
        // 허용되는 문자: 알파벳 소문자, 숫자, 밑줄(_)
        String validCharacters = "abcdefghijklmnopqrstuvwxyz0123456789_";

        // 입력된 문자열을 소문자로 변환하고 허용되지 않는 문자를 밑줄로 대체
        String converted = input.toLowerCase().replaceAll("[^" + validCharacters + "]", "_");

        return converted;
    }

}
