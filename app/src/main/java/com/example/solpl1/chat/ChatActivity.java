package com.example.solpl1.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.solpl1.chat.Adapters.FragmentsAdapter;
import com.example.solpl1.databinding.ActivityChat2Binding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChatActivity extends AppCompatActivity {

    ActivityChat2Binding binding;
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


    }
}