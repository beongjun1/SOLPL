package com.example.solpl1.Badge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.solpl1.R;
import com.example.solpl1.mypage.RECO_RESION;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Badge_Activity extends AppCompatActivity {

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("UserAccount").child(firebaseAuth.getUid()).child("reco_resion");
    RECO_RESION reco_resion = new RECO_RESION();
    ImageView goyang,suwon,siheung,osan,yongin,hwaseong,suncheon,jinju,jeju;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        goyang=findViewById(R.id.goyang);
        suwon=findViewById(R.id.suwon);
        siheung=findViewById(R.id.siheung);
        osan=findViewById(R.id.osan);
        yongin=findViewById(R.id.yongin);
        hwaseong=findViewById(R.id.hwaseong);
        suncheon=findViewById(R.id.suncheon);
        jinju=findViewById(R.id.jinju);
        jeju=findViewById(R.id.jeju);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reco_resion=snapshot.getValue(RECO_RESION.class);
                if(reco_resion.get고양()>1){
                    goyang.setImageResource(R.drawable.osan_on);
                } else if (reco_resion.get고양()>0) {
                    goyang.setImageResource(R.drawable.osan_off);
                }
                if(reco_resion.get수원()>1){
                    suwon.setImageResource(R.drawable.suwon_on);
                } else if (reco_resion.get수원()>0) {
                    suwon.setImageResource(R.drawable.suwon_off);
                }
                if(reco_resion.get시흥()>1){
                    siheung.setImageResource(R.drawable.siheung_on);
                } else if (reco_resion.get시흥()>0) {
                    siheung.setImageResource(R.drawable.siheung_off);
                }
                if(reco_resion.get오산()>1){
                    osan.setImageResource(R.drawable.osan_on);
                } else if (reco_resion.get오산()>0) {
                    osan.setImageResource(R.drawable.osan_off);
                }
                if(reco_resion.get용인()>1){
                    yongin.setImageResource(R.drawable.yongin_on);
                } else if (reco_resion.get용인()>0) {
                    yongin.setImageResource(R.drawable.yongin_off);
                }
                if(reco_resion.get화성()>1){
                    hwaseong.setImageResource(R.drawable.hwaseong_on);
                } else if (reco_resion.get화성()>0) {
                    hwaseong.setImageResource(R.drawable.hwaseong_off);
                }
                if(reco_resion.get순천()>1){
                    suncheon.setImageResource(R.drawable.suncheon_on);
                } else if (reco_resion.get순천()>0) {
                    suncheon.setImageResource(R.drawable.suncheon_off);
                }
                if(reco_resion.get진주()>1){
                    jinju.setImageResource(R.drawable.jinju_on);
                } else if (reco_resion.get진주()>0) {
                    jinju.setImageResource(R.drawable.jinju_off);
                }
                if(reco_resion.get제주()>1){
                    jeju.setImageResource(R.drawable.jeju_on);
                } else if (reco_resion.get제주()>0) {
                    jeju.setImageResource(R.drawable.jeju_off);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        goyang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","goyang");
                startActivity(intent);
            }
        });
        suwon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","suwon");
                startActivity(intent);
            }
        });
        siheung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","siheung");
                startActivity(intent);
            }
        });
        osan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","osan");
                startActivity(intent);
            }
        });
        yongin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","yongin");
                startActivity(intent);
            }
        });
        hwaseong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","hwaseong");
                startActivity(intent);
            }
        });
        suncheon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","suncheon");
                startActivity(intent);
            }
        });
        jinju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","jinju");
                startActivity(intent);
            }
        });
        jeju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","jeju");
                startActivity(intent);
            }
        });

    }
}