package com.example.solpl1.chat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.solpl1.MainActivity;
import com.example.solpl1.R;
import com.example.solpl1.calendar.MainCalendar;
import com.example.solpl1.chat.Adapters.FragmentsAdapter;
import com.example.solpl1.databinding.ActivityChat2Binding;
import com.example.solpl1.mainPost.MainPostFragment;
import com.example.solpl1.map.MainMap;
import com.example.solpl1.mypage.mypage_main_activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChatActivity extends AppCompatActivity {

    ActivityChat2Binding binding;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChat2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ViewPager2 viewPager2 = binding.pager;
        viewPager2.setAdapter(new FragmentsAdapter(this));

        TabLayout tabLayout = binding.tabLayout;
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0: {
                        tab.setText("가이드 채팅");
                        break;
                    }
                    case 1: {
                        tab.setText("함께 여행하기");
                        break;
                    }
                    case 2: {
                        tab.setText("주변 채팅");
                        break;
                    }
                }
            }
        }
        );
        tabLayoutMediator.attach();

        // 네비게이션 바 연결 및 홈 화면 설정
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()){
                    case R.id.nav_main:
                        transaction.replace(R.id.container, new MainPostFragment());
                        break;
                    case R.id.nav_calendar:
                        Intent intent1 = new Intent(ChatActivity.this, MainCalendar.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.nav_chat:
                        Intent intent2 = new Intent(ChatActivity.this, ChatActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_map:
                        Intent intent3 = new Intent(ChatActivity.this, MainMap.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_mypage:
                        Intent intent4 = new Intent(ChatActivity.this, mypage_main_activity.class);
                        startActivity(intent4);
                        finish();
                        break;
                }
                transaction.commit();
                return true;
            }
        });



    }
}