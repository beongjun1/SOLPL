package com.example.solpl1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.solpl1.calendar.MainCalendar;
import com.example.solpl1.chat.Activity.ChatActivity;
import com.example.solpl1.mainPost.MainPostFragment;
import com.example.solpl1.map.MainMap;
import com.example.solpl1.mypage.mypage_main_activity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 시작하자마자 MainPostFragment로 가기
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new MainPostFragment());
        transaction.commit();

        mAuth = FirebaseAuth.getInstance();
        // 구글 로그인 클라이언트 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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
                        Intent intent1 = new Intent(MainActivity.this, MainCalendar.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.nav_chat:
                        Intent intent2 = new Intent(MainActivity.this, ChatActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_map:
                        Intent intent3 = new Intent(MainActivity.this, MainMap.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_mypage:
                        Intent intent4 = new Intent(MainActivity.this, mypage_main_activity.class);
                        startActivity(intent4);
                        finish();
                        break;
                }
                transaction.commit();
                return true;
            }
        });
    }
    public void logout(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (user != null) {
            // Firebase 로그아웃
            mAuth.signOut();
        }

        if (account != null) {
            // 구글 로그아웃
            mGoogleSignInClient.signOut();
        }

        // 로그아웃 후 로그인 화면으로 이동
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}