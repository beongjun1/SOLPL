package com.example.solpl1.mainPost.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.widget.SearchView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.solpl1.MainActivity;
import com.example.solpl1.R;
import com.example.solpl1.ShakeDetector;
import com.example.solpl1.UserAccount;
import com.example.solpl1.calendar.MainCalendar;
import com.example.solpl1.chat.Activity.ChatActivity;
import com.example.solpl1.chat.Adapters.ChatUserListAdapter;
import com.example.solpl1.chat.Models.ChatItem;
import com.example.solpl1.databinding.ActivityMainPostBinding;
import com.example.solpl1.mainPost.Adapters.AllUserListAdapter;
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

import java.util.ArrayList;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {

    ActivityMainPostBinding binding;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth auth;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    int nCurrentPermission=0;
    ArrayList<UserAccount> userList = new ArrayList<>();
    AllUserListAdapter userListAdapter;
    static final int PERMISSIONS_REQUEST = 0x0000001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        OnCheckPermission();

        // 유저 검색기능
        binding.searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                //감지시 할 작업 작성
                Toast.makeText(PostActivity.this,"흔들림 감지",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(PostActivity.this, SOSActivity.class);
                startActivity(intent);
            }
        });



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
    private void OnCheckPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this,"앱 실행을 위해서는 권한을 설정해야 합니다.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS},PERMISSIONS_REQUEST);
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS},PERMISSIONS_REQUEST);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "앱 실행을 위한 권한이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "앱 실행을 위한 권한이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,    SensorManager.SENSOR_DELAY_UI);
    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_all_user_list);


        RecyclerView recyclerView = dialog.findViewById(R.id.allUserRecyclerview);
        SearchView searchView = dialog.findViewById(R.id.user_search);
        userListAdapter = new AllUserListAdapter(userList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(userListAdapter);


        //user 검색
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userSearch(newText);
                return true;
            }
        });


        FirebaseDatabase.getInstance().getReference().child("UserAccount")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                            String uid = dataSnapshot.getKey();
                            userList.add(userAccount);
                            Log.e("user Id",uid);
                        }
                        userListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    private void userSearch(String newText) {
        ArrayList<UserAccount> filteredList = new ArrayList<>();
        for(UserAccount item : userList){
            if(item.getName().toLowerCase(Locale.KOREA).contains(newText.toLowerCase(Locale.KOREA))){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this,"해당 사용자가 없습니다.",Toast.LENGTH_SHORT).show();
        }else {
            userListAdapter.setFilteredList(filteredList);
        }
    }

}