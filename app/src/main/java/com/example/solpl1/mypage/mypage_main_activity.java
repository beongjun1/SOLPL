package com.example.solpl1.mypage;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;
import com.example.solpl1.R;
import com.example.solpl1.MainActivity;
import com.example.solpl1.calendar.MainCalendar;
import com.example.solpl1.chat.chat_activity;
import com.example.solpl1.chat.chat_room_activity;
import com.example.solpl1.map.MainMap;
import com.example.solpl1.my_page_item;
import com.example.solpl1.post_management.post_management_activity;
import com.example.solpl1.post_management.review_management_activity;
import com.example.solpl1.profile_edit.profile_edit_activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class mypage_main_activity extends AppCompatActivity {
    private static final int PROFILE_EDIT_REQUEST_CODE = 1;
    private RecyclerView mRecyclerView;
    private ArrayList<my_page_item> tripList;
    private my_page_recycler_adapter mRecyclerAdapter;
    private TextView postCountTextView;
    Button my_page_writing,trip_current;
    CircleImageView profile_img;
    ImageView menu;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        RatingBar ratingBar = findViewById(R.id.my_page_rating_bar);

        profile_img= findViewById(R.id.mypage_profile_img);
        menu = findViewById(R.id.my_page_menu);

        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerAdapter = new my_page_recycler_adapter();

        postCountTextView = findViewById(R.id.post_count_textview);

        my_page_writing = findViewById(R.id.mypage_writing);

        trip_current=findViewById(R.id.trip_current);

        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        tripList = new ArrayList<>();
        tripList.add(new my_page_item(R.drawable.ex1,"2023.03.12 ~ 2023.03.15","수원 여행"));
        tripList.add(new my_page_item(R.drawable.activity_post_picture,"2023.03.11 ~ 2023.03.14","수원 여행"));
        tripList.add(new my_page_item(R.drawable.activity_post_picture,"2023.03.10 ~ 2023.03.13","수원 여행"));
        tripList.add(new my_page_item(R.drawable.activity_post_picture,"2023.03.09 ~ 2023.03.12","수원 여행"));
        tripList.add(new my_page_item(R.drawable.activity_post_picture,"2023.03.08 ~ 2023.03.11","수원 여행"));
        tripList.add(new my_page_item(R.drawable.activity_post_picture,"2023.03.07 ~ 2023.03.10","수원 여행"));
        tripList.add(new my_page_item(R.drawable.activity_post_picture,"2023.03.06 ~ 2023.03.09","수원 여행"));

        my_page_item item = tripList.get(0); // 0번 인덱스에 저장된 데이터 가져오기
        mRecyclerAdapter.setTripList(tripList);

//        //Intent를 받고 평균 값을 추출합니다.
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra("average_rating")) {
//            float avg_rating = intent.getFloatExtra("average_rating", 0f);
//            //평균값을 rating_bar에 넣는다
//            ratingBar.setRating(avg_rating);
//        }


        updatePostCount(); //포스트 숫자에 맞게 count늘어나는 코드

        setUserNameFromDatabase(); // 데이터베이스에서 userName가져오기

        my_page_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mypage_main_activity.this, my_page_writing_activity.class);
                startActivity(intent);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PopupMenu 객체 생성
                PopupMenu popup= new PopupMenu(mypage_main_activity.this, v); //두 번째 파라미터가 팝업메뉴가 붙을 뷰
                getMenuInflater().inflate(R.menu.mypage_menu, popup.getMenu());


                //팝업메뉴의 메뉴아이템을 선택하는 것을 듣는 리스너 객체 생성 및 설정
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.my_page_profile_edit:
                                Intent profileEditIntent = new Intent(mypage_main_activity.this, profile_edit_activity.class);
                                startActivityForResult(profileEditIntent, PROFILE_EDIT_REQUEST_CODE);
                                break;
                            case R.id.my_page_message:
                                Intent messageIntent = new Intent(mypage_main_activity.this, chat_room_activity.class);
                                startActivity(messageIntent);
                                break;
                            case R.id.my_page_post_management:
                                Intent postManagementIntent = new Intent(mypage_main_activity.this, post_management_activity.class);
                                startActivity(postManagementIntent);
                                break;
                            case R.id.my_page_review_management:
                                Intent reviewManagementIntent = new Intent(mypage_main_activity.this, review_management_activity.class);
                                startActivity(reviewManagementIntent);
                                break;


                        }
                       return false;
                    }
                });
                popup.show();
            }
        });


        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_main:
                        Intent intent = new Intent(mypage_main_activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    case R.id.nav_calendar:
                        Intent intent1 = new Intent(mypage_main_activity.this, MainCalendar.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.nav_chat:
                        Intent intent2 = new Intent(mypage_main_activity.this, chat_activity.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_map:
                        Intent intent3 = new Intent(mypage_main_activity.this, MainMap.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_mypage:
                        break;
                }
                return true;
            }
        });

        trip_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mypage_main_activity.this, my_page_achievement.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("imageUri")) {
                String imageUri = data.getStringExtra("imageUri");
                // 이미지 URL을 사용하여 이미지 업데이트 처리
                if (imageUri != null && !imageUri.isEmpty()) {
                    Glide.with(getApplicationContext())
                            .load(imageUri)
                            .into(profile_img);
                }
            }
        }
    }

    private void updatePostCount() {
        int postCount = tripList.size();
        String formattedPostCount = formatNumber(postCount); // 숫자 형식을 포맷팅하여 문자열로 변환
        postCountTextView.setText(formattedPostCount);
    }

    private String formatNumber(int number) {
        // 숫자를 천 단위로 구분하여 문자열로 변환
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
        decimalFormat.setGroupingUsed(true);
        return decimalFormat.format(number);
    }

    //FirebaseDatabase에서 userAccount에 있는 name을 가져온다.
    private void setUserNameFromDatabase() {
        // 현재 로그인한 사용자의 이메일을 가져오는 코드 (Firebase Authentication을 사용한다고 가정)
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = currentUser.getEmail();

        // 데이터베이스에서 사용자의 이메일을 기준으로 이름을 가져오는 쿼리 수행
        FirebaseDatabase.getInstance().getReference().child("UserAccount")
                .orderByChild("emailId").equalTo(userEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String userName = childSnapshot.child("name").getValue(String.class);
                                String imageUrl = childSnapshot.child("imageUrl").getValue(String.class);

                                if (userName != null && !userName.isEmpty()) {
                                    TextView userNameTextView = findViewById(R.id.my_page_user_name);
                                    userNameTextView.setText(userName);
                                }
                                if (imageUrl != null && !imageUrl.isEmpty()) {
                                    Glide.with(getApplicationContext())
                                            .load(imageUrl)
                                            .into(profile_img);
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("DatabaseError", "Error: " + databaseError.getMessage());
                    }
                });
    }

}