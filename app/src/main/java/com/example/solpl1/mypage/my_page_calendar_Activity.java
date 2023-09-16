package com.example.solpl1.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;


import androidx.annotation.NonNull;
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
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class my_page_calendar_Activity extends AppCompatActivity {
    private my_page_calendar_adapter adapter;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();

    private List<my_page_calendar_item> items = new ArrayList<my_page_calendar_item>();
    private DatabaseReference trip_db; // 실시간 데이터베이스
    private DatabaseReference user_account_database; // 실시간 데이터베이스
    private String key;
    int tripDuration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_calendar);


        RecyclerView recyclerView = findViewById(R.id.my_page_calendar_recycler_view);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        Log.d("EditCalendar Key", "key: "+ key);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> dateList = new ArrayList<>();




        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userEmail = user.getEmail();
        String convertedPath = convertToValidPath(userEmail);
        Log.d("key", "key: " + key);



        adapter = new my_page_calendar_adapter(dateList,items);
        recyclerView.setAdapter(adapter); // 이 위치에서 어댑터를 설정합니다.
        if (user != null) {
            DatabaseReference userTripsRef = FirebaseDatabase.getInstance().getReference("trip").child(convertedPath).child(key);
            userTripsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String tripTitle = dataSnapshot.child("title").getValue(String.class);
                        Log.d("tripTitle","tripTitle : " + tripTitle);
                        String tripStart = dataSnapshot.child("startDay").getValue(String.class);
                        Log.d("tripStart","tripStart : " + tripStart);
                        String tripEnd = dataSnapshot.child("endDay").getValue(String.class);
                        Log.d("tripEnd","tripEnd : " + tripEnd);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    try {
                        Date startDate = sdf.parse(tripStart);
                        Date endDate = sdf.parse(tripEnd);

                        // Calendar 객체를 사용하여 날짜를 계산
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(startDate);

                        while (!calendar.getTime().after(endDate)) {
                            Date currentDate = calendar.getTime();
                            String formattedDate = sdf.format(currentDate);
                            dateList.add(formattedDate);

                            // formattedDate에는 startDay와 endDay 사이의 날짜가 들어 있음
                            Log.d("DateBetween", "Date: " + formattedDate);

                            // 여기에서 원하는 작업을 수행할 수 있음

                            // 다음 날짜로 이동
                            calendar.add(Calendar.DATE, 1);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    adapter.setDates(dateList);
                    Log.d("dateList", "dateList: " + dateList);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

        }
        // 밖에서 newItems 리스트를 정의합니다.
        for (String day : dateList) {
        Log.d("date List Day","day: " + day);
        }
        trip_db = FirebaseDatabase.getInstance().getReference();
        CountDownLatch latch = new CountDownLatch(dateList.size()); // 카운트다운 래치 초기화

//        for (String day : dateList) {
            trip_db.child("trip").child(convertedPath).child(key).child("place").child("2023-09-23").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("MyPageDataSnapshot", "DataSnapshot: " + dataSnapshot.toString());
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    Log.d("currentUser", "currentUser: " + currentUser);
                    List<my_page_calendar_item> newItems = new ArrayList<>();

                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String name = childSnapshot.child("name").getValue(String.class);
                        String imageUrl = childSnapshot.child("imageUrl").getValue(String.class);
                        String time = childSnapshot.child("time").getValue(String.class);
                        String date = childSnapshot.child("day").getValue(String.class);

                        Log.d("MyPageDataSnapshot", "name: " + name);
                        Log.d("MyPageDataSnapshot", "imageUrl: " + imageUrl);
                        Log.d("MyPageDataSnapshot", "time: " + time);
                        Log.d("MyPageDataSnapshot", "day: " + date);

                        my_page_calendar_item item = new my_page_calendar_item(name, imageUrl, time, date);
                        newItems.add(item);
                    }

                    // 선택한 날짜의 아이템을 삭제하고 새로운 아이템 추가
                    List<my_page_calendar_item> filteredItems = new ArrayList<>();
//                    for (my_page_calendar_item currentItem : items) {
//                        if (!currentItem.getDate().equals(day)) {
//                            filteredItems.add(currentItem);
//                        }
//                    }
                    filteredItems.addAll(newItems);

//                    items.clear();
                    items.addAll(filteredItems);

                    adapter.notifyDataSetChanged(); // 데이터가 변경되었으므로 어댑터에 알려줌

                    // 작업이 끝났음을 카운트다운 래치에 알립니다.
//                    latch.countDown();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // 에러 처리
                    Log.d("MyPageDataSnapshot", "Firebase Error: " + error.getMessage());

                    // 작업이 끝났음을 카운트다운 래치에 알립니다.
//                    latch.countDown();
                }
            });
        }




//    }
    //문자열을 데이터베이스 경로에 사용 가능한 형식으로 변환
    public static String convertToValidPath(String input) {
        // 허용되는 문자: 알파벳 소문자, 숫자, 밑줄(_)
        String validCharacters = "abcdefghijklmnopqrstuvwxyz0123456789_";

        // 입력된 문자열을 소문자로 변환하고 허용되지 않는 문자를 밑줄로 대체
        String converted = input.toLowerCase().replaceAll("[^" + validCharacters + "]", "_");

        return converted;
    }
}





