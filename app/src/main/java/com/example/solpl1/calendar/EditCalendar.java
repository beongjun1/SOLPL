package com.example.solpl1.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EditCalendar extends AppCompatActivity {
    CalendarEditAdapter adapter;
    ArrayList<String> name = new ArrayList<>();
    private List<calendar_item> items = new ArrayList<calendar_item>();
    private DatabaseReference trip_db; // 실시간 데이터베이스
    private DatabaseReference user_account_database; // 실시간 데이터베이스
    private static final int REQUEST_CODE = 1;
    private String key;
    int tripDuration;
    Button calendar_create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar_create = findViewById(R.id.calendar_create);

        RecyclerView recyclerView = findViewById(R.id.main_recyclerView);

        Intent intent = getIntent();
        String startDay = intent.getStringExtra("startDay");
        String endDay = intent.getStringExtra("endDay");
        key = intent.getStringExtra("key");
        Log.d("EditCalendar Key", "key: "+ key);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // 날짜를 파싱하여 LocalDate 객체로 변환
        LocalDate startDate = LocalDate.parse(startDay, formatter);
        LocalDate endDate = LocalDate.parse(endDay, formatter);

        tripDuration = (int) (ChronoUnit.DAYS.between(startDate, endDate) + 1); // 여행 기간 계산

    // 리사이클러뷰 헤더에 날짜 형식을 추가
        for (int i = 0; i < tripDuration; i++) {
            String dayString = startDate.plusDays(i).format(formatter); // 날짜 포맷팅
            name.add(dayString);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CalendarEditAdapter(name, items,key);
        recyclerView.setAdapter(adapter);

        calendar_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String day = data.getStringExtra("selectedDay");
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String userEmail = user.getEmail();
            String convertedPath = convertToValidPath(userEmail);

            Log.d("day", "day: " + day);
            Log.d("key", "key: " + key);

            trip_db = FirebaseDatabase.getInstance().getReference("trip");
            trip_db.child(convertedPath).child(key).child("place").child(day).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d("DataSnapshot", "DataSnapshot: " + dataSnapshot.toString());

                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        Log.d("currentUser", "currentUser: " + currentUser);

                        if (currentUser != null) {
                            String currentUserIdToken = currentUser.getUid();
                            Log.d("currentUserIdToken", "currentUserIdToken: " + currentUserIdToken);

                            List<calendar_item> newItems = new ArrayList<>();
                            for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                                Log.d("DataSnapshot2", "DataSnapshot: " + placeSnapshot.toString());
                                String name = placeSnapshot.child("name").getValue(String.class);
                                String imageUrl = placeSnapshot.child("imageUrl").getValue(String.class);
                                String time = placeSnapshot.child("time").getValue(String.class);
                                String date = placeSnapshot.child("day").getValue(String.class);
                                Log.d("day", "day: " + date);

                                calendar_item item = new calendar_item(name, imageUrl, time, date);
                                newItems.add(item);
                            }

                            // 선택한 날짜의 아이템을 삭제하고 새로운 아이템 추가
                            List<calendar_item> filteredItems = new ArrayList<>();
                            for (calendar_item currentItem : items) {
                                if (!currentItem.getDate().equals(day)) {
                                    filteredItems.add(currentItem);
                                }
                            }
                            filteredItems.addAll(newItems);

                            items.clear();
                            items.addAll(filteredItems);

                            adapter.notifyDataSetChanged(); // 데이터가 변경되었으므로 어댑터에 알려줌
                        } else {
                            Log.d("CurrentUserCheck", "CurrentUser is null");
                        }
                    } else {
                        Log.d("DataSnapshot", "No data found for this day: " + day);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }

    }
    //문자열을 데이터베이스 경로에 사용 가능한 형식으로 변환
    public static String convertToValidPath(String input) {
        // 허용되는 문자: 알파벳 소문자, 숫자, 밑줄(_)
        String validCharacters = "abcdefghijklmnopqrstuvwxyz0123456789_";

        // 입력된 문자열을 소문자로 변환하고 허용되지 않는 문자를 밑줄로 대체
        String converted = input.toLowerCase().replaceAll("[^" + validCharacters + "]", "_");

        return converted;
    }
}





