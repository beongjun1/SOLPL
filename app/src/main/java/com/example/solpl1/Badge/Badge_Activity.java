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
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("UserAccount").child(firebaseAuth.getUid());
    RECO_RESION reco_resion = new RECO_RESION();
    ImageView represent_badge,goyang,suwon,siheung,osan,yongin,hwaseong,suncheon,jinju,jeju;
    ImageView gangnam,seocho,geumcheon,mapo,yongsan,seongdong,jonglo,yeongdeungpo,seongnam,anyang,yangyang,inje,yanggu,jeongsun,pyeongchang,sogcho,gangleung,wonju,gongju,eumseong,goesan,jecheon,sinan,muan,hwasun,goheung,sunchang,gimje,jeonju,tongyeong,sacheon,milyang,gimhae,geoje,yangsan,uilyeong,seogwipo;
    String badge;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        represent_badge=findViewById(R.id.represent_badge);
        goyang=findViewById(R.id.goyang);
        suwon=findViewById(R.id.suwon);
        siheung=findViewById(R.id.siheung);
        osan=findViewById(R.id.osan);
        yongin=findViewById(R.id.yongin);
        hwaseong=findViewById(R.id.hwaseong);
        suncheon=findViewById(R.id.suncheon);
        jinju=findViewById(R.id.jinju);
        jeju=findViewById(R.id.jeju);
        gangnam=findViewById(R.id.gangnam);
        seocho=findViewById(R.id.seocho);
        geumcheon=findViewById(R.id.geumcheon);
        mapo=findViewById(R.id.mapo);
        yongsan=findViewById(R.id.yongsan);
        seongdong=findViewById(R.id.seongdong);
        jonglo=findViewById(R.id.jonglo);
        yeongdeungpo=findViewById(R.id.yeongdeungpo);
        seongnam=findViewById(R.id.seongnam);
        anyang=findViewById(R.id.anyang);
        yangyang=findViewById(R.id.yangyang);
        inje=findViewById(R.id.inje);
        yanggu=findViewById(R.id.yanggu);
        jeongsun=findViewById(R.id.jeongsun);
        pyeongchang=findViewById(R.id.pyeongchang);
        sogcho=findViewById(R.id.sogcho);
        gangleung=findViewById(R.id.gangleung);
        wonju=findViewById(R.id.wonju);
        gongju=findViewById(R.id.gongju);
        eumseong=findViewById(R.id.eumseong);
        goesan=findViewById(R.id.goesan);
        jecheon=findViewById(R.id.jecheon);
        sinan=findViewById(R.id.sinan);
        muan=findViewById(R.id.muan);
        hwasun=findViewById(R.id.hwasun);
        goheung=findViewById(R.id.goheung);
        sunchang=findViewById(R.id.sunchang);
        gimje=findViewById(R.id.gimje);
        jeonju=findViewById(R.id.jeonju);
        tongyeong=findViewById(R.id.tongyeong);
        sacheon=findViewById(R.id.sacheon);
        milyang=findViewById(R.id.milyang);
        gimhae=findViewById(R.id.gimhae);
        geoje=findViewById(R.id.geoje);
        yangsan=findViewById(R.id.yangsan);
        uilyeong=findViewById(R.id.uilyeong);
        seogwipo=findViewById(R.id.seogwipo);

        databaseReference.child("badge").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                badge=snapshot.getValue(String.class);
                if(badge.equals("대표뱃지")){

                }
                else{
                    switch (badge){
                        case "suncheon_on":
                            represent_badge.setImageResource(R.drawable.suncheon_on);
                            break;
                        case "suncheon_off":
                            represent_badge.setImageResource(R.drawable.suncheon_off);
                            break;
                        case "goyang_on":
                            represent_badge.setImageResource(R.drawable.goyang_on);
                            break;
                        case "goyang_off":
                            represent_badge.setImageResource(R.drawable.goyang_off);
                            break;
                        case "suwon_on":
                            represent_badge.setImageResource(R.drawable.suwon_on);
                            break;
                        case "suwon_off":
                            represent_badge.setImageResource(R.drawable.suwon_off);
                            break;
                        case "jinju_on":
                            represent_badge.setImageResource(R.drawable.jinju_on);
                            break;
                        case "jinju_off":
                            represent_badge.setImageResource(R.drawable.jinju_off);
                            break;
                        case "jeju_on":
                            represent_badge.setImageResource(R.drawable.jeju_on);
                            break;
                        case "jeju_off":
                            represent_badge.setImageResource(R.drawable.jeju_off);
                            break;
                        case "yongin_on":
                            represent_badge.setImageResource(R.drawable.yongin_on);
                            break;
                        case "yongin_off":
                            represent_badge.setImageResource(R.drawable.yongin_off);
                            break;
                        case "hwaseong_on":
                            represent_badge.setImageResource(R.drawable.hwaseong_on);
                            break;
                        case "hwaseong_off":
                            represent_badge.setImageResource(R.drawable.hwaseong_off);
                            break;
                        case "osan_on":
                            represent_badge.setImageResource(R.drawable.osan_on);
                            break;
                        case "osan_off":
                            represent_badge.setImageResource(R.drawable.osan_off);
                            break;
                        case "siheung_on":
                            represent_badge.setImageResource(R.drawable.siheung_on);
                            break;
                        case "siheung_off":
                            represent_badge.setImageResource(R.drawable.siheung_off);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("reco_resion").addValueEventListener(new ValueEventListener() {
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
                intent.putExtra("count",reco_resion.get고양());
                startActivity(intent);
            }
        });
        suwon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","suwon");
                intent.putExtra("count",reco_resion.get수원());
                startActivity(intent);
            }
        });
        siheung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","siheung");
                intent.putExtra("count",reco_resion.get시흥());
                startActivity(intent);
            }
        });
        osan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","osan");
                intent.putExtra("count",reco_resion.get오산());
                startActivity(intent);
            }
        });
        yongin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","yongin");
                intent.putExtra("count",reco_resion.get용인());
                startActivity(intent);
            }
        });
        hwaseong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","hwaseong");
                intent.putExtra("count",reco_resion.get화성());
                startActivity(intent);
            }
        });
        suncheon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","suncheon");
                intent.putExtra("count",reco_resion.get순천());
                startActivity(intent);
            }
        });
        jinju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","jinju");
                intent.putExtra("count",reco_resion.get진주());
                startActivity(intent);
            }
        });
        jeju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","jeju");
                intent.putExtra("count",reco_resion.get제주());
                startActivity(intent);
            }
        });
        gangnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","gangnam");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        seocho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","seocho");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        geumcheon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","geumcheon");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        mapo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","mapo");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        yongsan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","yongsan");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        seongdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","seongdong");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        jonglo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","jonglo");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        yeongdeungpo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","yeongdeungpo");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        seongnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","seongnam");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        anyang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","anyang");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        yangyang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","yangyang");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        inje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","inje");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        gangleung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","gangleung");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        jeongsun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","jeongsun");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        yanggu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","yanggu");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        pyeongchang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","pyeongchang");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        sogcho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","sogcho");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        wonju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","wonju");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        gongju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","gongju");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        eumseong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","eumseong");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        goesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","goesan");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        jecheon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","jecheon");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        sinan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","sinan");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        muan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","muan");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        hwasun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","hwasun");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        goheung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","goheung");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        sunchang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","sunchang");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        gimje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","gimje");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        jeonju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","jeonju");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        tongyeong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","tongyeong");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        sacheon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","sacheon");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        milyang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","milyang");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        gimhae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","gimhae");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        geoje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","geoje");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        yangsan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","yangsan");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        uilyeong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","uilyeong");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
        seogwipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Badge_Activity.this,Touch_Resion.class);
                intent.putExtra("resion","seogwipo");
                intent.putExtra("count",0);
                startActivity(intent);
            }
        });
    }
}