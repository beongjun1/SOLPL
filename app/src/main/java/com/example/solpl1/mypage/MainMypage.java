package com.example.solpl1.mypage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.example.solpl1.MainActivity;
import com.example.solpl1.R;
import com.example.solpl1.calendar.MainCalendar;
import com.example.solpl1.chat.MainChat;
import com.example.solpl1.map.MainMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMypage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_main:
                        Intent intent = new Intent(MainMypage.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_calendar:
                        Intent intent1 = new Intent(MainMypage.this, MainCalendar.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.nav_chat:
                        Intent intent2 = new Intent(MainMypage.this, MainChat.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_map:
                        Intent intent3 = new Intent(MainMypage.this, MainMap.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_mypage:
                        break;
                }
                return true;
            }
        });


        ImageView imageView = findViewById(R.id.map_korea);

        VectorChildFinder vector = new VectorChildFinder(this, R.drawable.map_of_south_korea, imageView);
        VectorDrawableCompat.VFullPath path = vector.findPathByName("용인");
        path.setFillColor(Color.RED);

        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("광명");
        path1.setFillColor(Color.BLUE);

        imageView.invalidate();

    }

}
