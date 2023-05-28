package com.example.solpl1.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.solpl1.R;
import com.example.solpl1.MainActivity;
import com.example.solpl1.chat.chat_activity;
import com.example.solpl1.map.MainMap;
import com.example.solpl1.mypage.mypage_main_activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainCalendar extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_main:
                        Intent intent = new Intent(MainCalendar.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_calendar:
                        break;
                    case R.id.nav_chat:
                        Intent intent2 = new Intent(MainCalendar.this, chat_activity.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_map:
                        Intent intent3 = new Intent(MainCalendar.this, MainMap.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_mypage:
                        Intent intent4 = new Intent(MainCalendar.this, mypage_main_activity.class);
                        startActivity(intent4);
                        finish();
                        break;
                }
                return true;
            }
        });
    }
}
