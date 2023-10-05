package com.example.solpl1.mainPost.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.calendar.MainCalendar;
import com.example.solpl1.chat.Activity.ChatActivity;
import com.example.solpl1.databinding.ActivityMainPostBinding;
import com.example.solpl1.mainPost.Adapters.PostFragmentsAdapter;
import com.example.solpl1.map.MainMap;
import com.example.solpl1.mypage.mypage_main_activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PostActivity extends AppCompatActivity {

    ActivityMainPostBinding binding;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ViewPager2 viewPager2 = binding.pager;
        viewPager2.setAdapter(new PostFragmentsAdapter(this));

        TabLayout tabLayout = binding.tabLayout;
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0: {
                        tab.setText("게시글");
                        break;
                    }
                    case 1: {
                        tab.setText("now");
                        break;
                    }
                }
            }
        }
        );
        tabLayoutMediator.attach();



        auth = FirebaseAuth.getInstance();
        // 프로필 이미지
        FirebaseDatabase.getInstance().getReference().child("UserAccount")
                .child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAccount userAccount = snapshot.getValue(UserAccount.class);
                        if(userAccount.getImageUrl()== null){                                       //기본 프로필 이미지 설정
                            Picasso.get()                                                           //유저 프로필
                                    .load(R.drawable.default_profile)
                                    .into(binding.profile);
                        } else {
                            Picasso.get()                                                           //유저 프로필
                                    .load(userAccount.getImageUrl())
                                    .placeholder(R.drawable.default_profile)
                                    .into(binding.profile);
                        }

                        //holder.binding.name.setText(userAccount.getName());              // 유저이름
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // 네비게이션 바 연결 및 홈 화면 설정
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()){
                    case R.id.nav_main:
                        break;
                    case R.id.nav_calendar:
                        Intent intent1 = new Intent(PostActivity.this, MainCalendar.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.nav_chat:
                        Intent intent2 = new Intent(PostActivity.this, ChatActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_map:
                        Intent intent3 = new Intent(PostActivity.this, MainMap.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_mypage:
                        Intent intent4 = new Intent(PostActivity.this, mypage_main_activity.class);
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