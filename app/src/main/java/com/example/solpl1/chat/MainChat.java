package com.example.solpl1.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.solpl1.R;
import com.example.solpl1.MainActivity;
import com.example.solpl1.calendar.MainCalendar;
import com.example.solpl1.map.MainMap;
import com.example.solpl1.mypage.MainMypage;
import com.example.solpl1.mypage.mypage_main_activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainChat extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_main:
                        Intent intent = new Intent(MainChat.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_calendar:
                        Intent intent1 = new Intent(MainChat.this, MainCalendar.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.nav_chat:
                        break;
                    case R.id.nav_map:
                        Intent intent3 = new Intent(MainChat.this, MainMap.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_mypage:
                        Intent intent4 = new Intent(MainChat.this, MainMypage.class);
                        startActivity(intent4);
                        finish();
                        break;
                }
                return true;
            }
        });
    }
}
