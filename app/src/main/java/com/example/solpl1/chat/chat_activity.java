package com.example.solpl1.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.MainActivity;
import com.example.solpl1.R;
import com.example.solpl1.MainActivity;
import com.example.solpl1.calendar.MainCalendar;
import com.example.solpl1.map.MainMap;
import com.example.solpl1.mypage.mypage_main_activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class chat_activity extends AppCompatActivity {
    private ArrayList<chat_item> chatList;
    private chat_recycler_adapter chatRecyclerAdapter;
    private RecyclerView chat_recycler;
    BottomNavigationView bottomNavigationView;
    TextView chat_room_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        chat_recycler = findViewById(R.id.chat_recycler_view);

        chatRecyclerAdapter = new chat_recycler_adapter();

        chat_recycler.setAdapter(chatRecyclerAdapter);
        chat_recycler.setLayoutManager(new LinearLayoutManager(this));


        chatList = new ArrayList<>();
        chatList.add(new chat_item("강원도 속초 여행 중 이신분 !", "홍길동", "example@example.com", "내용입니다", "11:36", R.drawable.ex1, "3", "5"));
        chatList.add(new chat_item("강원도 속초 여행 중 이신분 !", "홍길동", "example@example.com", "내용입니다", "11:36", R.drawable.community, "3", "5"));
        chatList.add(new chat_item("강원도 속초 여행 중 이신분 !", "홍길동", "example@example.com", "내용입니다", "11:36", R.drawable.community, "3", "5"));
        chatList.add(new chat_item("강원도 속초 여행 중 이신분 !", "홍길동", "example@example.com", "내용입니다", "11:36", R.drawable.community, "3", "5"));
        chatList.add(new chat_item("강원도 속초 여행 중 이신분 !", "홍길동", "example@example.com", "내용입니다", "11:36", R.drawable.community, "3", "5"));
        chatList.add(new chat_item("강원도 속초 여행 중 이신분 !", "홍길동", "example@example.com", "내용입니다", "11:36", R.drawable.community, "3", "5"));
        chatList.add(new chat_item("강원도 속초 여행 중 이신분 !", "홍길동", "example@example.com", "내용입니다", "11:36", R.drawable.community, "3", "5"));
        chatList.add(new chat_item("강원도 속초 여행 중 이신분 !", "홍길동", "example@example.com", "내용입니다", "11:36", R.drawable.community, "3", "5"));
        chatList.add(new chat_item("강원도 속초 여행 중 이신분 !", "홍길동", "example@example.com", "내용입니다", "11:36", R.drawable.community, "3", "5"));

        chatRecyclerAdapter.setChatList(chatList);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_main:
                        Intent intent2 = new Intent(chat_activity.this, MainActivity.class);
                        startActivity(intent2);
                        finish();
                    case R.id.nav_calendar:
                        Intent intent1 = new Intent(chat_activity.this, MainCalendar.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.nav_chat:
                        break;
                    case R.id.nav_map:
                        Intent intent3 = new Intent(chat_activity.this, MainMap.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_mypage:
                        Intent intent4 = new Intent(chat_activity.this, mypage_main_activity.class);
                        startActivity(intent4);
                        finish();
                        break;
                }
                return true;
            }
        });

        chat_room_btn =findViewById(R.id.chat_room_btn);
        chat_room_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chat_activity.this, chat_room_activity.class);
                startActivity(intent);
            }
        });
    }
}
