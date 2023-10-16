package com.example.solpl1.Badge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.example.solpl1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Touch_Resion extends AppCompatActivity {
    Button select_badge;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("UserAccount").child(firebaseAuth.getUid()).child("badge");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_map);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        ImageView imageView = (ImageView) findViewById(R.id.achiv_korea);
        select_badge = (Button) findViewById(R.id.select_badge);
        select_badge.setVisibility(View.VISIBLE);
        Intent intent=getIntent();
        String resion=intent.getStringExtra("resion");
        int count=intent.getIntExtra("count",0);

        VectorChildFinder vector = new VectorChildFinder(Touch_Resion.this, R.drawable.map_of_south_korea, imageView);
        VectorDrawableCompat.VFullPath path=null;
        switch (resion){
            case "goyang":
                path = vector.findPathByName("고양");
                break;
            case "suwon":
                path = vector.findPathByName("수원");
                break;
            case "siheung":
                path = vector.findPathByName("시흥");
                break;
            case "osan":
                path = vector.findPathByName("오산");
                break;
            case "yongin":
                path = vector.findPathByName("용인");
                break;
            case "hwaseong":
                path = vector.findPathByName("화성");
                break;
            case "suncheon":
                path = vector.findPathByName("순천");
                break;
            case "jinju":
                path = vector.findPathByName("진주");
                break;
            case "jeju":
                path = vector.findPathByName("제주");
                break;
            case "gangnam":
                path = vector.findPathByName("강남구");
                break;
            case "seocho":
                path = vector.findPathByName("서초구");
                break;
            case "geumcheon":
                path = vector.findPathByName("금천구");
                break;
            case "mapo":
                path = vector.findPathByName("마포구");
                break;
            case "yongsan":
                path = vector.findPathByName("용산구");
                break;
            case "seongdong":
                path = vector.findPathByName("성동구");
                break;
            case "jonglo":
                path = vector.findPathByName("종로구");
                break;
            case "yeongdeungpo":
                path = vector.findPathByName("영등포구");
                break;
            case "seongnam":
                path = vector.findPathByName("성남");
                break;
            case "anyang":
                path = vector.findPathByName("안양");
                break;
            case "yangyang":
                path = vector.findPathByName("양양");
                break;
            case "inje":
                path = vector.findPathByName("인제");
                break;
            case "yanggu":
                path = vector.findPathByName("양구");
                break;
            case "jeongsun":
                path = vector.findPathByName("정선");
                break;
            case "pyeongchang":
                path = vector.findPathByName("평창");
                break;
            case "sogcho":
                path = vector.findPathByName("속초");
                break;
            case "gangleung":
                path = vector.findPathByName("강릉");
                break;
            case "wonju":
                path = vector.findPathByName("원주");
                break;
            case "gongju":
                path = vector.findPathByName("공주");
                break;
            case "eumseong":
                path = vector.findPathByName("음성");
                break;
            case "goesan":
                path = vector.findPathByName("괴산");
                break;
            case "jecheon":
                path = vector.findPathByName("제천");
                break;
            case "sinan":
                path = vector.findPathByName("신안");
                break;
            case "muan":
                path = vector.findPathByName("무안");
                break;
            case "hwasun":
                path = vector.findPathByName("화순");
                break;
            case "goheung":
                path = vector.findPathByName("고흥");
                break;
            case "sunchang":
                path = vector.findPathByName("순창");
                break;
            case "gimje":
                path = vector.findPathByName("김제");
                break;
            case "jeonju":
                path = vector.findPathByName("전주");
                break;
            case "tongyeong":
                path = vector.findPathByName("통영");
                break;
            case "sacheon":
                path = vector.findPathByName("사천");
                break;
            case "milyang":
                path = vector.findPathByName("밀양");
                break;
            case "gimhae":
                path = vector.findPathByName("김해");
                break;
            case "geoje":
                path = vector.findPathByName("거제");
                break;
            case "yangsan":
                path = vector.findPathByName("양산");
                break;
            case "uilyeong":
                path = vector.findPathByName("의령");
                break;
            case "seogwipo":
                path = vector.findPathByName("서귀포");
                break;
        }

        path.setFillColor(Color.RED);

        select_badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count<1){
                    Toast.makeText(Touch_Resion.this,"뱃지가 아직 잠겨 있습니다.",Toast.LENGTH_SHORT).show();
                }
                else if(count==1){
                    Toast.makeText(Touch_Resion.this,"대표 뱃지로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                    databaseReference.setValue(resion+"_off");
                    finish();
                }
                else{
                    Toast.makeText(Touch_Resion.this,"대표 뱃지로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                    databaseReference.setValue(resion+"_on");
                    finish();
                }
            }
        });
    }
}
